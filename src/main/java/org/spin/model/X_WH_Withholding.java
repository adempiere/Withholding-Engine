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
/** Generated Model - DO NOT CHANGE */
package org.spin.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for WH_Withholding
 *  @author Adempiere (generated) 
 *  @version Release 3.9.3 - $Id$ */
public class X_WH_Withholding extends PO implements I_WH_Withholding, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20220519L;

    /** Standard Constructor */
    public X_WH_Withholding (Properties ctx, int WH_Withholding_ID, String trxName)
    {
      super (ctx, WH_Withholding_ID, trxName);
      /** if (WH_Withholding_ID == 0)
        {
			setC_DocType_ID (0);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setIsApproved (false);
// N
			setProcessed (false);
// N
			setProcessing (false);
// N
			setWH_Definition_ID (0);
			setWH_Setting_ID (0);
			setWH_Withholding_ID (0);
			setWithholdingAmt (Env.ZERO);
			setWithholdingRate (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_WH_Withholding (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_WH_Withholding[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set A_Base_Amount.
		@param A_Base_Amount A_Base_Amount	  */
	public void setA_Base_Amount (BigDecimal A_Base_Amount)
	{
		set_Value (COLUMNNAME_A_Base_Amount, A_Base_Amount);
	}

	/** Get A_Base_Amount.
		@return A_Base_Amount	  */
	public BigDecimal getA_Base_Amount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_A_Base_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.adempiere.core.domains.models.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_BPartner)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_BPartner_Location)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_BPartner_Location.Table_Name)
			.getPO(getC_BPartner_Location_ID(), get_TrxName());	}

	/** Set Partner Location.
		@param C_BPartner_Location_ID 
		Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
	{
		if (C_BPartner_Location_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, Integer.valueOf(C_BPartner_Location_ID));
	}

	/** Get Partner Location.
		@return Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_ConversionType getC_ConversionType() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_ConversionType)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_ConversionType.Table_Name)
			.getPO(getC_ConversionType_ID(), get_TrxName());	}

	/** Set Currency Type.
		@param C_ConversionType_ID 
		Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID)
	{
		if (C_ConversionType_ID < 1) 
			set_Value (COLUMNNAME_C_ConversionType_ID, null);
		else 
			set_Value (COLUMNNAME_C_ConversionType_ID, Integer.valueOf(C_ConversionType_ID));
	}

	/** Get Currency Type.
		@return Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Currency)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_DocType)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_InvoiceLine getC_InvoiceLine() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_InvoiceLine)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_InvoiceLine.Table_Name)
			.getPO(getC_InvoiceLine_ID(), get_TrxName());	}

	/** Set Invoice Line.
		@param C_InvoiceLine_ID 
		Invoice Detail Line
	  */
	public void setC_InvoiceLine_ID (int C_InvoiceLine_ID)
	{
		if (C_InvoiceLine_ID < 1) 
			set_Value (COLUMNNAME_C_InvoiceLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_InvoiceLine_ID, Integer.valueOf(C_InvoiceLine_ID));
	}

	/** Get Invoice Line.
		@return Invoice Detail Line
	  */
	public int getC_InvoiceLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_InvoiceLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Invoice)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_Tax getC_Tax() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Tax)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Tax.Table_Name)
			.getPO(getC_Tax_ID(), get_TrxName());	}

	/** Set Tax.
		@param C_Tax_ID 
		Tax identifier
	  */
	public void setC_Tax_ID (int C_Tax_ID)
	{
		if (C_Tax_ID < 1) 
			set_Value (COLUMNNAME_C_Tax_ID, null);
		else 
			set_Value (COLUMNNAME_C_Tax_ID, Integer.valueOf(C_Tax_ID));
	}

	/** Get Tax.
		@return Tax identifier
	  */
	public int getC_Tax_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Tax_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Account Date.
		@param DateAcct 
		Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct)
	{
		set_Value (COLUMNNAME_DateAcct, DateAcct);
	}

	/** Get Account Date.
		@return Accounting Date
	  */
	public Timestamp getDateAcct () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcct);
	}

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID=135;
	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";
	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";
	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";
	/** Post = PO */
	public static final String DOCACTION_Post = "PO";
	/** Void = VO */
	public static final String DOCACTION_Void = "VO";
	/** Close = CL */
	public static final String DOCACTION_Close = "CL";
	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";
	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";
	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";
	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";
	/** <None> = -- */
	public static final String DOCACTION_None = "--";
	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";
	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";
	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";
	/** Set Document Action.
		@param DocAction 
		The targeted status of the document
	  */
	public void setDocAction (String DocAction)
	{

		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	public String getDocAction () 
	{
		return (String)get_Value(COLUMNNAME_DocAction);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
    }

	/** Set Approved.
		@param IsApproved 
		Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved)
	{
		set_Value (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved () 
	{
		Object oo = get_Value(COLUMNNAME_IsApproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Is Declared.
		@param IsDeclared 
		Show if a withholding has been declared
	  */
	public void setIsDeclared (boolean IsDeclared)
	{
		set_Value (COLUMNNAME_IsDeclared, Boolean.valueOf(IsDeclared));
	}

	/** Get Is Declared.
		@return Show if a withholding has been declared
	  */
	public boolean isDeclared () 
	{
		Object oo = get_Value(COLUMNNAME_IsDeclared);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Manual.
		@param IsManual 
		This is a manual process
	  */
	public void setIsManual (boolean IsManual)
	{
		set_Value (COLUMNNAME_IsManual, Boolean.valueOf(IsManual));
	}

	/** Get Manual.
		@return This is a manual process
	  */
	public boolean isManual () 
	{
		Object oo = get_Value(COLUMNNAME_IsManual);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Sales Transaction.
		@param IsSOTrx 
		This is a Sales Transaction
	  */
	public void setIsSOTrx (boolean IsSOTrx)
	{
		set_Value (COLUMNNAME_IsSOTrx, Boolean.valueOf(IsSOTrx));
	}

	/** Get Sales Transaction.
		@return This is a Sales Transaction
	  */
	public boolean isSOTrx () 
	{
		Object oo = get_Value(COLUMNNAME_IsSOTrx);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Simulation.
		@param IsSimulation 
		Performing the function is only simulated
	  */
	public void setIsSimulation (boolean IsSimulation)
	{
		set_Value (COLUMNNAME_IsSimulation, Boolean.valueOf(IsSimulation));
	}

	/** Get Simulation.
		@return Performing the function is only simulated
	  */
	public boolean isSimulation () 
	{
		Object oo = get_Value(COLUMNNAME_IsSimulation);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.adempiere.core.domains.models.I_C_InvoiceLine getSourceInvoiceLine() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_InvoiceLine)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_InvoiceLine.Table_Name)
			.getPO(getSourceInvoiceLine_ID(), get_TrxName());	}

	/** Set Source Invoice Line.
		@param SourceInvoiceLine_ID Source Invoice Line	  */
	public void setSourceInvoiceLine_ID (int SourceInvoiceLine_ID)
	{
		if (SourceInvoiceLine_ID < 1) 
			set_Value (COLUMNNAME_SourceInvoiceLine_ID, null);
		else 
			set_Value (COLUMNNAME_SourceInvoiceLine_ID, Integer.valueOf(SourceInvoiceLine_ID));
	}

	/** Get Source Invoice Line.
		@return Source Invoice Line	  */
	public int getSourceInvoiceLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SourceInvoiceLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_Invoice getSourceInvoice() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Invoice)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Invoice.Table_Name)
			.getPO(getSourceInvoice_ID(), get_TrxName());	}

	/** Set Source Invoice.
		@param SourceInvoice_ID Source Invoice	  */
	public void setSourceInvoice_ID (int SourceInvoice_ID)
	{
		if (SourceInvoice_ID < 1) 
			set_Value (COLUMNNAME_SourceInvoice_ID, null);
		else 
			set_Value (COLUMNNAME_SourceInvoice_ID, Integer.valueOf(SourceInvoice_ID));
	}

	/** Get Source Invoice.
		@return Source Invoice	  */
	public int getSourceInvoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SourceInvoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_OrderLine getSourceOrderLine() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_OrderLine)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_OrderLine.Table_Name)
			.getPO(getSourceOrderLine_ID(), get_TrxName());	}

	/** Set Source Order Line.
		@param SourceOrderLine_ID Source Order Line	  */
	public void setSourceOrderLine_ID (int SourceOrderLine_ID)
	{
		if (SourceOrderLine_ID < 1) 
			set_Value (COLUMNNAME_SourceOrderLine_ID, null);
		else 
			set_Value (COLUMNNAME_SourceOrderLine_ID, Integer.valueOf(SourceOrderLine_ID));
	}

	/** Get Source Order Line.
		@return Source Order Line	  */
	public int getSourceOrderLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SourceOrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_Order getSourceOrder() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Order)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Order.Table_Name)
			.getPO(getSourceOrder_ID(), get_TrxName());	}

	/** Set Source Order.
		@param SourceOrder_ID Source Order	  */
	public void setSourceOrder_ID (int SourceOrder_ID)
	{
		if (SourceOrder_ID < 1) 
			set_Value (COLUMNNAME_SourceOrder_ID, null);
		else 
			set_Value (COLUMNNAME_SourceOrder_ID, Integer.valueOf(SourceOrder_ID));
	}

	/** Get Source Order.
		@return Source Order	  */
	public int getSourceOrder_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SourceOrder_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Immutable Universally Unique Identifier.
		@param UUID 
		Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID)
	{
		set_Value (COLUMNNAME_UUID, UUID);
	}

	/** Get Immutable Universally Unique Identifier.
		@return Immutable Universally Unique Identifier
	  */
	public String getUUID () 
	{
		return (String)get_Value(COLUMNNAME_UUID);
	}

	public org.spin.model.I_WH_Definition getWH_Definition() throws RuntimeException
    {
		return (org.spin.model.I_WH_Definition)MTable.get(getCtx(), org.spin.model.I_WH_Definition.Table_Name)
			.getPO(getWH_Definition_ID(), get_TrxName());	}

	/** Set Withholding .
		@param WH_Definition_ID 
		Withholding Definition is used for define a withholding rule for BP
	  */
	public void setWH_Definition_ID (int WH_Definition_ID)
	{
		if (WH_Definition_ID < 1) 
			set_Value (COLUMNNAME_WH_Definition_ID, null);
		else 
			set_Value (COLUMNNAME_WH_Definition_ID, Integer.valueOf(WH_Definition_ID));
	}

	/** Get Withholding .
		@return Withholding Definition is used for define a withholding rule for BP
	  */
	public int getWH_Definition_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WH_Definition_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.spin.model.I_WH_Setting getWH_Setting() throws RuntimeException
    {
		return (org.spin.model.I_WH_Setting)MTable.get(getCtx(), org.spin.model.I_WH_Setting.Table_Name)
			.getPO(getWH_Setting_ID(), get_TrxName());	}

	/** Set Withholding Setting.
		@param WH_Setting_ID 
		specifies the setting to each applied withholding
	  */
	public void setWH_Setting_ID (int WH_Setting_ID)
	{
		if (WH_Setting_ID < 1) 
			set_Value (COLUMNNAME_WH_Setting_ID, null);
		else 
			set_Value (COLUMNNAME_WH_Setting_ID, Integer.valueOf(WH_Setting_ID));
	}

	/** Get Withholding Setting.
		@return specifies the setting to each applied withholding
	  */
	public int getWH_Setting_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WH_Setting_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Withholding Allocation.
		@param WH_Withholding_ID Withholding Allocation	  */
	public void setWH_Withholding_ID (int WH_Withholding_ID)
	{
		if (WH_Withholding_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_WH_Withholding_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_WH_Withholding_ID, Integer.valueOf(WH_Withholding_ID));
	}

	/** Get Withholding Allocation.
		@return Withholding Allocation	  */
	public int getWH_Withholding_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WH_Withholding_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Withholding Amt.
		@param WithholdingAmt Withholding Amt	  */
	public void setWithholdingAmt (BigDecimal WithholdingAmt)
	{
		set_Value (COLUMNNAME_WithholdingAmt, WithholdingAmt);
	}

	/** Get Withholding Amt.
		@return Withholding Amt	  */
	public BigDecimal getWithholdingAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WithholdingAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.adempiere.core.domains.models.I_C_Invoice getWithholdingDeclaration() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Invoice)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Invoice.Table_Name)
			.getPO(getWithholdingDeclaration_ID(), get_TrxName());	}

	/** Set Withholding Declaration.
		@param WithholdingDeclaration_ID 
		Withholding Declaration reference
	  */
	public void setWithholdingDeclaration_ID (int WithholdingDeclaration_ID)
	{
		if (WithholdingDeclaration_ID < 1) 
			set_Value (COLUMNNAME_WithholdingDeclaration_ID, null);
		else 
			set_Value (COLUMNNAME_WithholdingDeclaration_ID, Integer.valueOf(WithholdingDeclaration_ID));
	}

	/** Get Withholding Declaration.
		@return Withholding Declaration reference
	  */
	public int getWithholdingDeclaration_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WithholdingDeclaration_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Withholding Rate.
		@param WithholdingRate 
		Withholding Rate applied to Document
	  */
	public void setWithholdingRate (BigDecimal WithholdingRate)
	{
		set_Value (COLUMNNAME_WithholdingRate, WithholdingRate);
	}

	/** Get Withholding Rate.
		@return Withholding Rate applied to Document
	  */
	public BigDecimal getWithholdingRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WithholdingRate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}