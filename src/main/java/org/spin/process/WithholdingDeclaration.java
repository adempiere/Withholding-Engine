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
import org.compiere.model.MCurrency;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MPriceList;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.spin.model.MWHDefinition;
import org.spin.model.MWHType;
import org.spin.model.MWHWithholding;

/** Generated Process for (Generate Withholding Declaration)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.2
 */
public class WithholdingDeclaration extends WithholdingDeclarationAbstract
{
	private ArrayList<Declaration> m_Declarations = new ArrayList<Declaration> ();
	
	protected void prepare() {
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception {
		String result;
		if (isSelection()) {
			getSelectionValues()
				.entrySet()
				.stream()
				.forEach(list -> {
					MInvoice invoice= new MInvoice(getCtx(), list.getKey(), get_TrxName());
					addWHDoc(invoice);
				});
			result = processWHDoc();
		}else {
			StringBuffer whereClause = new StringBuffer();
			ArrayList<Object> params = new ArrayList<Object>();
			
			whereClause.append("AD_Client_ID = ? ");
			params.add(getAD_Client_ID());
			
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
				whereClause.append(" AND WH_Type_ID = ? ");
				params.add(getParameterAsInt("WH_Type_ID"));
			}
			
			new Query(getCtx(), MInvoice.Table_Name, whereClause.toString(), get_TrxName())
					.setParameters(params)
					.list()
					.forEach( invoice -> {
						addWHDoc((MInvoice) invoice);
					});
			result = processWHDoc();
		}
		
		return result;
	}
	
	private void addWHDoc(MInvoice invoiceWH) {
		
		MWHWithholding withholding = new Query(getCtx(), MWHWithholding.Table_Name, "C_Invoice_ID = ? AND IsSimulation = 'N' AND IsDeclared = 'N'", get_TrxName())
										.setParameters(invoiceWH.getC_Invoice_ID())
										.first();
		
		if (withholding!=null
				&& withholding.get_ID() > 0) {
			
			Declaration declaration = m_Declarations.stream()
													.filter(dec -> dec.getM_WHType_ID()==withholding.getWH_Setting().getWH_Type_ID())
													.findFirst()
													.orElse(null);
			BigDecimal amt = (invoiceWH.getGrandTotal(true).compareTo(Env.ZERO)>0 ? invoiceWH.getTotalLines(): invoiceWH.getTotalLines().negate());
			if (declaration==null) {
				declaration = new Declaration((MWHType)withholding.getWH_Setting().getWH_Type(),(MWHDefinition)withholding.getWH_Definition(), amt , this, invoiceWH.getC_Currency_ID());
				m_Declarations.add(declaration);
			}
			else
				declaration.addAmt(amt);
			
			declaration.addInvoice(invoiceWH);
		}
	}
	
	/**
	 * Process Document
	 */
	private String processWHDoc() {
		AtomicReference<String> result = new AtomicReference<String>();
		result.set("");
		m_Declarations.forEach(declaration -> {
			declaration.process();
			result.set(result.get() + "\n" + "@DocumentNo@  : "+ declaration.getDocumentNo());
		});
		return result.get();
	}
}

class Declaration {
	
	private MWHType m_WHType = null;
	private MWHDefinition m_WHDefinition = null;
	private BigDecimal m_Amt = Env.ZERO;
	private ArrayList<MInvoice> m_InvoicesWH = new ArrayList<MInvoice>();
	private SvrProcess process = null;
	private MInvoice m_Declaration = null;
	private int C_Currency_ID = 0;
	
	public Declaration(MWHType m_WHType, MWHDefinition m_WHDefinition, BigDecimal m_Amt,SvrProcess process, int C_Currency_ID) {
		this.m_WHType = m_WHType;
		this.m_Amt = m_Amt;
		this.process = process;
		this.m_WHDefinition = m_WHDefinition;
		this.C_Currency_ID = C_Currency_ID;
	}

	public MWHType getM_WHType() {
		return m_WHType;
	}
	
	public int getM_WHType_ID() {
		if (m_WHType!=null)
			return m_WHType.get_ID();
		
		return 0;
	}
	
	public BigDecimal getM_Amt() {
		return m_Amt;
	}
	
	public void addAmt(BigDecimal amt) {
		if (amt!=null
				&& m_Amt!=null)
			m_Amt= m_Amt.add(amt);
	}
	
	public void addInvoice(MInvoice invoice) {
		m_InvoicesWH.add(invoice);
	}
	
	public void process() {
		if (m_Amt!=null
				&& m_InvoicesWH!=null
					&& m_InvoicesWH.size() > 0) {
			if (m_Amt.compareTo(Env.ZERO) < 0) 
				GenerateDeclaration(false);
			else 
				GenerateDeclaration(true);
			
			setDeclaration();
				
		}
	}
	
	private void GenerateDeclaration(boolean isCredit) {
		m_Declaration = new MInvoice(process.getCtx(), 0, process.get_TrxName());
		m_Declaration.setC_BPartner_ID(m_WHDefinition.getC_BPartner_ID());
		if (isCredit)
			m_Declaration.setC_DocTypeTarget_ID(m_WHType.getDeclarationCreditDocType_ID());
		else
			m_Declaration.setC_DocTypeTarget_ID(m_WHType.getDeclarationDebitDocType_ID());
		
		if (m_Declaration.getC_DocTypeTarget_ID()==0)
			throw new AdempiereException("@Invalid@ @" + (isCredit ? MWHType.COLUMNNAME_DeclarationCreditDocType_ID : MWHType.COLUMNNAME_DeclarationDebitDocType_ID) + "@");
		
		m_Declaration.setDateInvoiced(process.getParameterAsTimestamp("DateDoc"));
		m_Declaration.setDateAcct(process.getParameterAsTimestamp("DateDoc"));
		m_Declaration.setIsSOTrx(m_Declaration.getC_DocTypeTarget().isSOTrx());
		
		Optional.ofNullable(MPriceList.getDefault(process.getCtx(), 
												m_Declaration.isSOTrx(), 
												MCurrency.get(process.getCtx(), C_Currency_ID).getISO_Code()))
				.ifPresent(priceList ->{
					m_Declaration.setM_PriceList_ID(priceList.getM_PriceList_ID());
				});
		m_Declaration.saveEx();
		
		MInvoiceLine declarationLine = new MInvoiceLine(m_Declaration);
		declarationLine.setC_Charge_ID(m_WHDefinition.getC_Charge_ID());
		declarationLine.setQty(Env.ONE);
		declarationLine.setPrice(m_Amt.abs());
		declarationLine.saveEx();
		
		m_Declaration.processIt(MInvoice.ACTION_Complete);
		m_Declaration.saveEx();
	}
	
	private void setDeclaration() {
		for (MInvoice mInvoice : m_InvoicesWH) {
			new Query(process.getCtx(), 
						MWHWithholding.Table_Name, 
						"C_Invoice_ID = ? ", 
						process.get_TrxName())
				.setParameters(mInvoice.getC_Invoice_ID())
				.list()
				.forEach(wh ->{
					wh.set_ValueOfColumn("WithholdingDeclaration_ID", m_Declaration.getC_Invoice_ID());
					wh.saveEx();
				});
		}
	}
	@Override
	public String toString() {
		return "WHType = " + (m_WHType ==null ? ""  :m_WHType.toString())
				+ "\n Amount = " + m_Amt.toString()
				+ "\n Invoice = " + m_InvoicesWH.toString();
	}
	
	public String getDocumentNo() {
		if (m_Declaration!=null)
			return m_Declaration.getDocumentNo();
		
		return "";
	}
	
}

