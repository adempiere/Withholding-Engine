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

/** Generated Interface for WH_Log
 *  @author Adempiere (generated) 
 *  @version Release 3.9.2
 */
public interface I_WH_Log 
{

    /** TableName=WH_Log */
    public static final String Table_Name = "WH_Log";

    /** AD_Table_ID=54651 */
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

    /** Column name Comments */
    public static final String COLUMNNAME_Comments = "Comments";

	/** Set Comments.
	  * Comments or additional information
	  */
	public void setComments (String Comments);

	/** Get Comments.
	  * Comments or additional information
	  */
	public String getComments();

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

    /** Column name SourceInvoice_ID */
    public static final String COLUMNNAME_SourceInvoice_ID = "SourceInvoice_ID";

	/** Set Source Invoice	  */
	public void setSourceInvoice_ID (int SourceInvoice_ID);

	/** Get Source Invoice	  */
	public int getSourceInvoice_ID();

	public org.adempiere.core.domains.models.I_C_Invoice getSourceInvoice() throws RuntimeException;

    /** Column name SourceInvoiceLine_ID */
    public static final String COLUMNNAME_SourceInvoiceLine_ID = "SourceInvoiceLine_ID";

	/** Set Source Invoice Line	  */
	public void setSourceInvoiceLine_ID (int SourceInvoiceLine_ID);

	/** Get Source Invoice Line	  */
	public int getSourceInvoiceLine_ID();

	public org.adempiere.core.domains.models.I_C_InvoiceLine getSourceInvoiceLine() throws RuntimeException;

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

    /** Column name WH_Log_ID */
    public static final String COLUMNNAME_WH_Log_ID = "WH_Log_ID";

	/** Set Withholding Log.
	  * Save a log for each document processed
	  */
	public void setWH_Log_ID (int WH_Log_ID);

	/** Get Withholding Log.
	  * Save a log for each document processed
	  */
	public int getWH_Log_ID();

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

	public org.spin.model.I_WH_Withholding getWH_Withholding() throws RuntimeException;
}
