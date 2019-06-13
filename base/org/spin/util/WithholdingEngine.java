/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2016 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_C_DocType;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.MDocType;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.spin.model.MWHAllocation;
import org.spin.model.MWHDefinition;

/**
 * Withholding Management Class for Setting Engine
 *
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class WithholdingEngine {

	private WithholdingEngine() {
		
	}
	
	/** Engine Singleton				*/
	private static WithholdingEngine settingEngine = null;
	private HashMap<String, Object> returnValues;
	private StringBuffer errorMessage = new StringBuffer();
	private HashMap<String, String> processMessage;

	/**
	 * 	Get Singleton
	 *	@return modelValidatorEngine
	 */
	public synchronized static WithholdingEngine get() {
		if (settingEngine == null)
			settingEngine = new WithholdingEngine();
		return settingEngine;
	}	//	get
	
	/**
	 * Get return Values
	 * @return
	 */
	public HashMap<String, Object> getReturnValues() {
		return returnValues;
	}
	
	/**
	 * Get Error Message
	 * @return
	 */
	public String getErrorMessage() {
		return errorMessage.toString();
	}
	
	/**
	 * 	Fire Document Validation.
	 * 	Call docValidate method of added validators
	 *	@param po persistent objects
	 *	@param documentTiming see ModelValidator.TIMING_ constants
     *	@return error message or null
	 */
	public String fireDocValidate(PO po, int documentTiming, int documentTypeId) {
		if (po == null
				|| documentTypeId <= 0) {
			return null;
		}
		//	Get Document Type
		MDocType documentType = MDocType.get(po.getCtx(), documentTypeId);
		if(documentType == null) {
			return null;
		}
		//	flush return values
		returnValues = new HashMap<String, Object>();
		processMessage = new HashMap<String, String>();
		//	Apply Listener
		MWHDefinition.getFromDocumentType(Env.getCtx(), documentTypeId)
			.forEach(withholding -> processWithholding(withholding, po, ModelValidator.documentEventValidators[documentTiming]));
		//	default
		return errorMessage.toString();
	}
	
	/**
	 * Fire for document and get document type from PO object
	 * @param po
	 * @param documentTiming
	 * @return
	 */
	public String fireDocValidate(PO po, int documentTiming) {
		if(po == null) {
			return null;
		}
		//	Validate for Document Type Target
		int documentTypeId = po.get_ValueAsInt(I_C_Invoice.COLUMNNAME_C_DocTypeTarget_ID);
		if(documentTypeId <= 0) {
			documentTypeId = po.get_ValueAsInt(I_C_DocType.COLUMNNAME_C_DocType_ID);
		}
		return fireDocValidate(po, documentTiming, documentTypeId);
	}
	
	/**
	 * 	Fire Model Change.
	 * 	Call modelChange method of added validators
	 *	@param po persistent objects
	 *	@param changeType ModelValidator.TYPE_*
	 *	@return error message or NULL for no veto
	 */
	public String fireModelChange(PO po, int changeType, int documentTypeId) {
		if (po == null
				|| documentTypeId <= 0) {
			return null;
		}
		//	Get Document Type
		MDocType documentType = MDocType.get(po.getCtx(), documentTypeId);
		if(documentType == null) {
			return null;
		}
		//	flush return values
		returnValues = new HashMap<String, Object>();
		processMessage = new HashMap<String, String>();
		//	Apply Listener
		MWHDefinition.getFromDocumentType(Env.getCtx(), documentTypeId)
			.forEach(withholding -> processWithholding(withholding, po, ModelValidator.tableEventValidators[changeType]));
		//	default
		return errorMessage.toString();
	}
	
	/**
	 * Process withholding
	 * @param withholdingDefinition
	 * @param po
	 * @param docTiming
	 */
	private void processWithholding(MWHDefinition withholdingDefinition, PO po, String eventModelValidator) {
		withholdingDefinition.getSettingList(po.get_TableName(), eventModelValidator)
			.forEach(setting -> {
				try {
					AbstractWithholdingSetting settingRunningImplementation = setting.getSettingInstance();
					//	Validate Null Value
					if(settingRunningImplementation == null) {
						throw new AdempiereException("@WH_Setting_ID@ @WithholdingClassName@ @NotFound@");
					}
					//	Verify if document is valid
					if(settingRunningImplementation.isValid()) {
						settingRunningImplementation.setWithholdingDefinition(withholdingDefinition);
						settingRunningImplementation.setParameter(AbstractWithholdingSetting.PO, po);
						settingRunningImplementation.setTransactionName(po.get_TrxName());
						//	Run It
						String runMessage = settingRunningImplementation.run();
						if(!Util.isEmpty(runMessage)) {
							//	Add new line
							if(errorMessage.length() > 0) {
								errorMessage.append(Env.NL);
							}
							errorMessage.append(runMessage);
						}
						//	Copy Return Value
						for(Entry<String, Object> entry : settingRunningImplementation.getReturnValues().entrySet()) {
							returnValues.put(entry.getKey(), entry.getValue());
						}
						createAllocation(settingRunningImplementation);
					}
					//	Add message
					if(!Util.isEmpty(settingRunningImplementation.getProcessMessage())) {
						processMessage.put(po.get_TableName() + "_" + po.get_ID(), settingRunningImplementation.getProcessMessage());
					}
				} catch(Exception e) {
					errorMessage.append(e);
				}
			});
	}
	
	/**
	 * Create Allocation for processed setting
	 * @param settingRunningImplementation
	 */
	private void createAllocation(AbstractWithholdingSetting settingRunningImplementation) {
		List<WithholdingLine> processedLines = settingRunningImplementation.getWithholdingLines();
		if(processedLines == null
				|| processedLines.size() == 0) {
			// Nothing here
			return;
		}
		//	Process each line
		processedLines.forEach(withholdingLine -> {
			MWHAllocation allocation = new MWHAllocation(settingRunningImplementation.getCtx(), 0, settingRunningImplementation.getTransactionName());
			allocation.setA_Base_Amount(withholdingLine.getBaseAmount());
			allocation.setWithholdingAmt(withholdingLine.getWithholdingAmount());
			//	Save
			allocation.saveEx();
		});
	}
}
