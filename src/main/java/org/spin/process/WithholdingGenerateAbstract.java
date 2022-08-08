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

import java.sql.Timestamp;
import org.compiere.process.SvrProcess;

/** Generated Process for (Withholding Generate)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.3
 */
public abstract class WithholdingGenerateAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "WithholdingGenerate";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Withholding Generate";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54243;
	/**	Parameter Name for Organization	*/
	public static final String AD_ORG_ID = "AD_Org_ID";
	/**	Parameter Name for Business Partner 	*/
	public static final String C_BPARTNER_ID = "C_BPartner_ID";
	/**	Parameter Name for Currency	*/
	public static final String C_CURRENCY_ID = "C_Currency_ID";
	/**	Parameter Name for Invoice	*/
	public static final String C_INVOICE_ID = "C_Invoice_ID";
	/**	Parameter Name for Account Date	*/
	public static final String DATEACCT = "DateAcct";
	/**	Parameter Name for Withholding Type	*/
	public static final String WH_TYPE_ID = "WH_Type_ID";
	/**	Parameter Name for Document Date	*/
	public static final String DATEDOC = "DateDoc";
	/**	Parameter Name for Currency To	*/
	public static final String C_CURRENCY_ID_TO = "C_Currency_ID_To";
	/**	Parameter Name for Manual	*/
	public static final String ISMANUAL = "IsManual";
	/**	Parameter Name for Document No	*/
	public static final String DOCUMENTNO = "DocumentNo";
	/**	Parameter Value for Organization	*/
	private int orgId;
	/**	Parameter Value for Business Partner 	*/
	private int bPartnerId;
	/**	Parameter Value for Currency	*/
	private int currencyId;
	/**	Parameter Value for Invoice	*/
	private int invoiceId;
	/**	Parameter Value for Account Date	*/
	private Timestamp dateAcct;
	/**	Parameter Value for Withholding Type	*/
	private int typeId;
	/**	Parameter Value for Document Date	*/
	private Timestamp dateDoc;
	/**	Parameter Value for Currency To	*/
	private int currencyToId;
	/**	Parameter Value for Manual	*/
	private boolean isManual;
	/**	Parameter Value for Document No	*/
	private String documentNo;

	@Override
	protected void prepare() {
		orgId = getParameterAsInt(AD_ORG_ID);
		bPartnerId = getParameterAsInt(C_BPARTNER_ID);
		currencyId = getParameterAsInt(C_CURRENCY_ID);
		invoiceId = getParameterAsInt(C_INVOICE_ID);
		dateAcct = getParameterAsTimestamp(DATEACCT);
		typeId = getParameterAsInt(WH_TYPE_ID);
		dateDoc = getParameterAsTimestamp(DATEDOC);
		currencyToId = getParameterAsInt(C_CURRENCY_ID_TO);
		isManual = getParameterAsBoolean(ISMANUAL);
		documentNo = getParameterAsString(DOCUMENTNO);
	}

	/**	 Getter Parameter Value for Organization	*/
	protected int getOrgId() {
		return orgId;
	}

	/**	 Setter Parameter Value for Organization	*/
	protected void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	/**	 Getter Parameter Value for Business Partner 	*/
	protected int getBPartnerId() {
		return bPartnerId;
	}

	/**	 Setter Parameter Value for Business Partner 	*/
	protected void setBPartnerId(int bPartnerId) {
		this.bPartnerId = bPartnerId;
	}

	/**	 Getter Parameter Value for Currency	*/
	protected int getCurrencyId() {
		return currencyId;
	}

	/**	 Setter Parameter Value for Currency	*/
	protected void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	/**	 Getter Parameter Value for Invoice	*/
	protected int getInvoiceId() {
		return invoiceId;
	}

	/**	 Setter Parameter Value for Invoice	*/
	protected void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**	 Getter Parameter Value for Account Date	*/
	protected Timestamp getDateAcct() {
		return dateAcct;
	}

	/**	 Setter Parameter Value for Account Date	*/
	protected void setDateAcct(Timestamp dateAcct) {
		this.dateAcct = dateAcct;
	}

	/**	 Getter Parameter Value for Withholding Type	*/
	protected int getTypeId() {
		return typeId;
	}

	/**	 Setter Parameter Value for Withholding Type	*/
	protected void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**	 Getter Parameter Value for Document Date	*/
	protected Timestamp getDateDoc() {
		return dateDoc;
	}

	/**	 Setter Parameter Value for Document Date	*/
	protected void setDateDoc(Timestamp dateDoc) {
		this.dateDoc = dateDoc;
	}

	/**	 Getter Parameter Value for Currency To	*/
	protected int getCurrencyToId() {
		return currencyToId;
	}

	/**	 Setter Parameter Value for Currency To	*/
	protected void setCurrencyToId(int currencyToId) {
		this.currencyToId = currencyToId;
	}

	/**	 Getter Parameter Value for Manual	*/
	protected boolean isManual() {
		return isManual;
	}

	/**	 Setter Parameter Value for Manual	*/
	protected void setIsManual(boolean isManual) {
		this.isManual = isManual;
	}

	/**	 Getter Parameter Value for Document No	*/
	protected String getDocumentNo() {
		return documentNo;
	}

	/**	 Setter Parameter Value for Document No	*/
	protected void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
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