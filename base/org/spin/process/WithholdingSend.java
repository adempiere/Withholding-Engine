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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MMailText;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.print.ReportEngine;
import org.compiere.util.Env;


/** Generated Process for (Withholding Send)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.3
 */
public class WithholdingSend extends WithholdingSendAbstract
{
	@Override
	protected void prepare()
	{
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception
	{
		AtomicReference<Integer> result = new AtomicReference<Integer>();
		result.set(0);
		
		if (getMailTextId()==0)
			throw new AdempiereException("@Invalid@ @R_MailTex_ID@");
		
		if (!isSelection()) {
			List<Object> params = new ArrayList<>();
			String whereClause = "DocStatus IN (?,?) ";
			params.add(MInvoice.DOCSTATUS_Completed);
			params.add(MInvoice.DOCSTATUS_Closed);

			whereClause += "AND EXISTS(SELECT 1 FROM "
									+ "WH_Withholding wh "
									+ "INNER JOIN WH_Setting whs ON (wh.WH_Setting_ID = whs.WH_Setting_ID) "
									+ "WHERE wh.C_Invoice_ID = C_Invoice.C_Invoice_ID ";
			
			if (getTypeId()!=0) {
				whereClause += "AND whs.WH_Type_ID = ? ";
				params.add(getTypeId());
			}
			
			whereClause += ") ";
			
			if (getBPartnerId()!=0) {
				whereClause += "AND C_BPartner_ID = ? ";
				params.add(getBPartnerId());
			}
			
			if (!getIsSOTrx().equals("")) {
				whereClause += "AND IsSOTrx = ? ";
				params.add(getIsSOTrx());
			}
			
			whereClause += "AND DateInvoiced >= ? ";
			if (getDateDoc()!=null) 
				params.add(getDateDoc());
			else
				params.add(Env.getContextAsDate(getCtx(), "@#Date@"));
			
			whereClause += "AND DateInvoiced <= ? ";
			if (getDateDocTo()!=null) 
				params.add(getDateDocTo());
			else
				params.add(Env.getContextAsDate(getCtx(), "@#Date@"));
			
			
			
			new Query(getCtx(), MInvoice.Table_Name, whereClause, get_TrxName())
					.setParameters(params)
					.list().forEach(invoice ->{
						result.set(result.get() + sendInvoiceEmail(invoice.get_ID(), getMailTextId()) );
				});
		}
		else 
			getSelectionKeys().forEach(invoiceID ->{
				result.set(result.get() + sendInvoiceEmail(invoiceID, getMailTextId()));
			});
		
			
		return "@EMail@ @Sent@ " +result.get();
	}
	/**
	 * Send Withholding Document
	 * @param Record_ID
	 * @param MailText_ID
	 * @return
	 */
	private int sendInvoiceEmail(int Record_ID, int MailText_ID) {
		MInvoice invoice = new MInvoice(getCtx(), Record_ID, get_TrxName());
		
		MUser to = (MUser)invoice.getAD_User();
		
		if (to == null
				|| (to!= null && to.get_ID()==0)) 
			to =Arrays.asList(MUser.getOfBPartner(getCtx(), invoice.getC_BPartner_ID(), get_TrxName()))
					.stream().filter(user -> user.getC_BPartner_Location_ID() == invoice.getC_BPartner_Location_ID())
					.findFirst()
					.orElse(null);
		if (to!= null 
				&& to.get_ID()!=0) {
			ReportEngine re = ReportEngine.get(getCtx(), ReportEngine.INVOICE, invoice.get_ID());
			File attachment = re.getPDF();
			MClient client = MClient.get(getCtx(), getAD_Client_ID());
			MMailText template = new MMailText(getCtx(), MailText_ID, get_TrxName());
			template.setPO(invoice);
			template.setBPartner(invoice.getC_BPartner_ID());
			template.setUser(getAD_User_ID());
			
			if (client.sendEMail(to.getEMail(), template.getMailHeader(), template.getMailText(true), attachment,template.isHtml())) { 
				addLog("@EMail@ @Sent@ @to@ " + to.getName());
				return 1;
			}else
				return 0;
			
		}else {
			addLog("@NotFound@ @AD_User_ID@ -> @C_BPartner_ID@ " + invoice.getC_BPartner().getName() + " @DocumentNo@ " + invoice.getDocumentNo());
			return 0;
		}
		
	}
}