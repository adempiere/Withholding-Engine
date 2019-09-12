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

package org.spin.process;

import org.compiere.process.SvrProcess;

/** Generated Process for (Withholding Process)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.2
 */
public abstract class WithholdingProcessAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "WithHoldingProcess";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Withholding Process";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54293;
	/**	Parameter Name for Withholding Type	*/
	public static final String WH_TYPE_ID = "WH_Type_ID";
	/**	Parameter Name for Withholding Setting	*/
	public static final String WH_SETTING_ID = "WH_Setting_ID";
	/**	Parameter Name for Withholding 	*/
	public static final String WH_DEFINITION_ID = "WH_Definition_ID";
	/**	Parameter Value for Withholding Type	*/
	private int typeId;
	/**	Parameter Value for Withholding Setting	*/
	private int settingId;
	/**	Parameter Value for Withholding 	*/
	private int definitionId;

	@Override
	protected void prepare() {
		typeId = getParameterAsInt(WH_TYPE_ID);
		settingId = getParameterAsInt(WH_SETTING_ID);
		definitionId = getParameterAsInt(WH_DEFINITION_ID);
	}

	/**	 Getter Parameter Value for Withholding Type	*/
	protected int getTypeId() {
		return typeId;
	}

	/**	 Setter Parameter Value for Withholding Type	*/
	protected void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**	 Getter Parameter Value for Withholding Setting	*/
	protected int getSettingId() {
		return settingId;
	}

	/**	 Setter Parameter Value for Withholding Setting	*/
	protected void setSettingId(int settingId) {
		this.settingId = settingId;
	}

	/**	 Getter Parameter Value for Withholding 	*/
	protected int getDefinitionId() {
		return definitionId;
	}

	/**	 Setter Parameter Value for Withholding 	*/
	protected void setDefinitionId(int definitionId) {
		this.definitionId = definitionId;
	}

	/**	 Getter Parameter Value for Process ID	*/
	public static final int getProcessId() {
		return ID_FOR_PROCESS;
	}

	/**	 Getter Parameter Value for Process Value	*/
	public static final String getProcessValue() {
		return VALUE_FOR_PROCESS;
	}

	/**	 Getter Parameter Value for Process Name	*/
	public static final String getProcessName() {
		return NAME_FOR_PROCESS;
	}
}