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

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for WH_Withholding
 *  @author Adempiere (generated) 
 *  @version Release 3.9.3
 */
public interface I_WH_Withholding 
{

    /** TableName=WH_Withholding */
    public static final String Table_Name = "WH_Withholding";

    /** AD_Table_ID=54647 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name A_Base_Amount */
    public static final String COLUMNNAME_A_Base_Amount = "A_Base_Amount";

	/** Set A_Base_Amount	  */
	public void setA_Base_Amount (BigDecimal A_Base_Amount);

	/** Get A_Base_Amount	  */
	public BigDecimal getA_Base_Amount();

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public org.adempiere.core.domains.models.I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_BPartner_Location_ID */
    public static final String COLUMNNAME_C_BPartner_Location_ID = "C_BPartner_Location_ID";

	/** Set Partner Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID);

	/** Get Partner Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID();

	public org.adempiere.core.domains.models.I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException;

    /** Column name C_ConversionType_ID */
    public static final String COLUMNNAME_C_ConversionType_ID = "C_ConversionType_ID";

	/** Set Currency Type.
	  * Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID);

	/** Get Currency Type.
	  * Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID();

	public org.adempiere.core.domains.models.I_C_ConversionType getC_ConversionType() throws RuntimeException;

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public org.adempiere.core.domains.models.I_C_Currency getC_Currency() throws RuntimeException;

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public org.adempiere.core.domains.models.I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name C_InvoiceLine_ID */
    public static final String COLUMNNAME_C_InvoiceLine_ID = "C_InvoiceLine_ID";

	/** Set Invoice Line.
	  * Invoice Detail Line
	  */
	public void setC_InvoiceLine_ID (int C_InvoiceLine_ID);

	/** Get Invoice Line.
	  * Invoice Detail Line
	  */
	public int getC_InvoiceLine_ID();

	public org.adempiere.core.domains.models.I_C_InvoiceLine getC_InvoiceLine() throws RuntimeException;

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public org.adempiere.core.domains.models.I_C_Invoice getC_Invoice() throws RuntimeException;

    /** Column name C_Tax_ID */
    public static final String COLUMNNAME_C_Tax_ID = "C_Tax_ID";

	/** Set Tax.
	  * Tax identifier
	  */
	public void setC_Tax_ID (int C_Tax_ID);

	/** Get Tax.
	  * Tax identifier
	  */
	public int getC_Tax_ID();

	public org.adempiere.core.domains.models.I_C_Tax getC_Tax() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DateAcct */
    public static final String COLUMNNAME_DateAcct = "DateAcct";

	/** Set Account Date.
	  * Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct);

	/** Get Account Date.
	  * Accounting Date
	  */
	public Timestamp getDateAcct();

    /** Column name DateDoc */
    public static final String COLUMNNAME_DateDoc = "DateDoc";

	/** Set Document Date.
	  * Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc);

	/** Get Document Date.
	  * Date of the Document
	  */
	public Timestamp getDateDoc();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name DocAction */
    public static final String COLUMNNAME_DocAction = "DocAction";

	/** Set Document Action.
	  * The targeted status of the document
	  */
	public void setDocAction (String DocAction);

	/** Get Document Action.
	  * The targeted status of the document
	  */
	public String getDocAction();

    /** Column name DocStatus */
    public static final String COLUMNNAME_DocStatus = "DocStatus";

	/** Set Document Status.
	  * The current status of the document
	  */
	public void setDocStatus (String DocStatus);

	/** Get Document Status.
	  * The current status of the document
	  */
	public String getDocStatus();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsApproved */
    public static final String COLUMNNAME_IsApproved = "IsApproved";

	/** Set Approved.
	  * Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved);

	/** Get Approved.
	  * Indicates if this document requires approval
	  */
	public boolean isApproved();

    /** Column name IsDeclared */
    public static final String COLUMNNAME_IsDeclared = "IsDeclared";

	/** Set Is Declared.
	  * Show if a withholding has been declared
	  */
	public void setIsDeclared (boolean IsDeclared);

	/** Get Is Declared.
	  * Show if a withholding has been declared
	  */
	public boolean isDeclared();

    /** Column name IsManual */
    public static final String COLUMNNAME_IsManual = "IsManual";

	/** Set Manual.
	  * This is a manual process
	  */
	public void setIsManual (boolean IsManual);

	/** Get Manual.
	  * This is a manual process
	  */
	public boolean isManual();

    /** Column name IsSOTrx */
    public static final String COLUMNNAME_IsSOTrx = "IsSOTrx";

	/** Set Sales Transaction.
	  * This is a Sales Transaction
	  */
	public void setIsSOTrx (boolean IsSOTrx);

	/** Get Sales Transaction.
	  * This is a Sales Transaction
	  */
	public boolean isSOTrx();

    /** Column name IsSimulation */
    public static final String COLUMNNAME_IsSimulation = "IsSimulation";

	/** Set Simulation.
	  * Performing the function is only simulated
	  */
	public void setIsSimulation (boolean IsSimulation);

	/** Get Simulation.
	  * Performing the function is only simulated
	  */
	public boolean isSimulation();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name SourceInvoiceLine_ID */
    public static final String COLUMNNAME_SourceInvoiceLine_ID = "SourceInvoiceLine_ID";

	/** Set Source Invoice Line	  */
	public void setSourceInvoiceLine_ID (int SourceInvoiceLine_ID);

	/** Get Source Invoice Line	  */
	public int getSourceInvoiceLine_ID();

	public org.adempiere.core.domains.models.I_C_InvoiceLine getSourceInvoiceLine() throws RuntimeException;

    /** Column name SourceInvoice_ID */
    public static final String COLUMNNAME_SourceInvoice_ID = "SourceInvoice_ID";

	/** Set Source Invoice	  */
	public void setSourceInvoice_ID (int SourceInvoice_ID);

	/** Get Source Invoice	  */
	public int getSourceInvoice_ID();

	public org.adempiere.core.domains.models.I_C_Invoice getSourceInvoice() throws RuntimeException;

    /** Column name SourceOrderLine_ID */
    public static final String COLUMNNAME_SourceOrderLine_ID = "SourceOrderLine_ID";

	/** Set Source Order Line	  */
	public void setSourceOrderLine_ID (int SourceOrderLine_ID);

	/** Get Source Order Line	  */
	public int getSourceOrderLine_ID();

	public org.adempiere.core.domains.models.I_C_OrderLine getSourceOrderLine() throws RuntimeException;

    /** Column name SourceOrder_ID */
    public static final String COLUMNNAME_SourceOrder_ID = "SourceOrder_ID";

	/** Set Source Order	  */
	public void setSourceOrder_ID (int SourceOrder_ID);

	/** Get Source Order	  */
	public int getSourceOrder_ID();

	public org.adempiere.core.domains.models.I_C_Order getSourceOrder() throws RuntimeException;

    /** Column name UUID */
    public static final String COLUMNNAME_UUID = "UUID";

	/** Set Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID);

	/** Get Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public String getUUID();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name WH_Definition_ID */
    public static final String COLUMNNAME_WH_Definition_ID = "WH_Definition_ID";

	/** Set Withholding .
	  * Withholding Definition is used for define a withholding rule for BP
	  */
	public void setWH_Definition_ID (int WH_Definition_ID);

	/** Get Withholding .
	  * Withholding Definition is used for define a withholding rule for BP
	  */
	public int getWH_Definition_ID();

	public org.spin.model.I_WH_Definition getWH_Definition() throws RuntimeException;

    /** Column name WH_Setting_ID */
    public static final String COLUMNNAME_WH_Setting_ID = "WH_Setting_ID";

	/** Set Withholding Setting.
	  * specifies the setting to each applied withholding
	  */
	public void setWH_Setting_ID (int WH_Setting_ID);

	/** Get Withholding Setting.
	  * specifies the setting to each applied withholding
	  */
	public int getWH_Setting_ID();

	public org.spin.model.I_WH_Setting getWH_Setting() throws RuntimeException;

    /** Column name WH_Withholding_ID */
    public static final String COLUMNNAME_WH_Withholding_ID = "WH_Withholding_ID";

	/** Set Withholding Allocation	  */
	public void setWH_Withholding_ID (int WH_Withholding_ID);

	/** Get Withholding Allocation	  */
	public int getWH_Withholding_ID();

    /** Column name WithholdingAmt */
    public static final String COLUMNNAME_WithholdingAmt = "WithholdingAmt";

	/** Set Withholding Amt	  */
	public void setWithholdingAmt (BigDecimal WithholdingAmt);

	/** Get Withholding Amt	  */
	public BigDecimal getWithholdingAmt();

    /** Column name WithholdingDeclaration_ID */
    public static final String COLUMNNAME_WithholdingDeclaration_ID = "WithholdingDeclaration_ID";

	/** Set Withholding Declaration.
	  * Withholding Declaration reference
	  */
	public void setWithholdingDeclaration_ID (int WithholdingDeclaration_ID);

	/** Get Withholding Declaration.
	  * Withholding Declaration reference
	  */
	public int getWithholdingDeclaration_ID();

	public org.adempiere.core.domains.models.I_C_Invoice getWithholdingDeclaration() throws RuntimeException;

    /** Column name WithholdingRate */
    public static final String COLUMNNAME_WithholdingRate = "WithholdingRate";

	/** Set Withholding Rate.
	  * Withholding Rate applied to Document
	  */
	public void setWithholdingRate (BigDecimal WithholdingRate);

	/** Get Withholding Rate.
	  * Withholding Rate applied to Document
	  */
	public BigDecimal getWithholdingRate();
}
