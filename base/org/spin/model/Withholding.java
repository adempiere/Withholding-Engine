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

import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
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
		}	
		//
		return error;
	}

	@Override
	public String modelChange(PO po, int type) throws Exception {
		return null;
	}
}
