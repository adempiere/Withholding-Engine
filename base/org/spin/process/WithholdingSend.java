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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MMailText;
import org.compiere.model.MUser;
import org.compiere.model.Query;
import org.compiere.print.ReportEngine;
import org.compiere.util.Env;
import org.compiere.util.Util;


/** Generated Process for (Withholding Send)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.3
 */
public class WithholdingSend extends WithholdingSendAbstract {

	private AtomicInteger sends = new AtomicInteger();
	
	@Override
	protected String doIt() throws Exception {
		if (getMailTextId() == 0)
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
			//	
			new Query(getCtx(), MInvoice.Table_Name, whereClause, get_TrxName())
					.setParameters(params)
					.list().forEach(invoice -> sendInvoiceEmail(invoice.get_ID(), getMailTextId()));
		} else {
			getSelectionKeys().forEach(invoiceID -> sendInvoiceEmail(invoiceID, getMailTextId()));
		}
		//	
		return "@EMail@ @Sent@ " + sends.get();
	}
	/**
	 * Send Withholding Document
	 * @param recordId
	 * @param mailTextId
	 * @return
	 */
	private void sendInvoiceEmail(int recordId, int mailTextId) {
		MInvoice invoice = new MInvoice(getCtx(), recordId, get_TrxName());
		AtomicReference<MUser> to = new AtomicReference<MUser>((MUser) invoice.getAD_User());
		MUser from = MUser.get(getCtx(), getAD_User_ID());
        //	Get from default account
        if (to.get() == null) {
        	Optional<MUser> maybeUser = Arrays.asList(MUser.getOfBPartner(getCtx(), invoice.getC_BPartner_ID(), get_TrxName()))
        		.stream()
        		.filter(user -> !Util.isEmpty(user.getNotificationType()) && (user.getNotificationType().equals(MUser.NOTIFICATIONTYPE_EMail) 
        				|| user.getNotificationType().equals(MUser.NOTIFICATIONTYPE_EMailPlusNotice)))
        		.filter(user -> !Util.isEmpty(user.getEMail()) && user.getC_BPartner_Location_ID() == invoice.getC_BPartner_Location_ID())
        		.findFirst();
        	if(maybeUser.isPresent()) {
        		to.set(maybeUser.get());
        	} else {
        		maybeUser = Arrays.asList(MUser.getOfBPartner(getCtx(), invoice.getC_BPartner_ID(), get_TrxName()))
                		.stream()
                		.filter(user -> !Util.isEmpty(user.getNotificationType()) && (user.getNotificationType().equals(MUser.NOTIFICATIONTYPE_EMail) 
                				|| user.getNotificationType().equals(MUser.NOTIFICATIONTYPE_EMailPlusNotice)))
                		.filter(user -> !Util.isEmpty(user.getEMail()))
                		.findFirst();
                if(maybeUser.isPresent()) {
                	to.set(maybeUser.get());
                }
        	}
        }
        //	
        Optional.ofNullable(to.get()).ifPresent(toUser -> {
        	ReportEngine re = ReportEngine.get(getCtx(), ReportEngine.INVOICE, invoice.get_ID());
			File attachment = re.getPDF();
			MClient client = MClient.get(getCtx(), getAD_Client_ID());
			MMailText template = new MMailText(getCtx(), mailTextId, get_TrxName());
			template.setPO(invoice);
			template.setBPartner(invoice.getC_BPartner_ID());
			template.setUser(getAD_User_ID());
			
			if (client.sendEMail(from, to.get(), template.getMailHeader(), template.getMailText(true), attachment,template.isHtml())) { 
				addLog("@EMail@ @Sent@ @to@ " + to.get().getName());
				sends.incrementAndGet();
			}
        });
        //	Other
        if(!Optional.ofNullable(to.get()).isPresent()) {
        	addLog("@NotFound@ @AD_User_ID@ -> @C_BPartner_ID@ " + invoice.getC_BPartner().getName() + " @DocumentNo@ " + invoice.getDocumentNo());
        }
	}
}