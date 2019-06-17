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
 * Copyright (C) 2003-2015 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.util.impexp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.sax.TransformerHandler;

import org.adempiere.pipo.PackOut;
import org.adempiere.pipo.handler.GenericPOHandler;
import org.compiere.model.I_C_Currency;
import org.compiere.model.I_GL_Category;
import org.compiere.model.Query;
import org.spin.model.I_WH_Definition;
import org.spin.model.I_WH_DefinitionLine;
import org.spin.model.I_WH_Setting;
import org.spin.model.I_WH_Type;
import org.spin.model.MWHDefinition;
import org.spin.model.MWHDefinitionLine;
import org.spin.model.MWHSetting;
import org.spin.model.MWHType;
import org.xml.sax.SAXException;

/**
 * Withholding Exporter
 * @author Yamel Senih, ySenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class WithholdingExporter extends GenericPOHandler {
	@Override
	public void create(Properties ctx, TransformerHandler document) throws SAXException {
		PackOut packOut = (PackOut) ctx.get("PackOutProcess");
		if(packOut == null ) {
			packOut = new PackOut();
			packOut.setLocalContext(ctx);
		}
		//	add here exclusion tables
		List<String> parentsToExclude = new ArrayList<String>();
		parentsToExclude.add(I_C_Currency.Table_Name);
		parentsToExclude.add(I_GL_Category.Table_Name);
		parentsToExclude.add(I_C_Currency.Table_Name);
		//	Export Withholding Tax Setup
		List<MWHType> withholdingTypeList = new Query(ctx, I_WH_Type.Table_Name, null, null)
				.setOnlyActiveRecords(true)
				.setClient_ID()
				.setOrderBy(I_WH_Type.COLUMNNAME_Value)
				.list();
		//	Export Withholding Type
		for(MWHType withholdingType : withholdingTypeList) {
			if(withholdingType.getWH_Type_ID() < PackOut.MAX_OFFICIAL_ID) {
				continue;
			}
			withholdingType.setAD_Org_ID(0);
			packOut.createGenericPO(document, withholdingType, true, parentsToExclude);
			//	Export Setting
			List<MWHSetting> withholdingSettingList = new Query(ctx, I_WH_Setting.Table_Name, I_WH_Setting.COLUMNNAME_WH_Type_ID + " = ?", null)
					.setParameters(withholdingType.getWH_Type_ID())
					.setOnlyActiveRecords(true)
					.setClient_ID()
					.setOrderBy(I_WH_Setting.COLUMNNAME_SeqNo)
					.list();
			//	Export
			for(MWHSetting withholdingSetting : withholdingSettingList) {
				if(withholdingSetting.getWH_Setting_ID() < PackOut.MAX_OFFICIAL_ID) {
					continue;
				}
				withholdingSetting.setAD_Org_ID(0);
				packOut.createGenericPO(document, withholdingSetting, true, parentsToExclude);
			}
			//	Export Withholding
			List<MWHDefinition> withholdingList = new Query(ctx, I_WH_Definition.Table_Name, I_WH_Definition.COLUMNNAME_WH_Type_ID + " = ?", null)
					.setParameters(withholdingType.getWH_Type_ID())
					.setOnlyActiveRecords(true)
					.setClient_ID()
					.setOrderBy(I_WH_Definition.COLUMNNAME_Name)
					.list();
			//	Export
			for(MWHDefinition withholding : withholdingList) {
				if(withholding.getWH_Definition_ID() < PackOut.MAX_OFFICIAL_ID) {
					continue;
				}
				withholding.setAD_Org_ID(0);
				packOut.createGenericPO(document, withholding, true, parentsToExclude);
				//	Export Withholding
				List<MWHDefinitionLine> withholdingLineList = new Query(ctx, I_WH_DefinitionLine.Table_Name, I_WH_DefinitionLine.COLUMNNAME_WH_Definition_ID + " = ?", null)
						.setParameters(withholding.getWH_Definition_ID())
						.setOnlyActiveRecords(true)
						.setClient_ID()
						.list();
				//	Export
				for(MWHDefinitionLine withholdingLine : withholdingLineList) {
					if(withholdingLine.getWH_DefinitionLine_ID() < PackOut.MAX_OFFICIAL_ID) {
						continue;
					}
					withholdingLine.setAD_Org_ID(0);
					//	Remove default bank account
					packOut.createGenericPO(document, withholdingLine, true, parentsToExclude);
				}
			}			
		}
	}
}
