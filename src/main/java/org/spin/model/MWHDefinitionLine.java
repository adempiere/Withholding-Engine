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
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.compiere.model.Query;
import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 * Withholding Setting
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class MWHDefinitionLine extends X_WH_DefinitionLine {
	
	public MWHDefinitionLine(Properties ctx, int WH_Setting_ID, String trxName) {
		super(ctx, WH_Setting_ID, trxName);
	}
	
	public MWHDefinitionLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7050562622116465459L;
	/** Static Cache */
	private static CCache<Integer, MWHDefinitionLine> definitionLineCacheIds = new CCache<Integer, MWHDefinitionLine>(Table_Name, 30);
	
	/**
	 * Get/Load Setting [CACHED]
	 * @param ctx context
	 * @param settingId
	 * @param trxName
	 * @return activity or null
	 */
	public static MWHDefinitionLine getById(Properties ctx, int settingId, String trxName)
	{
		if (settingId <= 0)
			return null;

		MWHDefinitionLine definition = definitionLineCacheIds.get(settingId);
		if (definition != null && definition.get_ID() > 0)
			return definition;

		definition = new Query(ctx , Table_Name , COLUMNNAME_WH_DefinitionLine_ID + "=?" , trxName)
				.setClient_ID()
				.setParameters(settingId)
				.first();
		if (definition != null && definition.get_ID() > 0) {
			definitionLineCacheIds.put(definition.get_ID(), definition);
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
	public static List<MWHDefinitionLine> getAll(Properties ctx, boolean resetCache, String trxName) {
		List<MWHDefinitionLine> definitionLineList;
		if (resetCache || definitionLineCacheIds.size() > 0 ) {
			definitionLineList = new Query(Env.getCtx(), Table_Name, null , trxName)
					.setClient_ID()
					.list();
			definitionLineList.stream().forEach(setting -> {
				definitionLineCacheIds.put(setting.getWH_DefinitionLine_ID(), setting);
			});
			return definitionLineList;
		}
		definitionLineList = definitionLineCacheIds.entrySet().stream()
				.map(activity -> activity.getValue())
				.collect(Collectors.toList());
		return  definitionLineList;
	}
	
	/**
	 * Clear values from local store
	 * @return void
	 */
	public static void clear() {
		definitionLineCacheIds.clear();
	}
	
	@Override
	protected boolean afterDelete(boolean success) {
		clear();
		MWHDefinition.clear();
		return super.afterDelete(success);
	}
	
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
		clear();
		MWHDefinition.clear();
		return super.afterSave(newRecord, success);
	}

	@Override
	public String toString() {
		return "MWHDefinitionLine [getC_DocType_ID()=" + getC_DocType_ID() + ", isIncludeProcessed()="
				+ isIncludeProcessed() + ", getWH_DefinitionLine_ID()=" + getWH_DefinitionLine_ID() + "]";
	}
}
