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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.core.domains.models.I_C_DocType;
import org.adempiere.core.domains.models.I_C_Invoice;
import org.compiere.model.MDocType;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.process.DocAction;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.spin.model.MWHDefinition;
import org.spin.model.MWHSetting;
import org.spin.model.MWHType;

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
	public String fireDocValidate(PO document, int documentTiming, int documentTypeId) {
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
		errorMessage = new StringBuffer();
		//	Apply Listener
		MWHDefinition.getFromDocumentType(Env.getCtx(), documentTypeId, document.getAD_Org_ID())
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
	public String fireDocValidate(PO document, int documentTiming) {
		if(document == null) {
			return null;
		}
		//	Validate for Document Type Target
		int documentTypeId = document.get_ValueAsInt(I_C_Invoice.COLUMNNAME_C_DocTypeTarget_ID);
		if(documentTypeId <= 0) {
			documentTypeId = document.get_ValueAsInt(I_C_DocType.COLUMNNAME_C_DocType_ID);
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
	public String fireModelChange(PO document, int changeType, int documentTypeId) {
		//	flush return values
		returnValues = new HashMap<String, Object>();
		processLog = new HashMap<String, String>();
		errorMessage = new StringBuffer();
		//	Get Document Type
		if(DocAction.class.isAssignableFrom(document.getClass())
				|| documentTypeId > 0) {
			MDocType documentType = MDocType.get(document.getCtx(), documentTypeId);
			if(documentType == null) {
				return null;
			}
			//	Apply Listener
			MWHDefinition.getFromDocumentType(Env.getCtx(), documentTypeId, document.getAD_Org_ID())
				.forEach(withholding -> processWithholding(withholding, document, ModelValidator.tableEventValidators[changeType]));
		} else {
			MWHDefinition.getFromTable(Env.getCtx(), document.get_TableName())
				.forEach(withholding -> processWithholding(withholding, document, ModelValidator.tableEventValidators[changeType]));
		}
		//	default
		return errorMessage.toString();
	}
	/**
	 * Process Withholding Event Model Validation Document
	 * @param withholdingDefinition
	 * @param document
	 * @param eventModelValidator
	 */
	private void processWithholding(MWHDefinition withholdingDefinition, PO document, String eventModelValidator) {
		processWithholding(withholdingDefinition, document, eventModelValidator, null, false);
	}
	/**
	 * Process withholding
	 * @param withholdingDefinition
	 * @param po
	 * @param docTiming
	 */
	private void processWithholding(MWHDefinition withholdingDefinition, PO document, String eventModelValidator, HashMap<String, Object> parameters, boolean eventProcess) {
		if (!eventProcess) {
			withholdingDefinition.getSettingList(((PO)document).get_TableName(), eventModelValidator)
				.stream()
				.sorted(Comparator.comparing(MWHSetting::getSeqNo))
				.forEach(setting -> {
					processWithholding(setting, withholdingDefinition, document, eventModelValidator);
				});
		}else {
			withholdingDefinition.getSettingList(eventModelValidator)
				.stream()
				.sorted(Comparator.comparing(MWHSetting::getSeqNo))
				.forEach(setting -> {
					processWithholding(setting, withholdingDefinition, document, eventModelValidator, parameters);
				});
		}
	}
	
	/**
	 * Process Withholding
	 * @param setting
	 * @param withholdingDefinition
	 * @param document
	 * @param eventModelValidator
	 */
	private void processWithholding(MWHSetting setting,MWHDefinition withholdingDefinition, PO document, String eventModelValidator) {
		processWithholding(setting, withholdingDefinition, document, eventModelValidator, null);
	}
	
	/**
	 * Process Withholding
	 * @param setting
	 * @param withholdingDefinition
	 * @param document
	 * @param eventModelValidator
	 */
	private void processWithholding(MWHSetting setting,MWHDefinition withholdingDefinition, PO document, String eventModelValidator, HashMap<String, Object> parameters) {
		try {
			AbstractWithholdingSetting settingRunningImplementation = setting.getSettingInstance();
			//	Validate Null Value
			if(settingRunningImplementation == null) {
				throw new AdempiereException("@WH_Setting_ID@ @WithholdingClassName@ @NotFound@");
			}
			if (parameters !=null)
				settingRunningImplementation.setParameters(parameters);
			//	Set default values
			settingRunningImplementation.setWithholdingDefinition(withholdingDefinition);
			settingRunningImplementation.setDocument(document);
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
				settingRunningImplementation.saveResult();
			}
			//	Add message
			if(!Util.isEmpty(settingRunningImplementation.getProcessLog())) {
				processLog.put(setting.getWH_Setting_ID() + "|" + document.get_ID(), settingRunningImplementation.getProcessLog());
				settingRunningImplementation.saveResult();
			}
		} catch(Exception e) {
			errorMessage.append(e);
		}
	}
	
	/**
	 * Process Withholding Event Type Process
	 * @param document
	 * @return
	 */
	public String fireProcess(PO document, HashMap<String, Object> parameters) {
		return fireProcess(document, null, null, null, parameters);
	}
	
	/**
	 * Process Withholding Event Type Process
	 * @param document
	 * @param type
	 * @return
	 */
	public String fireProcess(PO document, MWHType type, HashMap<String, Object> parameters) {
		return fireProcess(document, type, null, null, parameters);
	}
	
	/**
	 * Process Withholding Event Type Process
	 * @param document
	 * @param type
	 * @param setting
	 * @return
	 */
	public String fireProcess(PO document ,MWHType type, MWHSetting setting, HashMap<String, Object> parameters) {
		return fireProcess(document, type, setting, null, parameters);
	}
	
	/**
	 * Process Withholding Event Type Process 
	 * @param document
	 * @param type
	 * @param setting
	 * @param definition
	 * @return
	 */
	public String fireProcess(PO document ,MWHType type, MWHSetting setting, MWHDefinition definition, HashMap<String, Object> parameters) {
		
		if (document == null)
			return "";
		
		//	Validate for Document Type Target
		int documentTypeId = ((PO)document).get_ValueAsInt(I_C_Invoice.COLUMNNAME_C_DocTypeTarget_ID);
		if(documentTypeId <= 0) {
			documentTypeId = ((PO)document).get_ValueAsInt(I_C_DocType.COLUMNNAME_C_DocType_ID);
		}
		
		//	flush return values
		returnValues = new HashMap<String, Object>();
		processLog = new HashMap<String, String>();
		errorMessage = new StringBuffer();
		MWHDefinition.getFromDocumentType(Env.getCtx(), documentTypeId, document.getAD_Org_ID())
					 .stream()
					 .filter(whDef -> ((type!=null && type.get_ID()==whDef.getWH_Type_ID()) || type ==null)
							 			&& ((definition!=null && definition.get_ID()==whDef.get_ID()) || definition ==null))
					 .forEach(whDef ->{
						 if (setting != null
								 && setting.getWH_Type_ID() == whDef.getWH_Type_ID() 
								 	&& setting.getEventType().equals(MWHSetting.EVENTTYPE_Process))
							 processWithholding(setting, whDef, document, MWHSetting.EVENTTYPE_Process, parameters);
						 else if (setting == null)
							 processWithholding(whDef, document, MWHSetting.EVENTTYPE_Process, parameters, true);
					 });
		
		return errorMessage.toString();
	}
}
