/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.										*
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/

package org.spin.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.Env;
import org.spin.model.MWHSetting;
import org.spin.model.MWHWithholding;
import org.spin.model.X_WH_Withholding;
import org.syntax.jedit.InputHandler.end;

/**
 * Withholding Generate process
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class WithholdingGenerate extends WithholdingGenerateAbstract {

	protected void prepare() {
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception {
		HashMap<Integer,BigDecimal> withHoldingByBPartner = new HashMap<Integer, BigDecimal>();
		HashMap<Integer,Integer> withHoldingBySourceInvoice = new HashMap<Integer, Integer>();
		BigDecimal totalWithholdingAmt = BigDecimal.ZERO;
//		Create New Invoice
		MInvoice invoice = new MInvoice(Env.getCtx(), 0, get_TrxName());
		int chargeID = 0;
		int bpartnerId = 0;
//		Get Selections
		for(int key : getSelectionKeys()) {			
			
			BigDecimal withholdingAmt = getSelectionAsBigDecimal(key, "WH_WithholdingAmt");
			int withHoldingID = getSelectionAsInt(key, "WH_WH_Withholding_ID");
			int sourceInvoiceId = getSelectionAsInt(key, "WH_SourceInvoice_ID");
			int withholdingDefinitionId = getSelectionAsInt(key, "WH_WH_Definition_ID");
			int withholdingTypeId = getSelectionAsInt(key, "WHD_WH_Type_ID");
			bpartnerId = getSelectionAsInt(key, "WH_C_BPartner_ID");
			
//			Store WitholdingID and Witholding and Amt
			withHoldingByBPartner.put(withHoldingID,withholdingAmt);
			withHoldingBySourceInvoice.put(withHoldingID,sourceInvoiceId);
			MWHWithholding  withHolding = new MWHWithholding(getCtx(), withHoldingID, get_TrxName());
			int withHoldingSettingID = withHolding.get_ValueAsInt("WH_Setting_ID");
			MWHSetting withholdingSetting = new MWHSetting(Env.getCtx(), withHoldingSettingID, get_TrxName());
// 			Obtain Charge And Document type
			
			chargeID = withholdingSetting.getC_Charge_ID();
			invoice.setC_DocTypeTarget_ID(withholdingSetting.getC_DocType_ID());	

		}
		invoice.setC_BPartner_ID(bpartnerId);
		invoice.setIsSOTrx(false);
//		Calculates Total Amount
		totalWithholdingAmt = withHoldingByBPartner.values().stream().reduce(BigDecimal.ZERO, (p, q) -> p.add(q));
		
		invoice.setGrandTotal(totalWithholdingAmt);	
		invoice.save();
//		Create Invoice Line
		int invoiceID = invoice.getC_Invoice_ID();
		//		Creates Lines for Invoice
		int qty = 1;
		for (Entry<Integer,BigDecimal> withHoldingmap : withHoldingByBPartner.entrySet()) {
			int withHoldingAffected = withHoldingmap.getKey();
			//			Set Amt from Witholding on invoices
			MInvoiceLine invoiceLine = new MInvoiceLine(invoice);
		    if (withHoldingBySourceInvoice.containsKey(withHoldingAffected)) {
		    	invoiceLine.set_ValueOfColumn("InvoiceToAllocate_ID",withHoldingBySourceInvoice.get(withHoldingAffected) );
		    }
			invoiceLine.setC_Charge_ID(chargeID);
			invoiceLine.setPriceEntered(withHoldingmap.getValue());
			invoiceLine.setPriceActual(withHoldingmap.getValue());
			invoiceLine.setQty(qty);
			invoiceLine.save();				
//			Set References from generated invoices on Witholding
			MWHWithholding  withHolding = new MWHWithholding(getCtx(), withHoldingAffected, get_TrxName());
			withHolding.setC_Invoice_ID(invoiceID);
			withHolding.save();

		}	


		return "";
	}
}