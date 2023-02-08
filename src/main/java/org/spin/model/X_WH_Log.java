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
import org.compiere.util.KeyNamePair;

/** Generated Model for WH_Log
 *  @author Adempiere (generated) 
 *  @version Release 3.9.2 - $Id$ */
public class X_WH_Log extends PO implements I_WH_Log, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190817L;

    /** Standard Constructor */
    public X_WH_Log (Properties ctx, int WH_Log_ID, String trxName)
    {
      super (ctx, WH_Log_ID, trxName);
      /** if (WH_Log_ID == 0)
        {
			setWH_Definition_ID (0);
			setWH_Log_ID (0);
			setWH_Setting_ID (0);
        } */
    }

    /** Load Constructor */
    public X_WH_Log (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_WH_Log[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Comments.
		@param Comments 
		Comments or additional information
	  */
	public void setComments (String Comments)
	{
		set_Value (COLUMNNAME_Comments, Comments);
	}

	/** Get Comments.
		@return Comments or additional information
	  */
	public String getComments () 
	{
		return (String)get_Value(COLUMNNAME_Comments);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getWH_Definition_ID()));
    }

	/** Set Withholding Log.
		@param WH_Log_ID 
		Save a log for each document processed
	  */
	public void setWH_Log_ID (int WH_Log_ID)
	{
		if (WH_Log_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_WH_Log_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_WH_Log_ID, Integer.valueOf(WH_Log_ID));
	}

	/** Get Withholding Log.
		@return Save a log for each document processed
	  */
	public int getWH_Log_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_WH_Log_ID);
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

	public org.spin.model.I_WH_Withholding getWH_Withholding() throws RuntimeException
    {
		return (org.spin.model.I_WH_Withholding)MTable.get(getCtx(), org.spin.model.I_WH_Withholding.Table_Name)
			.getPO(getWH_Withholding_ID(), get_TrxName());	}

	/** Set Withholding Allocation.
		@param WH_Withholding_ID Withholding Allocation	  */
	public void setWH_Withholding_ID (int WH_Withholding_ID)
	{
		if (WH_Withholding_ID < 1) 
			set_Value (COLUMNNAME_WH_Withholding_ID, null);
		else 
			set_Value (COLUMNNAME_WH_Withholding_ID, Integer.valueOf(WH_Withholding_ID));
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
}