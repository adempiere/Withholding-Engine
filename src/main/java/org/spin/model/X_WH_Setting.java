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

/** Generated Model for WH_Setting
 *  @author Adempiere (generated) 
 *  @version Release 3.9.2 - $Id$ */
public class X_WH_Setting extends PO implements I_WH_Setting, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190817L;

    /** Standard Constructor */
    public X_WH_Setting (Properties ctx, int WH_Setting_ID, String trxName)
    {
      super (ctx, WH_Setting_ID, trxName);
      /** if (WH_Setting_ID == 0)
        {
			setEventType (null);
			setName (null);
			setSeqNo (0);
// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM WH_Setting WHERE WH_Type_ID=@WH_Type_ID@
			setValue (null);
			setWH_Setting_ID (0);
			setWH_Type_ID (0);
			setWithholdingClassName (null);
        } */
    }

    /** Load Constructor */
    public X_WH_Setting (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_WH_Setting[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.adempiere.core.domains.models.I_AD_Table getAD_Table() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_Table)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_Table.Table_Name)
			.getPO(getAD_Table_ID(), get_TrxName());	}

	/** Set Table.
		@param AD_Table_ID 
		Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1) 
			set_Value (COLUMNNAME_AD_Table_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
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

	/** EventModelValidator AD_Reference_ID=53237 */
	public static final int EVENTMODELVALIDATOR_AD_Reference_ID=53237;
	/** Table Before New = TBN */
	public static final String EVENTMODELVALIDATOR_TableBeforeNew = "TBN";
	/** Table Before Change = TBC */
	public static final String EVENTMODELVALIDATOR_TableBeforeChange = "TBC";
	/** Table Before Delete = TBD */
	public static final String EVENTMODELVALIDATOR_TableBeforeDelete = "TBD";
	/** Table After New = TAN */
	public static final String EVENTMODELVALIDATOR_TableAfterNew = "TAN";
	/** Table After Change = TAC */
	public static final String EVENTMODELVALIDATOR_TableAfterChange = "TAC";
	/** Table After Delete = TAD */
	public static final String EVENTMODELVALIDATOR_TableAfterDelete = "TAD";
	/** Document Before Prepare = DBPR */
	public static final String EVENTMODELVALIDATOR_DocumentBeforePrepare = "DBPR";
	/** Document Before Void = DBVO */
	public static final String EVENTMODELVALIDATOR_DocumentBeforeVoid = "DBVO";
	/** Document Before Close = DBCL */
	public static final String EVENTMODELVALIDATOR_DocumentBeforeClose = "DBCL";
	/** Document Before Reactivate = DBAC */
	public static final String EVENTMODELVALIDATOR_DocumentBeforeReactivate = "DBAC";
	/** Document Before Reverse Correct = DBRC */
	public static final String EVENTMODELVALIDATOR_DocumentBeforeReverseCorrect = "DBRC";
	/** Document Before Reverse Accrual = DBRA */
	public static final String EVENTMODELVALIDATOR_DocumentBeforeReverseAccrual = "DBRA";
	/** Document Before Complete = DBCO */
	public static final String EVENTMODELVALIDATOR_DocumentBeforeComplete = "DBCO";
	/** Document Before Post = DBPO */
	public static final String EVENTMODELVALIDATOR_DocumentBeforePost = "DBPO";
	/** Document After Prepare = DAPR */
	public static final String EVENTMODELVALIDATOR_DocumentAfterPrepare = "DAPR";
	/** Document After Void = DAVO */
	public static final String EVENTMODELVALIDATOR_DocumentAfterVoid = "DAVO";
	/** Document After Close = DACL */
	public static final String EVENTMODELVALIDATOR_DocumentAfterClose = "DACL";
	/** Document After Reactivate = DAAC */
	public static final String EVENTMODELVALIDATOR_DocumentAfterReactivate = "DAAC";
	/** Document After Reverse Correct = DARC */
	public static final String EVENTMODELVALIDATOR_DocumentAfterReverseCorrect = "DARC";
	/** Document After Reverse Accrual = DARA */
	public static final String EVENTMODELVALIDATOR_DocumentAfterReverseAccrual = "DARA";
	/** Document After Complete = DACO */
	public static final String EVENTMODELVALIDATOR_DocumentAfterComplete = "DACO";
	/** Document After Post = DAPO */
	public static final String EVENTMODELVALIDATOR_DocumentAfterPost = "DAPO";
	/** Table After New Replication = TANR */
	public static final String EVENTMODELVALIDATOR_TableAfterNewReplication = "TANR";
	/** Table After Change Replication = TACR */
	public static final String EVENTMODELVALIDATOR_TableAfterChangeReplication = "TACR";
	/** Table Before Delete Replication = TBDR */
	public static final String EVENTMODELVALIDATOR_TableBeforeDeleteReplication = "TBDR";
	/** Set Event Model Validator.
		@param EventModelValidator Event Model Validator	  */
	public void setEventModelValidator (String EventModelValidator)
	{

		set_Value (COLUMNNAME_EventModelValidator, EventModelValidator);
	}

	/** Get Event Model Validator.
		@return Event Model Validator	  */
	public String getEventModelValidator () 
	{
		return (String)get_Value(COLUMNNAME_EventModelValidator);
	}

	/** EventType AD_Reference_ID=54139 */
	public static final int EVENTTYPE_AD_Reference_ID=54139;
	/** Process = P */
	public static final String EVENTTYPE_Process = "P";
	/** Event = E */
	public static final String EVENTTYPE_Event = "E";
	/** Set Event Type.
		@param EventType 
		Type of Event
	  */
	public void setEventType (String EventType)
	{

		set_Value (COLUMNNAME_EventType, EventType);
	}

	/** Get Event Type.
		@return Type of Event
	  */
	public String getEventType () 
	{
		return (String)get_Value(COLUMNNAME_EventType);
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

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
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

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getValue());
    }

	/** Set Withholding Setting.
		@param WH_Setting_ID 
		specifies the setting to each applied withholding
	  */
	public void setWH_Setting_ID (int WH_Setting_ID)
	{
		if (WH_Setting_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_WH_Setting_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_WH_Setting_ID, Integer.valueOf(WH_Setting_ID));
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
			set_ValueNoCheck (COLUMNNAME_WH_Type_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_WH_Type_ID, Integer.valueOf(WH_Type_ID));
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

	/** Set Withholding Class Name.
		@param WithholdingClassName 
		Java Classname
	  */
	public void setWithholdingClassName (String WithholdingClassName)
	{
		set_Value (COLUMNNAME_WithholdingClassName, WithholdingClassName);
	}

	/** Get Withholding Class Name.
		@return Java Classname
	  */
	public String getWithholdingClassName () 
	{
		return (String)get_Value(COLUMNNAME_WithholdingClassName);
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