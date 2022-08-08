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
import java.util.Properties;

import org.compiere.model.Query;
import org.compiere.util.CCache;

/**
 * Withholding Setting
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class MWHLog extends X_WH_Log {
	
	public MWHLog(Properties ctx, int WH_Log_ID, String trxName) {
		super(ctx, WH_Log_ID, trxName);
	}
	
	public MWHLog(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7050562622116465459L;
	/** Static Cache */
	private static CCache<Integer, MWHLog> logCacheIds = new CCache<Integer, MWHLog>(Table_Name, 30);
	
	
	/**
	 * Get/Load Log [CACHED]
	 * @param ctx context
	 * @param logId
	 * @param trxName
	 * @return activity or null
	 */
	public static MWHLog getById(Properties ctx, int logId, String trxName) {
		if (logId <= 0)
			return null;

		MWHLog definition = logCacheIds.get(logId);
		if (definition != null && definition.get_ID() > 0)
			return definition;

		definition = new Query(ctx , Table_Name , COLUMNNAME_WH_Log_ID + "=?" , trxName)
				.setClient_ID()
				.setParameters(logId)
				.first();
		if (definition != null && definition.get_ID() > 0)
		{
			logCacheIds.put(definition.get_ID(), definition);
		}
		return definition;
	}
}
