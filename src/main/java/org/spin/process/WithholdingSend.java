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
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.ServerReportCtl;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.spin.queue.notification.DefaultNotifier;
import org.spin.queue.util.QueueLoader;


/** Generated Process for (Withholding Send)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.3
 */
public class WithholdingSend extends WithholdingSendAbstract {

	private AtomicInteger sends = new AtomicInteger();
	private boolean sendToQueue = false;
	
	@Override
	protected String doIt() throws Exception {
		if (getMailTextId() == 0)
			throw new AdempiereException("@Invalid@ @R_MailTex_ID@");
		
		sendToQueue = isSendDocumentToQueue();
		
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
		AtomicReference<MUser> recipient = new AtomicReference<MUser>();
		MUser sender = MUser.get(getCtx(), getAD_User_ID());
		
		if (invoice.getAD_User_ID() > 0)
			recipient.set((MUser) invoice.getAD_User());
		
        //	Get from default account
        if (recipient.get() == null) {
        	Optional<MUser> maybeUser = Arrays.asList(MUser.getOfBPartner(getCtx(), invoice.getC_BPartner_ID(), get_TrxName()))
        		.stream()
        		.filter(user -> !Util.isEmpty(user.getNotificationType()) && (user.getNotificationType().equals(MUser.NOTIFICATIONTYPE_EMail) 
        				|| user.getNotificationType().equals(MUser.NOTIFICATIONTYPE_EMailPlusNotice)))
        		.filter(user -> !Util.isEmpty(user.getEMail()) && user.getC_BPartner_Location_ID() == invoice.getC_BPartner_Location_ID())
        		.findFirst();
        	if(maybeUser.isPresent()) {
        		recipient.set(maybeUser.get());
        	} else {
        		maybeUser = Arrays.asList(MUser.getOfBPartner(getCtx(), invoice.getC_BPartner_ID(), get_TrxName()))
                		.stream()
                		.filter(user -> !Util.isEmpty(user.getNotificationType()) && (user.getNotificationType().equals(MUser.NOTIFICATIONTYPE_EMail) 
                				|| user.getNotificationType().equals(MUser.NOTIFICATIONTYPE_EMailPlusNotice)))
                		.filter(user -> !Util.isEmpty(user.getEMail()))
                		.findFirst();
                if(maybeUser.isPresent()) {
                	recipient.set(maybeUser.get());
                }
        	}
        }
        File attachment = getPDF(invoice);
    	if (attachment == null)
			return;
    	
        if (!sendToQueue) {
	        //	
	        Optional.ofNullable(recipient.get()).ifPresent(toUser -> {
	    		//	
	        	MClient client = MClient.get(getCtx(), getAD_Client_ID());
	        	MMailText template = getTemplate(invoice, mailTextId);
				if (client.sendEMail(sender, recipient.get(), template.getMailHeader(), template.getMailText(true), attachment,template.isHtml())) { 
					addLog("@EMail@ @Sent@ @to@ " + recipient.get().getName());
					sends.incrementAndGet();
				}
	        });
	        //	Other
	        if(!Optional.ofNullable(recipient.get()).isPresent()) {
	        	addLog("@NotFound@ @AD_User_ID@ -> @C_BPartner_ID@ " + invoice.getC_BPartner().getName() + " @DocumentNo@ " + invoice.getDocumentNo());
	        }
        }else {
			MMailText template = getTemplate(invoice, mailTextId);
			//	Get instance for notifier
			DefaultNotifier notifier = (DefaultNotifier) QueueLoader.getInstance().getQueueManager(DefaultNotifier.QUEUETYPE_DefaultNotifier)
					.withContext(getCtx())
					.withTransactionName(get_TrxName());
			//	Send notification to queue
			notifier
				.clearMessage()
				.withApplicationType(DefaultNotifier.DefaultNotificationType_UserDefined)
				.withUserId(sender.get_ID())
				.withText(template.getMailText(true))
				.withDescription(template.getMailHeader())
				.withEntity(invoice);
			
			if (recipient.get() != null) 
				notifier.addRecipient(recipient.get().getAD_User_ID());
			else {
				addLog(invoice.getC_Invoice_ID(), null, null, "@RequestActionEMailNoTo@");
				return;
			}
			//	Attachment
			notifier.addAttachment(attachment);
			//	Add to queue
			notifier.addToQueue();
			addLog (invoice.getC_Invoice_ID(), null, null, "@MessageAddedToQueue@");
			//	
			sends.incrementAndGet();
        }
	}
	
	/**
	 * Get Mail Template 
	 * @param invoice
	 * @param mailTextId
	 * @return
	 * @return MMailText
	 */
	private MMailText getTemplate (MInvoice invoice, int mailTextId) {
		MMailText template = new MMailText(getCtx(), mailTextId, get_TrxName());
		template.setPO(invoice);
		template.setUser(getAD_User_ID());
		template.setBPartner(invoice.getC_BPartner_ID());
		
		return template;
	}
	
	/**
	 * Get PDF from Print Format 
	 * @param invoice
	 * @return
	 * @return File
	 */
	private File getPDF(MInvoice invoice) {
		ReportEngine reportEngine = ReportEngine.get(getCtx(), ReportEngine.INVOICE, invoice.get_ID());
		MPrintFormat printFormat = reportEngine.getPrintFormat();
    	File attachment = null;
    	if (printFormat.getJasperProcess_ID() > 0) {
    		if (ServerReportCtl.runJasperProcess(invoice.get_ID(), reportEngine, true, null, getProcessInfo())) {
    			attachment = getProcessInfo().getPDFReport();
    		}
    	}else 
    		attachment = reportEngine.getPDF();
    	
		//	
		if(attachment == null) {
			addLog(invoice.getC_Invoice_ID(), null, null, "@FilePDF@ @NotFound@");
			return null;
		}
		
		return attachment;
	}
}