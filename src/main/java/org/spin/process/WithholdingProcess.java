/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
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
 * Contributor(s): Carlos Parada www.erpya.com                                *
 *****************************************************************************/
package org.spin.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.adempiere.model.MView;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.PO;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.spin.model.MWHDefinition;
import org.spin.model.MWHSetting;
import org.spin.model.MWHType;
import org.spin.util.WithholdingEngine;


/** Generated Process for (Withholding Process)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.2
 */
public class WithholdingProcess extends WithholdingProcessAbstract
{
	
	private MWHSetting mSetting = null;
	private MWHDefinition mDefinition = null;
	private HashMap<String, Object> params = new HashMap<>();
	
	@Override
	protected void prepare()
	{
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception
	{
		String result = "";
		
		//Save parameters
		for (ProcessInfoParameter param : getParameter()) 
			params.put(param.getParameterName(), param.getParameter());
		
		//Process withholding
		processWithholding();
		
		return result;
	}
	
	/**
	 * Process Withholding
	 * @return
	 */
	private String processWithholding() {
		
		MWHType mType = null;
		AtomicReference<String> result = new AtomicReference<>();
		result.set("");
		
		if (getTypeId()!=0)
			mType = new MWHType(getCtx(), getTypeId(), get_TrxName());
		if (getSettingId()!=0)
			mSetting = new MWHSetting(getCtx(), getSettingId(), get_TrxName());
		if (getDefinitionId()!=0)
			mDefinition = new MWHDefinition(getCtx(), getDefinitionId(), get_TrxName());

		if (getRecord_ID()!=0
				&& getTable_ID()!=0) 
			result.set(processDocument(mType, getTable_ID(), getRecord_ID()));
		else {
			if (mType!=null
					&& mType.get_ID() > 0) 
				processWithHoldingType(mType);
			else {
				MWHType.getAll(getCtx(), true, get_TrxName())
							.forEach(type ->{
								result.set(processWithHoldingType(type));
							});
			}
		}

		return result.get();
	}
	
	/**
	 * Process by Withholding Type
	 * @param type
	 * @return
	 */
	private String processWithHoldingType(MWHType type) {
		String result = "";
		if (type !=null 
				&& type.getAD_View_ID()!=0) {
			String sql = MView.getSQLFromView(type.getAD_View_ID(), get_TrxName());
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				st = DB.prepareStatement(sql, get_TrxName());
				rs = st.executeQuery();
				while (rs.next()) {
					Integer AD_Table_ID = rs.getInt("AD_Table_ID");
					Integer Record_ID = rs.getInt("Record_ID");
					result = processDocument(type, AD_Table_ID, Record_ID);
				}
			}catch (SQLException e) {
				result = e.getMessage();
			}
			finally{
				DB.close(rs, st);
				st = null; rs = null;
			}
		}
		return result;
	}
	
	/**
	 * Process Document
	 * @param type
	 * @param AD_Table_ID
	 * @param Record_ID
	 * @return
	 */
	private String processDocument(MWHType type, int AD_Table_ID, int Record_ID) {
		String result = "";
		PO document = null;
		if (AD_Table_ID != 0
				&& Record_ID!= 0) {
			
			if (AD_Table_ID==MInvoice.Table_ID) 
				document = new MInvoice(getCtx(), Record_ID, get_TrxName());
			else if (AD_Table_ID==MOrder.Table_ID)
				document = new MOrder(getCtx(), Record_ID, get_TrxName());
			else 
				return "";
			
			result = WithholdingEngine.get().fireProcess(document, type, mSetting, mDefinition, params);
		}
		
		return result;
	}
	
}