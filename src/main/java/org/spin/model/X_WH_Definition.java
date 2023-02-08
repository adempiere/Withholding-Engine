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

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for WH_Definition
 *  @author Adempiere (generated) 
 *  @version Release 3.9.2 - $Id$ */
public class X_WH_Definition extends PO implements I_WH_Definition, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190817L;

    /** Standard Constructor */
    public X_WH_Definition (Properties ctx, int WH_Definition_ID, String trxName)
    {
      super (ctx, WH_Definition_ID, trxName);
      /** if (WH_Definition_ID == 0)
        {
			setName (null);
			setWH_Definition_ID (0);
			setWH_Type_ID (0);
        } */
    }

    /** Load Constructor */
    public X_WH_Definition (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_WH_Definition[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	public org.adempiere.core.domains.models.I_C_Charge getC_Charge() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Charge)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Charge.Table_Name)
			.getPO(getC_Charge_ID(), get_TrxName());	}

	/** Set Charge.
		@param C_Charge_ID 
		Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID)
	{
		if (C_Charge_ID < 1) 
			set_Value (COLUMNNAME_C_Charge_ID, null);
		else 
			set_Value (COLUMNNAME_C_Charge_ID, Integer.valueOf(C_Charge_ID));
	}

	/** Get Charge.
		@return Additional document charges
	  */
	public int getC_Charge_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_DocType getDeclarationCreditDocType() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_DocType)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_DocType.Table_Name)
			.getPO(getDeclarationCreditDocType_ID(), get_TrxName());	}

	/** Set Declaration Credit Document Type.
		@param DeclarationCreditDocType_ID 
		Declaration Credit Document Type
	  */
	public void setDeclarationCreditDocType_ID (int DeclarationCreditDocType_ID)
	{
		if (DeclarationCreditDocType_ID < 1) 
			set_Value (COLUMNNAME_DeclarationCreditDocType_ID, null);
		else 
			set_Value (COLUMNNAME_DeclarationCreditDocType_ID, Integer.valueOf(DeclarationCreditDocType_ID));
	}

	/** Get Declaration Credit Document Type.
		@return Declaration Credit Document Type
	  */
	public int getDeclarationCreditDocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DeclarationCreditDocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_DocType getDeclarationDebitDocType() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_DocType)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_DocType.Table_Name)
			.getPO(getDeclarationDebitDocType_ID(), get_TrxName());	}

	/** Set Declaration Debit Document Type.
		@param DeclarationDebitDocType_ID 
		Declaration Debit Document Type
	  */
	public void setDeclarationDebitDocType_ID (int DeclarationDebitDocType_ID)
	{
		if (DeclarationDebitDocType_ID < 1) 
			set_Value (COLUMNNAME_DeclarationDebitDocType_ID, null);
		else 
			set_Value (COLUMNNAME_DeclarationDebitDocType_ID, Integer.valueOf(DeclarationDebitDocType_ID));
	}

	/** Get Declaration Debit Document Type.
		@return Declaration Debit Document Type
	  */
	public int getDeclarationDebitDocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DeclarationDebitDocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
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

	/** Set Withholding .
		@param WH_Definition_ID 
		Withholding Definition is used for define a withholding rule for BP
	  */
	public void setWH_Definition_ID (int WH_Definition_ID)
	{
		if (WH_Definition_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_WH_Definition_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_WH_Definition_ID, Integer.valueOf(WH_Definition_ID));
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

	public org.spin.model.I_WH_Type getWH_Type() throws RuntimeException
    {
		return (org.spin.model.I_WH_Type)MTable.get(getCtx(), org.spin.model.I_WH_Type.Table_Name)
			.getPO(getWH_Type_ID(), get_TrxName());	}

	/** Set Withholding Type.
		@param WH_Type_ID 
		Indicates the types of national tax withholdings
	  */
	public void setWH_Type_ID (int WH_Type_ID)
	{
		if (WH_Type_ID < 1) 
			set_Value (COLUMNNAME_WH_Type_ID, null);
		else 
			set_Value (COLUMNNAME_WH_Type_ID, Integer.valueOf(WH_Type_ID));
	}

	/** Get Withholding Type.
		@return Indicates the types of national tax withholdings
	  */
	public int getWH_Type_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WH_Type_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_DocType getWithholdingCreditDocType() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_DocType)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_DocType.Table_Name)
			.getPO(getWithholdingCreditDocType_ID(), get_TrxName());	}

	/** Set Withholding Credit Document Type.
		@param WithholdingCreditDocType_ID 
		Withholding Credit Document Type
	  */
	public void setWithholdingCreditDocType_ID (int WithholdingCreditDocType_ID)
	{
		if (WithholdingCreditDocType_ID < 1) 
			set_Value (COLUMNNAME_WithholdingCreditDocType_ID, null);
		else 
			set_Value (COLUMNNAME_WithholdingCreditDocType_ID, Integer.valueOf(WithholdingCreditDocType_ID));
	}

	/** Get Withholding Credit Document Type.
		@return Withholding Credit Document Type
	  */
	public int getWithholdingCreditDocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WithholdingCreditDocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_DocType getWithholdingDebitDocType() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_DocType)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_DocType.Table_Name)
			.getPO(getWithholdingDebitDocType_ID(), get_TrxName());	}

	/** Set Withholding Debit Document Type.
		@param WithholdingDebitDocType_ID 
		Withholding Debit Document Type
	  */
	public void setWithholdingDebitDocType_ID (int WithholdingDebitDocType_ID)
	{
		if (WithholdingDebitDocType_ID < 1) 
			set_Value (COLUMNNAME_WithholdingDebitDocType_ID, null);
		else 
			set_Value (COLUMNNAME_WithholdingDebitDocType_ID, Integer.valueOf(WithholdingDebitDocType_ID));
	}

	/** Get Withholding Debit Document Type.
		@return Withholding Debit Document Type
	  */
	public int getWithholdingDebitDocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WithholdingDebitDocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}