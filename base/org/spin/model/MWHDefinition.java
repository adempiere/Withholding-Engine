/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 or later of the                                  *
 * GNU General Public License as published                                    *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2016 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.Util;

/**
 * Withholding Setting
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class MWHDefinition extends X_WH_Definition {
	
	public MWHDefinition(Properties ctx, int WH_Setting_ID, String trxName) {
		super(ctx, WH_Setting_ID, trxName);
	}
	
	public MWHDefinition(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7050562622116465459L;
	/** Static Cache */
	private static CCache<Integer, MWHDefinition> definitionCacheIds = new CCache<Integer, MWHDefinition>(Table_Name, 30);
	/** Static Cache */
	private static CCache<String, List<MWHDefinition>> wihholdingDefinitionFromDocumentTypeCacheValues = new CCache<String, List<MWHDefinition>>(Table_Name + "_DocumentType", 30);
	/** Static Cache  for tables */
	private static CCache<String, List<MWHDefinition>> wihholdingDefinitionFromTableCacheValues = new CCache<String, List<MWHDefinition>>(Table_Name + "_Table", 30);
	/**	Type
	
	
	/**
	 * Get/Load Setting [CACHED]
	 * @param ctx context
	 * @param definitionId
	 * @param trxName
	 * @return activity or null
	 */
	public static MWHDefinition getById(Properties ctx, int definitionId, String trxName)
	{
		if (definitionId <= 0)
			return null;

		MWHDefinition definition = definitionCacheIds.get(definitionId);
		if (definition != null && definition.get_ID() > 0)
			return definition;

		definition = new Query(ctx , Table_Name , COLUMNNAME_WH_Definition_ID + "=?" , trxName)
				.setClient_ID()
				.setParameters(definitionId)
				.first();
		if (definition != null && definition.get_ID() > 0)
		{
			definitionCacheIds.put(definition.get_ID(), definition);
		}
		return definition;
	}

	/**
	 * Get All Setting
	 * @param ctx
	 * @param resetCache
	 * @param trxName
	 * @return
	 */
	public static List<MWHDefinition> getAll(Properties ctx, boolean resetCache, String trxName) {
		List<MWHDefinition> definitionList;
		if (resetCache || definitionCacheIds.size() > 0 ) {
			definitionList = new Query(Env.getCtx(), Table_Name, null , trxName)
					.setClient_ID()
					.setOrderBy(COLUMNNAME_Name)
					.list();
			definitionList.stream().forEach(setting -> {
				definitionCacheIds.put(setting.getWH_Definition_ID(), setting);
			});
			return definitionList;
		}
		definitionList = definitionCacheIds.entrySet().stream()
				.map(activity -> activity.getValue())
				.collect(Collectors.toList());
		return  definitionList;
	}
	
	/**
	 * Get from document type
	 * @param ctx
	 * @param documentTypeId
	 * @return
	 */
	public static List<MWHDefinition> getFromDocumentType(Properties ctx, int documentTypeId) {
		return getFromDocumentType(ctx, documentTypeId, 0);
	}
		
	/**
	 * Get from document type
	 * @param ctx
	 * @param documentTypeId
	 * @param organizationId
	 * @return
	 */
	public static List<MWHDefinition> getFromDocumentType(Properties ctx, int documentTypeId, int organizationId) {
		String key = documentTypeId + "_" + organizationId;
		List<MWHDefinition> definitionList = wihholdingDefinitionFromDocumentTypeCacheValues.get(key);
		if(definitionList != null) {
			return definitionList;
		}
		//	
		String whereClause = new String("EXISTS(SELECT 1 FROM WH_DefinitionLine "
				+ "WHERE C_DocType_ID = ? "
				+ " AND AD_Org_ID IN (0,?) "
				+ "AND WH_Definition_ID = WH_Definition.WH_Definition_ID)");
		//	
		definitionList = new Query(ctx, I_WH_Definition.Table_Name, whereClause, null)
				.setClient_ID()
				.setParameters(documentTypeId, organizationId)
				.setOnlyActiveRecords(true)
				.<MWHDefinition>list();
		//	Set
		wihholdingDefinitionFromDocumentTypeCacheValues.put(key, definitionList);
		//	Return 
		return definitionList;
	}
	
	/**
	 * Get from table
	 * @param ctx
	 * @param tableName
	 * @param organizationId
	 * @return
	 */
	public static List<MWHDefinition> getFromTable(Properties ctx, String tableName) {
		List<MWHDefinition> definitionList = wihholdingDefinitionFromTableCacheValues.get(tableName);
		if(definitionList != null) {
			return definitionList;
		}
		//	
		MTable table = MTable.get(ctx, tableName);
		String whereClause = new String("EXISTS(SELECT 1 FROM WH_Setting s "
				+ "WHERE s.AD_Table_ID = ? "
				+ "AND s.WH_Type_ID = WH_Definition.WH_Type_ID)");
		//	
		definitionList = new Query(ctx, I_WH_Definition.Table_Name, whereClause, null)
				.setClient_ID()
				.setParameters(table.getAD_Table_ID())
				.setOnlyActiveRecords(true)
				.<MWHDefinition>list();
		//	Set
		wihholdingDefinitionFromTableCacheValues.put(tableName, definitionList);
		//	Return 
		return definitionList;
	}
	
	/**
	 * Clear values from local store
	 * @return void
	 */
	public static void clear() {
		definitionCacheIds.clear();
		wihholdingDefinitionFromDocumentTypeCacheValues.clear();
	}
	
	/**
	 * Get Applicability without table
	 * @param eventType
	 * @return
	 */
	public List<MWHSetting> getApplicability(String eventType) {
		MWHType withholdingType = MWHType.getById(getCtx(), getWH_Type_ID(), get_TrxName());
		return withholdingType.getApplicability(eventType);
	}

	/**
	 * Get from Table and event model validator
	 * @param tableName
	 * @param eventModelValidator
	 * @return
	 */
	public List<MWHSetting> getSettingList(String tableName, String eventModelValidator) {
		if(Util.isEmpty(tableName)) {
			return new ArrayList<MWHSetting>();
		}
		MWHType withholdingType = MWHType.getById(getCtx(), getWH_Type_ID(), get_TrxName());
		return withholdingType.getApplicability(tableName, eventModelValidator);
	}

	/**
	 * Get from event type
	 * @param eventType
	 * @return
	 */
	public List<MWHSetting> getSettingList(String eventType) {
		MWHType withholdingType = MWHType.getById(getCtx(), getWH_Type_ID(), get_TrxName());
		return withholdingType.getApplicability(eventType);
	}
	
	@Override
	protected boolean afterDelete(boolean success) {
		clear();
		MWHDefinitionLine.clear();
		return super.afterDelete(success);
	}
	
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		clear();
		MWHDefinitionLine.clear();
		return super.afterSave(newRecord, success);
	}
	
	@Override
	public String toString() {
		return "MWHDefinition [getC_Charge_ID()=" + getC_Charge_ID()
				+ ", getName()=" + getName() + ", getWH_Definition_ID()=" + getWH_Definition_ID() + "]";
	}
}
