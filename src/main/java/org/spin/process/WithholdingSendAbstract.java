/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.                                     *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net                                                  *
 * or https://github.com/adempiere/adempiere/blob/develop/license.html        *
 *****************************************************************************/

package org.spin.process;

import java.sql.Timestamp;
import org.compiere.process.SvrProcess;

/** Generated Process for (Withholding Send)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public abstract class WithholdingSendAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "WithholdingSend (Process)";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Withholding Send";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54362;
	/**	Parameter Name for Business Partner 	*/
	public static final String C_BPARTNER_ID = "C_BPartner_ID";
	/**	Parameter Name for Withholding Type	*/
	public static final String WH_TYPE_ID = "WH_Type_ID";
	/**	Parameter Name for Sales Transaction	*/
	public static final String ISSOTRX = "IsSOTrx";
	/**	Parameter Name for Mail Template	*/
	public static final String R_MAILTEXT_ID = "R_MailText_ID";
	/**	Parameter Name for Document Date	*/
	public static final String DATEDOC = "DateDoc";
	/**	Parameter Name for Send Withholding to Notification Queue	*/
	public static final String WH_SENDDOCUMENTTOQUEUE = "WH_SendDocumentToQueue";
	/**	Parameter Value for Business Partner 	*/
	private int bPartnerId;
	/**	Parameter Value for Withholding Type	*/
	private int typeId;
	/**	Parameter Value for Sales Transaction	*/
	private String isSOTrx;
	/**	Parameter Value for Mail Template	*/
	private int mailTextId;
	/**	Parameter Value for Document Date	*/
	private Timestamp dateDoc;
	/**	Parameter Value for Document Date(To)	*/
	private Timestamp dateDocTo;
	/**	Parameter Value for Send Withholding to Notification Queue	*/
	private boolean isSendDocumentToQueue;

	@Override
	protected void prepare() {
		bPartnerId = getParameterAsInt(C_BPARTNER_ID);
		typeId = getParameterAsInt(WH_TYPE_ID);
		isSOTrx = getParameterAsString(ISSOTRX);
		mailTextId = getParameterAsInt(R_MAILTEXT_ID);
		dateDoc = getParameterAsTimestamp(DATEDOC);
		dateDocTo = getParameterToAsTimestamp(DATEDOC);
		isSendDocumentToQueue = getParameterAsBoolean(WH_SENDDOCUMENTTOQUEUE);
	}

	/**	 Getter Parameter Value for Business Partner 	*/
	protected int getBPartnerId() {
		return bPartnerId;
	}

	/**	 Setter Parameter Value for Business Partner 	*/
	protected void setBPartnerId(int bPartnerId) {
		this.bPartnerId = bPartnerId;
	}

	/**	 Getter Parameter Value for Withholding Type	*/
	protected int getTypeId() {
		return typeId;
	}

	/**	 Setter Parameter Value for Withholding Type	*/
	protected void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**	 Getter Parameter Value for Sales Transaction	*/
	protected String getIsSOTrx() {
		return isSOTrx;
	}

	/**	 Setter Parameter Value for Sales Transaction	*/
	protected void setIsSOTrx(String isSOTrx) {
		this.isSOTrx = isSOTrx;
	}

	/**	 Getter Parameter Value for Mail Template	*/
	protected int getMailTextId() {
		return mailTextId;
	}

	/**	 Setter Parameter Value for Mail Template	*/
	protected void setMailTextId(int mailTextId) {
		this.mailTextId = mailTextId;
	}

	/**	 Getter Parameter Value for Document Date	*/
	protected Timestamp getDateDoc() {
		return dateDoc;
	}

	/**	 Setter Parameter Value for Document Date	*/
	protected void setDateDoc(Timestamp dateDoc) {
		this.dateDoc = dateDoc;
	}

	/**	 Getter Parameter Value for Document Date(To)	*/
	protected Timestamp getDateDocTo() {
		return dateDocTo;
	}

	/**	 Setter Parameter Value for Document Date(To)	*/
	protected void setDateDocTo(Timestamp dateDocTo) {
		this.dateDocTo = dateDocTo;
	}

	/**	 Getter Parameter Value for Send Withholding to Notification Queue	*/
	protected boolean isSendDocumentToQueue() {
		return isSendDocumentToQueue;
	}

	/**	 Setter Parameter Value for Send Withholding to Notification Queue	*/
	protected void setSendDocumentToQueue(boolean isSendDocumentToQueue) {
		this.isSendDocumentToQueue = isSendDocumentToQueue;
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