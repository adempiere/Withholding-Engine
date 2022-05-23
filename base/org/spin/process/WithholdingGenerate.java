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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCurrency;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MPriceList;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.spin.model.MWHDefinition;
import org.spin.model.MWHSetting;
import org.spin.model.MWHWithholding;

/**
 * Withholding Generate process
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 * @contributor Carlos Parada, cParada@erpya.com, http://www.erpya.com
 */
public class WithholdingGenerate extends WithholdingGenerateAbstract {
	
	/**View Column Currency*/
	private final static String COLUMN_C_Currency_ID = "WH_C_Currency_ID";
	/**View Column Currency To*/
	private final static String COLUMN_C_Currency_ID_To = "CTO_C_Currency_ID_To";
	/**View Column Converted Withholding Amount*/
	private final static String COLUMN_Converted_WithholdingAmt = "WH_Converted_WithholdingAmt";

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
					BigDecimal manualWithholdingAmt = Env.ZERO;
					setCurrencyId(Optional.ofNullable((Integer)list.getValue().get(COLUMN_C_Currency_ID)).orElse(Env.ZERO.intValue()));
					setCurrencyToId(Optional.ofNullable((Integer)list.getValue().get(COLUMN_C_Currency_ID_To)).orElse(Env.ZERO.intValue()));
					if (withholding.isManual()) 
						manualWithholdingAmt = Optional.ofNullable((BigDecimal)list.getValue().get(COLUMN_Converted_WithholdingAmt)).orElse(Env.ZERO);
					

					generateWHDoc(withholding, manualWithholdingAmt);
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
			
			if (getOrgId() > 0) {
				whereClause.append(" AND AD_Org_ID = ? ");
				params.add(getOrgId());
			}
			
			if (getBPartnerId() > 0) {
				whereClause.append(" AND C_BPartner_ID = ? ");
				params.add(getBPartnerId());
			}
			
			if (getInvoiceId() > 0) {
				whereClause.append(" AND SourceInvoice_ID = ? ");
				params.add(getInvoiceId());
			}
			
			if (getTypeId() > 0) {
				whereClause.append(" AND EXISTS (SELECT 1 FROM WH_Definition whd WHERE whd.WH_Definition_ID = WH_Withholding.WH_Definition_ID AND whd.WH_Type_ID = ?) ");
				params.add(getTypeId());
			}
			
			if (getCurrencyId() > 0) {
				whereClause.append(" AND EXISTS (SELECT 1 FROM C_Invoice i WHERE i.C_Invoice_ID = WH_Withholding.SourceInvoice_ID AND i.C_Currency_ID = ?) ");
				params.add(getCurrencyId());
			}
			
			new Query(getCtx(), MWHWithholding.Table_Name, whereClause.toString(), get_TrxName())
					.setParameters(params)
					.list()
					.forEach( withholding -> {
						generateWHDoc((MWHWithholding) withholding, Env.ZERO);
					});
			processWHDoc();
		}
		
		return "@OK@";
	}
	
	/**
	 * Generate Withholding Document
	 * @param withholding
	 */
	private void generateWHDoc(MWHWithholding withholding, BigDecimal manualWithholdingAmt) {
		
		if (!withholding.isProcessed()
				|| withholding.isSimulation())
			return ;
		
		if (getCurrencyId()==0)
			throw new AdempiereException("@NotFound@ @C_Currency_ID@");
		
		if (getCurrencyToId()==0)
			throw new AdempiereException("@NotFound@ @C_Currency_ID_To@");
		
		if (isManual()) {
			if (getBPartnerId()==0)
				throw new AdempiereException("@NotFound@ @C_BPartner_ID@");
			
			if (Util.isEmpty(getDocumentNo()))
				throw new AdempiereException("@IsEmpty@ @DocumentNo@");
		}
		
		if (withholding.getC_Invoice_ID() > 0) {
			MInvoice whDoc = MInvoice.get(getCtx(), withholding.getC_Invoice_ID());
			if (whDoc!=null &&
					(whDoc.getDocStatus().equals(MInvoice.DOCSTATUS_Completed)
						|| whDoc.getDocStatus().equals(MInvoice.DOCSTATUS_Closed)
							|| whDoc.getDocStatus().equals(MInvoice.DOCSTATUS_InProgress)
								|| whDoc.getDocStatus().equals(MInvoice.DOCSTATUS_Invalid))) {
				addLog("@DocumentNo@ : " + whDoc.getDocumentNo() + " | @IsGenerated@ | @DocStatus@ : " + whDoc.getDocStatusName());
				return;
			}
		}
		AtomicReference<Optional<MInvoice>> invoiceTo = new AtomicReference<>();
		AtomicReference<Optional<MInvoiceLine>> invoiceLineTo = new AtomicReference<>();
		
		
		AtomicReference<Integer> Curr_WH_Definition_ID = new AtomicReference<Integer>();
		AtomicReference<Integer> Curr_WH_Setting_ID = new AtomicReference<Integer>();
		AtomicReference<Integer> Curr_C_BPartner_ID = new AtomicReference<Integer>();
		AtomicReference<Integer> Curr_C_DocType_ID = new AtomicReference<Integer>();
		AtomicReference<Integer> Curr_C_ConversionType_ID = new AtomicReference<Integer>();
		
		if (withholding.get_ID() > 0 ) {
			Curr_WH_Definition_ID.set(withholding.getWH_Definition_ID());
			Curr_WH_Setting_ID.set(withholding.getWH_Setting_ID());
			Curr_C_BPartner_ID.set(withholding.getC_BPartner_ID());
			Curr_C_DocType_ID.set(withholding.getWHDocType());
			Curr_C_ConversionType_ID.set(withholding.getC_ConversionType_ID());
			invoiceTo.set(Optional.empty());
			invoiceLineTo.set(Optional.empty());
			MWHDefinition whDefinition = (MWHDefinition)withholding.getWH_Definition();
			MWHSetting whSetting = (MWHSetting)withholding.getWH_Setting();
			MInvoice invoiceFrom = MInvoice.get(getCtx(), withholding.getSourceInvoice_ID());
			Optional<Withholding> withholldingDoc  = Optional.empty();
			
			withholldingDoc = withholdingDocList.stream()
								.filter(wh ->(wh.getC_BPartner_ID()==Curr_C_BPartner_ID.get()
												&& wh.getWH_Definition_ID()==Curr_WH_Definition_ID.get() 
													&& wh.getWH_Setting_ID() == Curr_WH_Setting_ID.get())
														&& wh.getC_DocType_ID() == Curr_C_DocType_ID.get()
															&& wh.getC_ConversionType_ID() == Curr_C_ConversionType_ID.get())
								.findFirst();

			if (!withholldingDoc.isPresent()) 
				withholldingDoc = Optional.ofNullable(new Withholding(withholding.getWH_Definition_ID(), 
																		withholding.getWH_Setting_ID(), 
																		withholding.getC_BPartner_ID(), 
																		withholding.getWHDocType(), 
																		withholding.getC_ConversionType_ID(), 
																		this));
			
			withholldingDoc.ifPresent(whDocument->{
				if (!whDocument.getInvoice().isPresent()) {
					MInvoice invoice = new MInvoice(getCtx(), 0, get_TrxName());
					invoiceTo.set(Optional.ofNullable(invoice));
					whDocument.setInvoice(invoice);
				}else
					invoiceTo.set(whDocument.getInvoice());
				
				invoiceTo.get().ifPresent(invoice ->{
					
					AtomicReference<BigDecimal> withholdingAmt = new AtomicReference<BigDecimal>(Env.ZERO);
					
					if (invoice.get_ID()==0) {
						if (withholding.isManual())
							invoice.setDocumentNo(getDocumentNo());
						
						invoice.setAD_Org_ID(withholding.getAD_Org_ID());
						invoice.setC_BPartner_ID(withholding.getC_BPartner_ID());
						invoice.setC_BPartner_Location_ID(withholding.getC_BPartner_Location_ID());
						invoice.setIsSOTrx(withholding.isSOTrx());
						invoice.setDateInvoiced(getDateDoc());
						invoice.setDateAcct(getDateDoc());
						invoice.setC_ConversionType_ID(withholding.getC_ConversionType_ID());
						invoice.setM_PriceList_ID(invoiceFrom.getM_PriceList_ID());
						Optional<MPriceList> maybePriceList = Optional
																.ofNullable(MPriceList.getDefault(getCtx(), 
																	  					invoice.isSOTrx(), 
																	  					MCurrency.get(getCtx(), getCurrencyToId()).getISO_Code()));
						maybePriceList.ifPresent(priceList -> invoice.setM_PriceList_ID(priceList.getM_PriceList_ID()));
						
						int C_DocType_ID =  Curr_C_DocType_ID.get();
						if (C_DocType_ID > 0)
							invoice.setC_DocTypeTarget_ID(C_DocType_ID);
						else
							throw new AdempiereException("@NotFound@ @WithholdingDebitDocType_ID@");
						
						invoice.setC_DocType_ID(invoice.getC_DocTypeTarget_ID());
						invoice.saveEx();
					}
					
					if (withholding.isManual()
							&& Optional.ofNullable(manualWithholdingAmt)
									   .orElse(Env.ZERO).compareTo(Env.ZERO) != 0)
						withholdingAmt.set(manualWithholdingAmt);
					else {
						if (getCurrencyId() != getCurrencyToId()) {
							withholdingAmt.set(Optional.ofNullable(MConversionRate.convert(invoice.getCtx(), 
									Optional.ofNullable(withholding.getWithholdingAmt()).orElse(Env.ZERO), 
									getCurrencyId(), 
									getCurrencyToId(), 
									withholding.getDateAcct(), 
									withholding.getC_ConversionType_ID(), 
									withholding.getAD_Client_ID(), 
									withholding.getAD_Org_ID())).orElse(Env.ZERO));
						}else 
							withholdingAmt.set(withholding.getWithholdingAmt());
					}

					invoiceLineTo.set(Optional.ofNullable(new MInvoiceLine(invoice)));
					invoiceLineTo.get().ifPresent(invoiceLine ->{
						if (whSetting.getC_Charge_ID()> 0)
							invoiceLine.setC_Charge_ID(whSetting.getC_Charge_ID());
						else if (whDefinition.getC_Charge_ID()> 0)
							invoiceLine.setC_Charge_ID(whDefinition.getC_Charge_ID());
						else 
							new AdempiereException("@NotFound@ @C_Charge_ID@");
						
						invoiceLine.setQty(Env.ONE);
						invoiceLine.setPrice(Optional.ofNullable(withholdingAmt.get()).orElse(Env.ZERO));
						invoiceLine.saveEx();
						
						withholding.setC_Invoice_ID(invoiceLine.getC_Invoice_ID());
						withholding.setC_InvoiceLine_ID(invoiceLine.getC_InvoiceLine_ID());
						withholding.saveEx();
						
					});
					
					if (whDocument.getInvoice()==null)
						whDocument.setInvoice(invoice);
					
					whDocument.addWithHolding(withholding);
					
				});
				
				withholdingDocList.add(whDocument);
			});
			
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
	private int C_DocType_ID = 0;
	private int C_ConversionType_ID = 0;
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
	public Withholding(int WH_Definition_ID, int WH_Setting_ID, int C_BPartner_ID, int C_DocType_ID, int C_ConversionType_ID, SvrProcess process) {
		this.WH_Definition_ID = WH_Definition_ID;
		this.WH_Setting_ID = WH_Setting_ID;
		this.C_BPartner_ID = C_BPartner_ID;
		this.C_DocType_ID= C_DocType_ID; 
		this.C_ConversionType_ID = C_ConversionType_ID;
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
	public Optional<MInvoice> getInvoice() {
		return Optional.ofNullable(invoice);
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
	
	/**
	 * Get Document Type Identifier
	 * @return
	 * @return int
	 */
	public int getC_DocType_ID() {
		return C_DocType_ID;
	}
	
	/**
	 * Set Document Type Identifier 
	 * @param c_DocType_ID
	 * @return void
	 */
	public void setC_DocType_ID(int c_DocType_ID) {
		C_DocType_ID = c_DocType_ID;
	}
	
	/**
	 * Get Conversion Type Identifier 
	 * @return
	 * @return int
	 */
	public int getC_ConversionType_ID() {
		return C_ConversionType_ID;
	}
	
	/**
	 * Set Conversion Type Identifier 
	 * @param c_ConversionType_ID
	 * @return void
	 */
	public void setC_ConversionType_ID(int c_ConversionType_ID) {
		C_ConversionType_ID = c_ConversionType_ID;
	}
	
}