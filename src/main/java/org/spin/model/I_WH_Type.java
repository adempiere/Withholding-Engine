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

/** Generated Interface for WH_Type
 *  @author Adempiere (generated) 
 *  @version Release 3.9.2
 */
public interface I_WH_Type 
{

    /** TableName=WH_Type */
    public static final String Table_Name = "WH_Type";

    /** AD_Table_ID=54648 */
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

    /** Column name AD_View_ID */
    public static final String COLUMNNAME_AD_View_ID = "AD_View_ID";

	/** Set View.
	  * View allows you to create dynamic views of information from the dictionary application
	  */
	public void setAD_View_ID (int AD_View_ID);

	/** Get View.
	  * View allows you to create dynamic views of information from the dictionary application
	  */
	public int getAD_View_ID();

	public org.adempiere.core.domains.models.I_AD_View getAD_View() throws RuntimeException;

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

    /** Column name DeclarationCreditDocType_ID */
    public static final String COLUMNNAME_DeclarationCreditDocType_ID = "DeclarationCreditDocType_ID";

	/** Set Declaration Credit Document Type.
	  * Declaration Credit Document Type
	  */
	public void setDeclarationCreditDocType_ID (int DeclarationCreditDocType_ID);

	/** Get Declaration Credit Document Type.
	  * Declaration Credit Document Type
	  */
	public int getDeclarationCreditDocType_ID();

	public org.adempiere.core.domains.models.I_C_DocType getDeclarationCreditDocType() throws RuntimeException;

    /** Column name DeclarationDebitDocType_ID */
    public static final String COLUMNNAME_DeclarationDebitDocType_ID = "DeclarationDebitDocType_ID";

	/** Set Declaration Debit Document Type.
	  * Declaration Debit Document Type
	  */
	public void setDeclarationDebitDocType_ID (int DeclarationDebitDocType_ID);

	/** Get Declaration Debit Document Type.
	  * Declaration Debit Document Type
	  */
	public int getDeclarationDebitDocType_ID();

	public org.adempiere.core.domains.models.I_C_DocType getDeclarationDebitDocType() throws RuntimeException;

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

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();

    /** Column name WH_Type_ID */
    public static final String COLUMNNAME_WH_Type_ID = "WH_Type_ID";

	/** Set Withholding Type.
	  * Indicates the types of national tax withholdings
	  */
	public void setWH_Type_ID (int WH_Type_ID);

	/** Get Withholding Type.
	  * Indicates the types of national tax withholdings
	  */
	public int getWH_Type_ID();

    /** Column name WithholdingCreditDocType_ID */
    public static final String COLUMNNAME_WithholdingCreditDocType_ID = "WithholdingCreditDocType_ID";

	/** Set Withholding Credit Document Type.
	  * Withholding Credit Document Type
	  */
	public void setWithholdingCreditDocType_ID (int WithholdingCreditDocType_ID);

	/** Get Withholding Credit Document Type.
	  * Withholding Credit Document Type
	  */
	public int getWithholdingCreditDocType_ID();

	public org.adempiere.core.domains.models.I_C_DocType getWithholdingCreditDocType() throws RuntimeException;

    /** Column name WithholdingDebitDocType_ID */
    public static final String COLUMNNAME_WithholdingDebitDocType_ID = "WithholdingDebitDocType_ID";

	/** Set Withholding Debit Document Type.
	  * Withholding Debit Document Type
	  */
	public void setWithholdingDebitDocType_ID (int WithholdingDebitDocType_ID);

	/** Get Withholding Debit Document Type.
	  * Withholding Debit Document Type
	  */
	public int getWithholdingDebitDocType_ID();

	public org.adempiere.core.domains.models.I_C_DocType getWithholdingDebitDocType() throws RuntimeException;
}
