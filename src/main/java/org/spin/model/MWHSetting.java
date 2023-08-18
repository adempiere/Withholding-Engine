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

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.compiere.model.Query;
import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.spin.util.AbstractWithholdingSetting;
import org.spin.util.GenericWithholdingSetting;

/**
 * Withholding Setting
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class MWHSetting extends X_WH_Setting {
	
	public MWHSetting(Properties ctx, int WH_Setting_ID, String trxName) {
		super(ctx, WH_Setting_ID, trxName);
	}
	
	public MWHSetting(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7050562622116465459L;
	/** Static Cache */
	private static CCache<Integer, MWHSetting> settingCacheIds = new CCache<Integer, MWHSetting>(Table_Name, 30);
	/** Static Cache */
	private static CCache<String, MWHSetting> settingCacheValues = new CCache<String, MWHSetting>(Table_Name, 30);
	
	/**
	 * Get/Load Setting [CACHED]
	 * @param ctx context
	 * @param settingId
	 * @param trxName
	 * @return activity or null
	 */
	public static MWHSetting getById(Properties ctx, int settingId, String trxName)
	{
		if (settingId <= 0)
			return null;

		MWHSetting setting = settingCacheIds.get(settingId);
		if (setting != null && setting.get_ID() > 0)
			return setting;

		setting = new Query(ctx , Table_Name , COLUMNNAME_WH_Setting_ID + "=?" , trxName)
				.setClient_ID()
				.setParameters(settingId)
				.first();
		if (setting != null && setting.get_ID() > 0)
		{
			int clientId = Env.getAD_Client_ID(ctx);
			String key = clientId + "#" + setting.getValue();
			settingCacheValues.put(key, setting);
			settingCacheIds.put(setting.get_ID(), setting);
		}
		return setting;
	}

	/**
	 * get Setting By Value [CACHED]
	 * @param ctx
	 * @param settingValue
	 * @param trxName
	 * @return
	 */
	public static MWHSetting getByValue(Properties ctx, String settingValue, String trxName)
	{
		if (settingValue == null)
			return null;
		if (settingCacheValues.size() == 0 )
			getAll(ctx, true, trxName);

		int clientId = Env.getAD_Client_ID(ctx);
		String key = clientId + "#" + settingValue;
		MWHSetting setting = settingCacheValues.get(key);
		if (setting != null && setting.get_ID() > 0 )
			return setting;

		setting =  new Query(ctx, Table_Name , COLUMNNAME_Value +  "=?", trxName)
				.setClient_ID()
				.setParameters(settingValue)
				.first();

		if (setting != null && setting.get_ID() > 0) {
			settingCacheValues.put(key, setting);
			settingCacheIds.put(setting.get_ID() , setting);
		}
		return setting;
	}

	/**
	 * Get All Setting
	 * @param ctx
	 * @param resetCache
	 * @param trxName
	 * @return
	 */
	public static List<MWHSetting> getAll(Properties ctx, boolean resetCache, String trxName) {
		List<MWHSetting> settingList;
		if (resetCache || settingCacheIds.size() > 0 ) {
			settingList = new Query(Env.getCtx(), Table_Name, null , trxName)
					.setClient_ID()
					.setOrderBy(COLUMNNAME_Name)
					.list();
			settingList.stream().forEach(setting -> {
				int clientId = Env.getAD_Client_ID(ctx);
				String key = clientId + "#" + setting.getValue();
				settingCacheIds.put(setting.getWH_Setting_ID(), setting);
				settingCacheValues.put(key, setting);
			});
			return settingList;
		}
		settingList = settingCacheIds.entrySet().stream()
				.map(setting -> setting.getValue())
				.collect(Collectors.toList());
		return  settingList;
	}
	
	/**
	 * Get Class from device type, used for handler
	 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
	 * @return
	 * @return Class<?>
	 */
	private Class<?> getHandlerClass() {
		String className = getWithholdingClassName();
		//	Validate null values
		if(Util.isEmpty(className)) {
			return null;
		}
		try {
			Class<?> clazz = Class.forName(className);
			//	Make sure that it is a PO class
			Class<?> superClazz = clazz.getSuperclass();
			//	Validate super class
			while (superClazz != null) {
				if (superClazz == AbstractWithholdingSetting.class) {
					log.fine("Use: " + className);
					return clazz;
				}
				//	Get Super Class
				superClazz = superClazz.getSuperclass();
			}
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		//	
		log.finest("Not found: " + className);
		return null;
	}	//	getHandlerClass
	
	/**
	 * Get Report export instance
	 * @return
	 */
	public AbstractWithholdingSetting getSettingInstance() throws Exception {
		//	Load it
		//	Get class from parent
		Class<?> clazz = getHandlerClass();
		//	Not yet implemented
		if (clazz == null) {
			log.log(Level.INFO, "Using Standard Functional Setting");
			return new GenericWithholdingSetting(this);
		}
		//	
		Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[]{MWHSetting.class});
		//	new instance
		return (AbstractWithholdingSetting) constructor.newInstance(new Object[] {this});
	}

	@Override
	public String toString() {
		return "MWHSetting [getAD_Table_ID()=" + getAD_Table_ID() + ", getEventModelValidator()="
				+ getEventModelValidator() + ", getEventType()=" + getEventType() + ", getName()=" + getName()
				+ ", getSeqNo()=" + getSeqNo() + ", getValue()=" + getValue() + ", getWH_Setting_ID()="
				+ getWH_Setting_ID() + "]";
	}
}
