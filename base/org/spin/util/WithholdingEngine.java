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

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_C_DocType;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.MDocType;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.process.DocAction;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.spin.model.MWHWithholding;
import org.spin.model.MWHDefinition;
import org.spin.model.MWHLog;
import org.spin.model.MWHSetting;

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
	private HashMap<String, String> processLog;

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
	 *	@param document persistent objects
	 *	@param documentTiming see ModelValidator.TIMING_ constants
     *	@return error message or null
	 */
	public String fireDocValidate(DocAction document, int documentTiming, int documentTypeId) {
		if (document == null
				|| documentTypeId <= 0) {
			return null;
		}
		//	Get Document Type
		MDocType documentType = MDocType.get(document.getCtx(), documentTypeId);
		if(documentType == null) {
			return null;
		}
		//	flush return values
		returnValues = new HashMap<String, Object>();
		processLog = new HashMap<String, String>();
		//	Apply Listener
		MWHDefinition.getFromDocumentType(Env.getCtx(), documentTypeId)
			.forEach(withholding -> processWithholding(withholding, document, ModelValidator.documentEventValidators[documentTiming]));
		//	default
		return errorMessage.toString();
	}
	
	/**
	 * Fire for document and get document type from PO object
	 * @param document
	 * @param documentTiming
	 * @return
	 */
	public String fireDocValidate(DocAction document, int documentTiming) {
		if(document == null) {
			return null;
		}
		//	Validate for Document Type Target
		int documentTypeId = ((PO)document).get_ValueAsInt(I_C_Invoice.COLUMNNAME_C_DocTypeTarget_ID);
		if(documentTypeId <= 0) {
			documentTypeId = ((PO)document).get_ValueAsInt(I_C_DocType.COLUMNNAME_C_DocType_ID);
		}
		return fireDocValidate(document, documentTiming, documentTypeId);
	}
	
	/**
	 * 	Fire Model Change.
	 * 	Call modelChange method of added validators
	 *	@param document persistent objects
	 *	@param changeType ModelValidator.TYPE_*
	 *	@return error message or NULL for no veto
	 */
	public String fireModelChange(DocAction document, int changeType, int documentTypeId) {
		if (document == null
				|| documentTypeId <= 0) {
			return null;
		}
		//	Get Document Type
		MDocType documentType = MDocType.get(document.getCtx(), documentTypeId);
		if(documentType == null) {
			return null;
		}
		//	flush return values
		returnValues = new HashMap<String, Object>();
		processLog = new HashMap<String, String>();
		//	Apply Listener
		MWHDefinition.getFromDocumentType(Env.getCtx(), documentTypeId)
			.forEach(withholding -> processWithholding(withholding, document, ModelValidator.tableEventValidators[changeType]));
		//	default
		return errorMessage.toString();
	}
	
	/**
	 * Process withholding
	 * @param withholdingDefinition
	 * @param po
	 * @param docTiming
	 */
	private void processWithholding(MWHDefinition withholdingDefinition, DocAction document, String eventModelValidator) {
		withholdingDefinition.getSettingList(((PO)document).get_TableName(), eventModelValidator)
			.stream()
			.sorted(Comparator.comparing(MWHSetting::getSeqNo))
			.forEach(setting -> {
				try {
					AbstractWithholdingSetting settingRunningImplementation = setting.getSettingInstance();
					//	Validate Null Value
					if(settingRunningImplementation == null) {
						throw new AdempiereException("@WH_Setting_ID@ @WithholdingClassName@ @NotFound@");
					}
					//	Set default values
					settingRunningImplementation.setWithholdingDefinition(withholdingDefinition);
					settingRunningImplementation.setDocument(document);
					settingRunningImplementation.setTransactionName(document.get_TrxName());
					//	Verify if document is valid
					if(settingRunningImplementation.isValid()) {
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
						//	Validate amount
						if(settingRunningImplementation.getWithholdingAmount() != null
								&& settingRunningImplementation.getWithholdingAmount().compareTo(Env.ZERO) > 0) {
							createWithholding(settingRunningImplementation);
						} else {
							createLog(settingRunningImplementation);
						}
					}
					//	Add message
					if(!Util.isEmpty(settingRunningImplementation.getProcessLog())) {
						processLog.put(setting.getWH_Setting_ID() + "|" + document.get_ID(), settingRunningImplementation.getProcessLog());
						createLog(settingRunningImplementation);
					}
				} catch(Exception e) {
					errorMessage.append(e);
				}
			});
	}
	
	/**
	 * Create Allocation for processed setting
	 * @param withholdingRunning
	 */
	private void createWithholding(AbstractWithholdingSetting withholdingRunning) {
		MWHWithholding withholding = new MWHWithholding(withholdingRunning.getCtx(), 0, withholdingRunning.getTransactionName());
		withholding.setDateDoc(new Timestamp(System.currentTimeMillis()));
		withholding.setA_Base_Amount(withholdingRunning.getBaseAmount());
		withholding.setWithholdingAmt(withholdingRunning.getWithholdingAmount());
		withholding.setWithholdingRate(withholdingRunning.getWithholdingRate());
		withholding.setWH_Definition_ID(withholdingRunning.getDefinition().getWH_Definition_ID());
		withholding.setWH_Setting_ID(withholdingRunning.getSetting().getWH_Setting_ID());
		withholding.setC_DocType_ID();
		//	Description
		if(!Util.isEmpty(withholdingRunning.getProcessDescription())) {
			withholding.setDescription(Msg.parseTranslation(withholdingRunning.getCtx(), withholdingRunning.getProcessDescription()));
		}
		//	Add additional references
		//	Note that not exist validation for types
		withholdingRunning.getReturnValues().entrySet().forEach(value -> {
			if(withholding.get_ColumnIndex(value.getKey()) > 0) {
				if(value.getValue() != null) {
					withholding.set_ValueOfColumn(value.getKey(), value.getValue());
				}
			}
		});
		//	Save
		withholding.setDocStatus(MWHWithholding.DOCSTATUS_Drafted);
		withholding.saveEx();
		//	Complete
		if(withholding.processIt(MWHWithholding.ACTION_Complete)) {
			throw new AdempiereException(withholding.getProcessMsg());
		}
	}
	
	/**
	 * Create Event Log
	 * @param withholdingRunning
	 */
	private void createLog(AbstractWithholdingSetting withholdingRunning) {
		if(Util.isEmpty(withholdingRunning.getProcessLog())) {
			return;
		}
		MWHLog log = new MWHLog(withholdingRunning.getCtx(), 0, withholdingRunning.getTransactionName());
		log.setWH_Definition_ID(withholdingRunning.getDefinition().getWH_Definition_ID());
		log.setWH_Setting_ID(withholdingRunning.getSetting().getWH_Setting_ID());
		//	Description
		log.setComments(Msg.parseTranslation(withholdingRunning.getCtx(), withholdingRunning.getProcessLog()));
		//	Add additional references
		//	Note that not exist validation for types
		withholdingRunning.getReturnValues().entrySet().forEach(value -> {
			if(log.get_ColumnIndex(value.getKey()) > 0) {
				if(value.getValue() != null) {
					log.set_ValueOfColumn(value.getKey(), value.getValue());
				}
			}
		});
		//	Save
		log.saveEx();
	}
}
