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

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
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
	public String docValidate(PO po, int timing) {
		//	Default running for invoices
		String error = null;
		if (po.get_TableName().equals(MInvoice.Table_Name)) {
			MInvoice invoice = (MInvoice) po;
			if(!invoice.isReversal()) {
				error = WithholdingEngine.get().fireDocValidate(invoice, timing);
			}
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
							withholding.processIt(MWHWithholding.ACTION_Reverse_Correct);
							withholding.saveEx();
					});
				}
			}
		}	
		//
		return error;
	}

	@Override
	public String modelChange(PO po, int type) throws Exception {
		return null;
	}
}
