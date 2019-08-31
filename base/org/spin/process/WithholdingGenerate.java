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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.spin.model.MWHDefinition;
import org.spin.model.MWHSetting;
import org.spin.model.MWHWithholding;

/**
 * Withholding Generate process
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 * @contributor Carlos Parada, cParada@erpya.com, http://www.erpya.com
 */
public class WithholdingGenerate extends WithholdingGenerateAbstract {

	private ArrayList<Withholding> withholdingDocList = new ArrayList<Withholding> ();
	protected void prepare() {
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception {
		
		if (isSelection()) {
			getSelectionValues()
				.entrySet()
				.stream()
				.forEach(list -> {
					MWHWithholding withholding = new MWHWithholding(getCtx(), list.getKey(), get_TrxName());
					generateWHDoc(withholding);
				});
			processWHDoc();
		}else {
			StringBuffer whereClause = new StringBuffer();
			ArrayList<Object> params = new ArrayList<Object>();
			
			whereClause.append("AD_Client_ID = ? ");
			params.add(getAD_Client_ID());
			
			whereClause.append("AND DocStatus IN (?,?) ");
			params.add(MWHWithholding.DOCSTATUS_Completed);
			params.add(MWHWithholding.DOCSTATUS_Closed);
			
			if (getParameterAsInt("AD_Org_ID") > 0) {
				whereClause.append(" AND AD_Org_ID = ? ");
				params.add(getParameterAsInt("AD_Org_ID"));
			}
			
			if (getParameterAsInt("C_BPartner_ID") > 0) {
				whereClause.append(" AND C_BPartner_ID = ? ");
				params.add(getParameterAsInt("C_BPartner_ID"));
			}
			
			if (getParameterAsInt("C_Invoice_ID") > 0) {
				whereClause.append(" AND SourceInvoice_ID = ? ");
				params.add(getParameterAsInt("C_Invoice_ID"));
			}
			
			if (getParameterAsInt("WH_Type_ID") > 0) {
				whereClause.append(" AND EXISTS (SELECT 1 FROM WH_Definition whd WHERE whd.WH_Definition_ID = WH_Withholding.WH_Definition_ID AND whd.WH_Type_ID = ?) ");
				params.add(getParameterAsInt("WH_Type_ID"));
			}
			
			new Query(getCtx(), MWHWithholding.Table_Name, whereClause.toString(), get_TrxName())
					.setParameters(params)
					.list()
					.forEach( withholding -> {
						generateWHDoc((MWHWithholding) withholding);
					});
			processWHDoc();
		}
		
		return "@OK@";
	}
	
	/**
	 * Generate Withholding Document
	 * @param withholding
	 */
	private void generateWHDoc(MWHWithholding withholding) {
		
		if (!withholding.isProcessed()
				|| withholding.get_ValueAsBoolean("IsSimulation"))
			return ;
		
		if (withholding.getC_Invoice_ID() > 0) {
			MInvoice whDoc = (MInvoice) withholding.getC_Invoice();
			if (whDoc.getDocStatus().equals(MInvoice.DOCSTATUS_Completed)
					|| whDoc.getDocStatus().equals(MInvoice.DOCSTATUS_Closed)
						|| whDoc.getDocStatus().equals(MInvoice.DOCSTATUS_InProgress)
							|| whDoc.getDocStatus().equals(MInvoice.DOCSTATUS_Invalid)) {
				addLog("@DocumentNo@ : " + whDoc.getDocumentNo() + " | @IsGenerated@ | @DocStatus@ : " + whDoc.getDocStatusName());
				return;
			}
		}
		MInvoice invoiceTo = null;
		MInvoiceLine invoiceLineTo = null;
		
		
		AtomicReference<Integer> Curr_WH_Definition_ID = new AtomicReference<Integer>();
		AtomicReference<Integer> Curr_WH_Setting_ID = new AtomicReference<Integer>();
		AtomicReference<Integer> Curr_C_BPartner_ID = new AtomicReference<Integer>();
		
		
		if (withholding.get_ID() > 0 ) {
			Curr_WH_Definition_ID.set(withholding.getWH_Definition_ID());
			Curr_WH_Setting_ID.set(withholding.getWH_Setting_ID());
			Curr_C_BPartner_ID.set(withholding.getC_BPartner_ID());
			MWHDefinition whDefinition = (MWHDefinition)withholding.getWH_Definition();
			MWHSetting whSetting = (MWHSetting)withholding.getWH_Setting();
			MInvoice invoiceFrom = (MInvoice) withholding.getSourceInvoice();
			
			Withholding withholldingDoc = withholdingDocList.stream()
															.filter(wh ->(wh.getC_BPartner_ID()==Curr_C_BPartner_ID.get()
																					&& wh.getWH_Definition_ID()==Curr_WH_Definition_ID.get() 
																						&& wh.getWH_Setting_ID() == Curr_WH_Setting_ID.get()))
															.findFirst()
															.orElse(null);
			
			if (withholldingDoc==null) {
				
				
				withholldingDoc = new Withholding(withholding.getWH_Definition_ID(), withholding.getWH_Setting_ID(), withholding.getC_BPartner_ID(), this);
				
				
				invoiceTo = new MInvoice(getCtx(), 0, get_TrxName());
				invoiceTo.setAD_Org_ID(invoiceFrom.getAD_Org_ID());
				invoiceTo.setC_BPartner_ID(invoiceFrom.getC_BPartner_ID());
				invoiceTo.setC_BPartner_Location_ID(invoiceFrom.getC_BPartner_Location_ID());
				invoiceTo.setM_PriceList_ID(invoiceFrom.getM_PriceList_ID());
				invoiceTo.setIsSOTrx(invoiceFrom.isSOTrx());
				invoiceTo.setDateInvoiced(getParameterAsTimestamp("DateDoc"));
				invoiceTo.setDateAcct(getParameterAsTimestamp("DateDoc"));
				int C_DocType_ID =  withholding.getWHDocType();
				if (C_DocType_ID > 0)
					invoiceTo.setC_DocTypeTarget_ID(C_DocType_ID);
				else
					throw new AdempiereException("@NotFound@ @WithholdingDebitDocType_ID@");
				
				invoiceTo.saveEx();
				
				
				//	Get Document No
				int docNo = Integer.parseInt(invoiceTo.getDocumentNo());
				//	Format Date
				String format = "yyyyMM";
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				String prefix = sdf.format(invoiceTo.getDateInvoiced().getTime());
				if(prefix == null)
					prefix = "";
				//	Set New Document No
				invoiceTo.setDocumentNo(prefix + String.format("%1$" + 8 + "s", docNo).replace(" ", "0"));
				invoiceTo.save();
				
			}
			
			if (invoiceTo==null)
				invoiceTo = withholldingDoc.getInvoice();
			
			invoiceLineTo = new MInvoiceLine(invoiceTo);
			if (whSetting.getC_Charge_ID()> 0)
				invoiceLineTo.setC_Charge_ID(whSetting.getC_Charge_ID());
			else if (whDefinition.getC_Charge_ID()> 0)
				invoiceLineTo.setC_Charge_ID(whDefinition.getC_Charge_ID());
			else 
				new AdempiereException("@NotFound@ @C_Charge_ID@");
			
			invoiceLineTo.set_ValueOfColumn("InvoiceToAllocate_ID", invoiceFrom.getC_Invoice_ID());
			invoiceLineTo.setQty(Env.ONE);
			invoiceLineTo.setPrice(withholding.getWithholdingAmt());
			invoiceLineTo.saveEx();
			
			withholding.setC_Invoice_ID(invoiceLineTo.getC_Invoice_ID());
			withholding.setC_InvoiceLine_ID(invoiceLineTo.getC_InvoiceLine_ID());
			withholding.saveEx();
			
			if (withholldingDoc.getInvoice()==null)
				withholldingDoc.setInvoice(invoiceTo);
			
			withholldingDoc.addWithHolding(withholding);
			withholdingDocList.add(withholldingDoc);
		}
	}
	/**
	 * Process Document
	 */
	private void processWHDoc() {
		withholdingDocList.stream().forEach( withholding -> {
			withholding.process();
		});
	}
}

/**
 * 
 * @author Carlos Parada, cParada@erpya.com, http://www.erpya.com
 *
 */
class Withholding{
	private int WH_Definition_ID = 0;
	private int WH_Setting_ID = 0;
	private int C_BPartner_ID = 0;
	private MInvoice invoice = null;
	private MInvoiceLine invoiceLine = null;
	private ArrayList<MWHWithholding> withholding = new ArrayList<MWHWithholding>();
	private SvrProcess process = null;
	
	/**
	 * Constructor
	 * @param WH_Definition_ID
	 * @param WH_Setting_ID
	 * @param C_BPartner_ID
	 * @param process
	 */
	public Withholding(int WH_Definition_ID, int WH_Setting_ID, int C_BPartner_ID, SvrProcess process) {
		this.WH_Definition_ID = WH_Definition_ID;
		this.WH_Setting_ID = WH_Setting_ID;
		this.C_BPartner_ID = C_BPartner_ID;
		this.process = process;
	}
	
	/**
	 * Get Withholpding Definition
	 * @return
	 */
	public int getWH_Definition_ID() {
		return WH_Definition_ID;
	}
	
	/**
	 * Get Withholding Setting 
	 * @return
	 */
	public int getWH_Setting_ID() {
		return WH_Setting_ID;
	}
	
	/**
	 * Get Business Partner
	 * @return
	 */
	public int getC_BPartner_ID() {
		return C_BPartner_ID;
	}
	
	/**
	 * Get Invoice
	 * @return
	 */
	public MInvoice getInvoice() {
		return invoice;
	}
	
	/**
	 * Get WithHolding
	 * @return
	 */
	public ArrayList<MWHWithholding> getWithholding() {
		return withholding;
	}
	
	/**
	 * Set Invoice
	 * @param invoice
	 */
	public void setInvoice(MInvoice invoice) {
		this.invoice = invoice;
	}
	
	/**
	 * Get Invoice Line
	 * @return
	 */
	public MInvoiceLine getInvoiceLine() {
		return invoiceLine;
	}
	
	/**
	 * Set Invoice
	 * @param invoiceLine
	 */
	public void setInvoiceLine(MInvoiceLine invoiceLine) {
		this.invoiceLine = invoiceLine;
	}
	
	/**
	 * Add Withholding
	 * @param withholding
	 */
	public void addWithHolding(MWHWithholding withholding) {
		this.withholding.add(withholding);
	}
	
	/**
	 * Process Document
	 */
	public void process() {
		if (invoice!=null
				&& !invoice.isProcessed()) {
			invoice.processIt(MInvoice.DOCACTION_Complete);
			invoice.saveEx();
			if (process!=null)
				process.addLog("@DocumentNo@ : " + invoice.getDocumentNo());
		}
	}
	
}