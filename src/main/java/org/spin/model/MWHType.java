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
 * Withholding Type
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class MWHType extends X_WH_Type {
	
	public MWHType(Properties ctx, int WH_Type_ID, String trxName) {
		super(ctx, WH_Type_ID, trxName);
	}
	
	public MWHType(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7050562622116465459L;
	/** Static Cache */
	private static CCache<Integer, MWHType> typeCacheIds = new CCache<Integer, MWHType>(Table_Name, 30);
	/** Static Cache */
	private static CCache<String, MWHType> typeCacheValues = new CCache<String, MWHType>(Table_Name, 30);
	/** Static Cache */
	private static CCache<String, List<MWHSetting>> wihholdingSettingCacheValues = new CCache<String, List<MWHSetting>>(Table_Name, 30);
	/**	Setting List	*/
	private List<MWHSetting> settingList = null;
	
	
	/**
	 * Get/Load Setting [CACHED]
	 * @param ctx context
	 * @param typeId
	 * @param trxName
	 * @return activity or null
	 */
	public static MWHType getById(Properties ctx, int typeId, String trxName) {
		if (typeId <= 0)
			return null;

		MWHType type = typeCacheIds.get(typeId);
		if (type != null && type.get_ID() > 0)
			return type;

		type = new Query(ctx , Table_Name , COLUMNNAME_WH_Type_ID + "=?" , trxName)
				.setClient_ID()
				.setParameters(typeId)
				.first();
		if (type != null && type.get_ID() > 0)
		{
			int clientId = Env.getAD_Client_ID(ctx);
			String key = clientId + "#" + type.getValue();
			typeCacheValues.put(key, type);
			typeCacheIds.put(type.get_ID(), type);
		}
		return type;
	}

	/**
	 * get Setting By Value [CACHED]
	 * @param ctx
	 * @param typeValue
	 * @param trxName
	 * @return
	 */
	public static MWHType getByValue(Properties ctx, String typeValue, String trxName) {
		if (typeValue == null)
			return null;
		if (typeCacheValues.size() == 0 )
			getAll(ctx, true, trxName);

		int clientId = Env.getAD_Client_ID(ctx);
		String key = clientId + "#" + typeValue;
		MWHType type = typeCacheValues.get(key);
		if (type != null && type.get_ID() > 0 )
			return type;

		type =  new Query(ctx, Table_Name , COLUMNNAME_Value +  "=?", trxName)
				.setClient_ID()
				.setParameters(typeValue)
				.first();

		if (type != null && type.get_ID() > 0) {
			typeCacheValues.put(key, type);
			typeCacheIds.put(type.get_ID() , type);
		}
		return type;
	}

	/**
	 * Get All Setting
	 * @param ctx
	 * @param resetCache
	 * @param trxName
	 * @return
	 */
	public static List<MWHType> getAll(Properties ctx, boolean resetCache, String trxName) {
		List<MWHType> typeList;
		if (resetCache || typeCacheIds.size() > 0 ) {
			typeList = new Query(Env.getCtx(), Table_Name, null , trxName)
					.setClient_ID()
					.setOrderBy(COLUMNNAME_Name)
					.list();
			typeList.stream().forEach(setting -> {
				int clientId = Env.getAD_Client_ID(ctx);
				String key = clientId + "#" + setting.getValue();
				typeCacheIds.put(setting.getWH_Type_ID(), setting);
				typeCacheValues.put(key, setting);
			});
			return typeList;
		}
		typeList = typeCacheIds.entrySet().stream()
				.map(type -> type.getValue())
				.collect(Collectors.toList());
		return  typeList;
	}
	
	/**
	 * Get Setting list from Withholding type
	 * @return
	 */
	public List<MWHSetting> getSettingList() {
		if(settingList == null) {
			settingList = new Query(getCtx(), I_WH_Setting.Table_Name, I_WH_Setting.COLUMNNAME_WH_Type_ID + " = ?", get_TrxName())
					.setParameters(getWH_Type_ID())
					.setOnlyActiveRecords(true)
					.setClient_ID()
					.list();
		}
		//	Return list
		return settingList;
	}

	/**
	 * Get for all
	 * @param eventType
	 * @param tableId
	 * @param eventModelValidator
	 * @return
	 */
	private List<MWHSetting> getApplicability(String eventType, int tableId, String eventModelValidator) {
		String key = getWH_Type_ID() + "|" + eventType + "|" + tableId + "|" + (Util.isEmpty(eventModelValidator)? "": eventModelValidator);
		List<MWHSetting> applicabilityList = wihholdingSettingCacheValues.get(key);
		if(applicabilityList != null) {
			return applicabilityList;
		}
		//	
		StringBuffer whereClause = new StringBuffer(I_WH_Setting.COLUMNNAME_WH_Type_ID + " = ?");
		ArrayList<Object> params =  new ArrayList<Object>();
		//	
		whereClause.append(" AND ").append(I_WH_Setting.COLUMNNAME_EventType + " = ?");
		params.add(getWH_Type_ID());
		params.add(eventType);
		//	For Table
		if(tableId > 0) {
			whereClause.append(" AND ").append(I_WH_Setting.COLUMNNAME_AD_Table_ID + " = ?");
			params.add(tableId);
		}
		//	For Event Model Validator
		if(tableId > 0) {
			whereClause.append(" AND ").append(I_WH_Setting.COLUMNNAME_EventModelValidator + " = ?");
			params.add(eventModelValidator);
		}
		//	
		applicabilityList = new Query(getCtx(), I_WH_Setting.Table_Name, whereClause.toString(), get_TrxName())
				.setClient_ID()
				.setParameters(params)
				.setOnlyActiveRecords(true)
				.<MWHSetting>list();
		//	Set
		wihholdingSettingCacheValues.put(key, applicabilityList);
		//	Return 
		return applicabilityList;
	}
	
	/**
	 * Get Applicability without table
	 * @param eventType
	 * @return
	 */
	public List<MWHSetting> getApplicability(String eventType) {
		return getApplicability(eventType, 0, null);
	}

	/**
	 * Get from Table and event model validator
	 * @param tableName
	 * @param eventModelValidator
	 * @return
	 */
	public List<MWHSetting> getApplicability(String tableName, String eventModelValidator) {
		if(Util.isEmpty(tableName)) {
			return new ArrayList<MWHSetting>();
		}
		//	
		MTable table = MTable.get(getCtx(), tableName);
		return getApplicability(MWHSetting.EVENTTYPE_Event, table.getAD_Table_ID(), eventModelValidator);
	}

	@Override
	public String toString() {
		return "MWHType [getName()=" + getName() + ", getValue()=" + getValue() + ", getWH_Type_ID()=" + getWH_Type_ID()
				+ "]";
	}
}
