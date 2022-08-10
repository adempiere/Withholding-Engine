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
package org.spin.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Msg;

/** Generated Model for WH_Allocation
 *  @author Adempiere (generated) 
 *  @version Release 3.9.2 - $Id$ */
public class MWHWithholding extends X_WH_Withholding implements DocAction, DocOptions {

	/**
	 *
	 */
	private static final long serialVersionUID = 20190611L;

    /** Standard Constructor */
    public MWHWithholding (Properties ctx, int WH_Allocation_ID, String trxName)
    {
      super (ctx, WH_Allocation_ID, trxName);
    }

    /** Load Constructor */
    public MWHWithholding (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName() + get_ID() +"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	processIt
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt()
	{
		log.info("unlockIt - " + toString());
	//	setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt
	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		setValuesFromSourceDocument();
		return true;
	}
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());

		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateDoc(), dt.getDocBaseType(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}
		//	Add up Amounts
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info("rejectIt - " + toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		log.info(toString());
		//
		
		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		//	Set Definitive Document No
		setDefiniteDocumentNo();

		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	/**
	 * 	Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setDateDoc(new Timestamp(System.currentTimeMillis()));
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = null;
			int index = p_info.getColumnIndex("C_DocType_ID");
			if (index == -1)
				index = p_info.getColumnIndex("C_DocTypeTarget_ID");
			if (index != -1)		//	get based on Doc Type (might return null)
				value = DB.getDocumentNo(get_ValueAsInt(index), get_TrxName(), true);
			if (value != null) {
				setDocumentNo(value);
			}
		}
	}

	/**
	 * Set Document Type
	 */
	public void setC_DocType_ID() {
		String sql = "SELECT C_DocType_ID FROM C_DocType "
			+ "WHERE AD_Client_ID = ? AND AD_Org_ID IN (0," + getAD_Org_ID()
			+ ") AND DocBaseType = ? "
			+ " AND IsActive = 'Y' "
			+ "ORDER BY AD_Org_ID, IsDefault DESC";
		int documentTypeId = DB.getSQLValue(null, sql, getAD_Client_ID(), "WHH");
		if (documentTypeId <= 0) {
			log.severe ("Not found for AD_Client_ID=" + getAD_Client_ID () + ", DocBaseType=WHH");
		} else {
			log.fine("(APS) - " + "WHH");
			setC_DocType_ID(documentTypeId);
		}
	}	//	setC_DocTypeTarget_ID
	
	/**
	 * Set Values From Document
	 * @return void
	 */
	private void setValuesFromSourceDocument() {
		
		PO document = null;
		if (getSourceInvoice_ID() > 0)
			document = new MInvoice(getCtx(), getSourceInvoice_ID(), get_TrxName());
		if (document == null 
				&& getSourceOrder_ID() > 0)
			document = new MOrder(getCtx(), getSourceOrder_ID(), get_TrxName());
		
		Optional<PO> maybeSourceDocument = Optional.ofNullable(document);
		maybeSourceDocument.ifPresent(sourceDocument -> {
			if (getC_Currency_ID() == 0)
				setC_Currency_ID(sourceDocument.get_ValueAsInt(MWHWithholding.COLUMNNAME_C_Currency_ID));
			if (getC_ConversionType_ID() == 0) {
				int C_ConversionType_ID = sourceDocument.get_ValueAsInt(MWHWithholding.COLUMNNAME_C_ConversionType_ID); 
				if (C_ConversionType_ID == 0)
					C_ConversionType_ID = MConversionType.getDefault(getAD_Client_ID());
				setC_ConversionType_ID(C_ConversionType_ID);
			}
			if (getC_BPartner_Location_ID() == 0 )
				setC_BPartner_Location_ID(sourceDocument.get_ValueAsInt(MWHWithholding.COLUMNNAME_C_BPartner_Location_ID));
			if (getDateAcct() == null)
				setDateAcct((Timestamp)sourceDocument.get_Value(MWHWithholding.COLUMNNAME_DateAcct));
			
			setIsSOTrx(sourceDocument.get_ValueAsBoolean(MWHWithholding.COLUMNNAME_IsSOTrx));
		});
	}

	/**
	 * 	Void Document.
	 * 	Same as Close.
	 * 	@return true if success 
	 */
	public boolean voidIt()
	{
		log.info("voidIt - " + toString());
		// Before Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
		if (m_processMsg != null)
			return false;
		// After Void
		//	Declaration
		if(getWithholdingDeclaration_ID() != 0) {
			MInvoice declaration = (MInvoice) getWithholdingDeclaration();
			if(!declaration.getDocStatus().equals(MInvoice.DOCSTATUS_Reversed)
					&& !declaration.getDocStatus().equals(MInvoice.DOCSTATUS_Voided)) {
				throw new AdempiereException("@DeclarationReferenceError@: @WithholdingDeclaration_ID@ " + declaration.getDocumentNo());
			}
		}
		//	Withholding
		if(getC_Invoice_ID() != 0) {
			MInvoice withholdingDoc = (MInvoice) getC_Invoice();
			if(!withholdingDoc.getDocStatus().equals(MInvoice.DOCSTATUS_Reversed)
					&& !withholdingDoc.getDocStatus().equals(MInvoice.DOCSTATUS_Voided)) {
				throw new AdempiereException("@WithholdingReferenceError@: @C_Invoice_ID@ " + withholdingDoc.getDocumentNo());
			}
		}
		//	AP document
		if(getC_Invoice_ID() != 0) {
			MInvoice invoice = (MInvoice) getC_Invoice();
			if(invoice.getDocStatus().equals(MInvoice.DOCSTATUS_Completed)) {
				if(!invoice.processIt(MInvoice.ACTION_Reverse_Correct)) {
					throw new AdempiereException(invoice.getProcessMsg());
				}
				invoice.saveEx();
			}
		}
		//	Validate reference
		addDescription(Msg.getMsg(getCtx(), "Voided"));
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;

		setProcessed(true);
        setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt
	
	/**
     *  Add to Description
     *  @param description text
     */
    public void addDescription (String description) {
        String desc = getDescription();
        if (desc == null)
            setDescription(description);
        else
            setDescription(desc + " | " + description);
    }   //  addDescription
	
	/**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
	 * 	@return true if success 
	 */
	public boolean closeIt() {
		log.info("closeIt - " + toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;
		
		setProcessed(true);
		setDocAction(DOCACTION_None);
		
		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;

		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Correction
	 * 	@return true if success 
	 */
	public boolean reverseCorrectIt()
	{
		log.info("reverseCorrectIt - " + toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		//	Void It
		voidIt();
		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;

		return false;
	}	//	reverseCorrectionIt
	
	/**
	 * 	Reverse Accrual - none
	 * 	@return true if success 
	 */
	public boolean reverseAccrualIt()
	{
		log.info("reverseAccrualIt - " + toString());
		// Before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		//	Void It
		voidIt();
		// After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;

		return false;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
	public boolean reActivateIt()
	{
		log.info("reActivateIt - " + toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		setDocAction(DOCACTION_Complete);
		setProcessed(false);
		return true;
	}	//	reActivateIt
	
	
	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
	//	sb.append(": ")
	//		.append(Msg.translate(getCtx(),"TotalLines")).append("=").append(getTotalLines())
	//		.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
	//	return getSalesRep_ID();
		return 0;
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return null;	//getTotalLines();
	}	//	getApprovalAmt
	
	/**
	 * Get Withholding list from invoice
	 * @param context
	 * @param sourceInvoiceId
	 * @param transactionName
	 * @return
	 */
	public static List<MWHWithholding> getWithholdingFromInvoice(Properties context, int sourceInvoiceId, String transactionName) {
		return new Query(context, I_WH_Withholding.Table_Name, I_WH_Withholding.COLUMNNAME_SourceInvoice_ID + " = ?", transactionName)
			.setClient_ID()
			.setParameters(sourceInvoiceId)
			.setOnlyActiveRecords(true)
			.<MWHWithholding>list();
	}
	
	/**
	 * Get Withholding source list from invoice
	 * @param context
	 * @param invoiceId
	 * @param transactionName
	 * @return
	 */
	public static List<MWHWithholding> getWithholdingSourceFromInvoice(Properties context, int invoiceId, String transactionName) {
		return new Query(context, I_WH_Withholding.Table_Name, I_WH_Withholding.COLUMNNAME_C_Invoice_ID + " = ?", transactionName)
			.setClient_ID()
			.setParameters(invoiceId)
			.setOnlyActiveRecords(true)
			.<MWHWithholding>list();
	}
	
	/**
	 * Get Withholding source list from declaration
	 * @param context
	 * @param withholdingDeclarationId
	 * @param transactionName
	 * @return
	 */
	public static List<MWHWithholding> getWithholdingSourceFromDeclaration(Properties context, int withholdingDeclarationId, String transactionName) {
		return new Query(context, I_WH_Withholding.Table_Name, I_WH_Withholding.COLUMNNAME_WithholdingDeclaration_ID + " = ?", transactionName)
			.setClient_ID()
			.setParameters(withholdingDeclarationId)
			.setOnlyActiveRecords(true)
			.<MWHWithholding>list();
	}
	
	public int getWHDocType() {
		if (getSourceInvoice_ID() !=0) {
			if (getSourceInvoice().getC_DocType().getDocBaseType().equals(MDocType.DOCBASETYPE_APInvoice)
					|| getSourceInvoice().getC_DocType().getDocBaseType().equals(MDocType.DOCBASETYPE_ARInvoice)) {
				if (getWH_Setting().getWithholdingDebitDocType_ID()!=0)
					return getWH_Setting().getWithholdingDebitDocType_ID();
				if (getWH_Definition().getWithholdingDebitDocType_ID()!=0)
					return getWH_Definition().getWithholdingDebitDocType_ID();
				if (getWH_Setting().getWH_Type().getWithholdingDebitDocType_ID()!=0)
					return getWH_Setting().getWH_Type().getWithholdingDebitDocType_ID();
			}else if (getSourceInvoice().getC_DocType().getDocBaseType().equals(MDocType.DOCBASETYPE_APCreditMemo)
					|| getSourceInvoice().getC_DocType().getDocBaseType().equals(MDocType.DOCBASETYPE_ARCreditMemo)){
				if (getWH_Setting().getWithholdingCreditDocType_ID()!=0)
					return getWH_Setting().getWithholdingCreditDocType_ID();
				if (getWH_Definition().getWithholdingCreditDocType_ID()!=0)
					return getWH_Definition().getWithholdingCreditDocType_ID();
				if (getWH_Setting().getWH_Type().getWithholdingCreditDocType_ID()!=0)
					return getWH_Setting().getWH_Type().getWithholdingCreditDocType_ID();
			}
				
		}
		return 0;
	}

	@Override
	public int customizeValidActions(String docStatus, Object processing,
			String orderType, String isSOTrx, int AD_Table_ID,
			String[] docAction, String[] options, int index) {
		//	Valid Document Action
		if (AD_Table_ID == Table_ID){
			if (docStatus.equals(DocumentEngine.STATUS_Completed)) {
				options[index++] = DocumentEngine.ACTION_Void;
			}
		}
		//	Default
		return index;
	}
	
    @Override
    public String toString()
    {
      StringBuffer sb = new StringBuffer ("MWHAllocation[")
        .append(getSummary()).append("]");
      return sb.toString();
    }
    
    @Override
    public void setWithholdingDeclaration_ID(int WithholdingDeclaration_ID) {
    	setIsDeclared(WithholdingDeclaration_ID > 0);
    	super.setWithholdingDeclaration_ID(WithholdingDeclaration_ID);
    }
}