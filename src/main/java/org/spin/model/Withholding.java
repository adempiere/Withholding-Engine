/*************************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                              *
 * Copyright (C) 2012-2018 E.R.P. Consultores y Asociados, C.A.                      *
 * Contributor(s): Yamel Senih ysenih@erpya.com                                      *
 * This program is free software: you can redistribute it and/or modify              *
 * it under the terms of the GNU General Public License as published by              *
 * the Free Software Foundation, either version 3 of the License, or                 *
 * (at your option) any later version.                                               *
 * This program is distributed in the hope that it will be useful,                   *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                     *
 * GNU General Public License for more details.                                      *
 * You should have received a copy of the GNU General Public License                 *
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.            *
 ************************************************************************************/
package org.spin.model;

import java.util.List;
import java.util.stream.Collectors;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.core.domains.models.I_C_Order;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MTable;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.spin.util.WithholdingEngine;

/**
 * 	Add Default Model Validator for Withholding Engine
 * 	@author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com>
 */
public class Withholding implements ModelValidator {

	/**
	 * Constructor
	 */
	public Withholding() {
		super();
	}

	/** Logger */
	private static CLogger log = CLogger
			.getCLogger(Withholding.class);
	/** Client */
	private int clientId = -1;

	@Override
	public void initialize(ModelValidationEngine engine, MClient client) {
		// client = null for global validator
		if (client != null) {
			clientId = client.getAD_Client_ID();
			log.info(client.toString());
		} else {
			log.info("Initializing global validator: " + this.toString());
		}
		// Add Timing change only for invoice
		engine.addDocValidate(MInvoice.Table_Name, this);
		//	List it
		List<MWHSetting> settings = new Query(Env.getCtx(), MWHSetting.Table_Name, "EventType = 'E' AND AD_Client_ID =? "
				+ "AND EXISTS(SELECT 1 FROM WH_Type wt WHERE wt.WH_Type_ID = WH_Setting.WH_Type_ID AND wt.IsActive = 'Y')", null)
                .setOnlyActiveRecords(true)
                .setParameters(clientId)
                .setOrderBy(MWHSetting.COLUMNNAME_SeqNo)
                .<MWHSetting>list();
		//	for Tables
		settings
		.stream()
		.filter(setting -> setting.getEventModelValidator().startsWith("T"))
        .collect(Collectors.groupingBy(MWHSetting::getAD_Table_ID))
        .entrySet()
        .forEach(tableSet -> {
        	String tableName = MTable.getTableName(Env.getCtx(), tableSet.getKey());
            engine.addModelChange(tableName, this);
        });
		//	For documents
		settings
		.stream()
		.filter(setting -> setting.getEventModelValidator().startsWith("D"))
        .collect(Collectors.groupingBy(MWHSetting::getAD_Table_ID))
        .entrySet()
        .forEach(tableSet -> {
        	String tableName = MTable.getTableName(Env.getCtx(), tableSet.getKey());
            engine.addDocValidate(tableName, this);
        });
	}

	@Override
	public int getAD_Client_ID() {
		return clientId;
	}

	@Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		log.info("AD_User_ID=" + AD_User_ID);
		return null;
	}

	@Override
	public String docValidate(PO entity, int timing) {
		//	Default running for invoices
		String error = WithholdingEngine.get().fireDocValidate(entity, timing);
		if(!Util.isEmpty(error)) {
			throw new AdempiereException(error);
		}
		//	For table
		if (entity.get_TableName().equals(MInvoice.Table_Name)) {
			MInvoice invoice = (MInvoice) entity;
			//	For Reverse Correct
			if(timing == TIMING_BEFORE_REVERSECORRECT
					|| timing == TIMING_BEFORE_REVERSEACCRUAL
					|| timing == TIMING_BEFORE_VOID) {
				if(!invoice.isReversal()) {
					StringBuffer errorMessage = new StringBuffer();
					List<MWHWithholding> withholdingList = MWHWithholding.getWithholdingFromInvoice(invoice.getCtx(), invoice.getC_Invoice_ID(), invoice.get_TrxName());
					//	Validate
					withholdingList.stream()
						.filter(withholding -> withholding.getDocStatus().equals(MWHWithholding.STATUS_Completed) && withholding.getC_Invoice_ID() != 0)
						.forEach(withholding -> {
							if(errorMessage.length() > 0) {
								errorMessage.append(Env.NL);
							}
							errorMessage.append("@WH_Withholding_ID@ ").append(withholding.getDocumentNo()).append(" @C_Invoice_ID@ ").append(withholding.getC_Invoice().getDocumentNo());
						});
					//	Throw if exist documents
					if(errorMessage.length() > 0) {
						throw new AdempiereException("@WithholdingReferenceError@: " + errorMessage);
					}
					//	Else
					withholdingList.stream()
						.filter(withholding -> withholding.getDocStatus().equals(MWHWithholding.STATUS_Completed))
						.forEach(withholding -> {
							if(!withholding.processIt(MWHWithholding.ACTION_Void)) {
								throw new AdempiereException(withholding.getProcessMsg());
							}
							withholding.saveEx();
					});
				}
				StringBuffer errorMessage = new StringBuffer();
				List<MWHWithholding> withholdingList = new Query(Env.getCtx(), MWHWithholding.Table_Name, "C_Invoice_ID = ? AND WithholdingDeclaration_ID > 0", null)
		                .setOnlyActiveRecords(true)
		                .setParameters(invoice.getC_Invoice_ID())
		                .<MWHWithholding>list();
				withholdingList.stream()
				.forEach(withholding -> {
					MInvoice withholdingDeclaration = MInvoice.get(Env.getCtx(), withholding.getWithholdingDeclaration_ID());
					if(errorMessage.length() > 0) {
						errorMessage.append(Env.NL);
					}
					if (withholdingDeclaration.getDocStatus().equals(MInvoice.DOCSTATUS_Completed) 
							|| withholdingDeclaration.getDocStatus().equals(MInvoice.DOCSTATUS_Closed)
							|| withholdingDeclaration.getDocStatus().equals(MInvoice.DOCSTATUS_Drafted)) {
						errorMessage.append("@WH_Withholding_ID@ ").append(withholding.getDocumentNo()).append(" @C_Invoice_ID@ ").append(withholdingDeclaration.getDocumentNo());
					}						
				});					
				if(errorMessage.length() > 0) {
					throw new AdempiereException("@WithholdingReferenceError@: " + errorMessage);
				}				
			}			
		}
		//
		return null;
	}

	@Override
	public String modelChange(PO entity, int type) throws Exception {
		//	Default running for invoices
		int documentTypeId = entity.get_ValueAsInt(I_C_Order.COLUMNNAME_C_DocTypeTarget_ID);
		if(documentTypeId <= 0) {
			documentTypeId = entity.get_ValueAsInt(I_C_Order.COLUMNNAME_C_DocType_ID);
		}
		String error = WithholdingEngine.get().fireModelChange(entity, type, documentTypeId);
		if(!Util.isEmpty(error)) {
			throw new AdempiereException(error);
		}
		return null;
	}
}
