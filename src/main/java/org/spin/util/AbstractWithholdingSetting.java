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
 * Copyright (C) 2003-2014 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.spin.model.MWHDefinition;
import org.spin.model.MWHLog;
import org.spin.model.MWHSetting;
import org.spin.model.MWHWithholding;

/**
 * Abstract class for handle all withholding document
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public abstract class AbstractWithholdingSetting {
	
	
	public AbstractWithholdingSetting(MWHSetting setting) {
		this.setting = setting;
		this.context = setting.getCtx();
		this.baseAmount = Env.ZERO;
		this.withholdingRate = Env.ZERO;
		this.withholdingAmount = Env.ZERO;
	}
	
	/**	Setting	*/
	private MWHSetting setting;
	/**	Withholding	*/
	private MWHDefinition withholdingDefinition;
	/**	Value Parameters	*/
	private HashMap<String, Object> parameters = new HashMap<String, Object>();
	/**	Return Value */
	private HashMap<String, Object> returnValues = new HashMap<String, Object>();
	/**	Context	*/
	private Properties context;
	/**	Transaction Name	*/
	private String transactionName;
	/**	Process Message	*/
	private StringBuffer processLog = new StringBuffer();
	/**	Process Description	*/
	private StringBuffer processDescription = new StringBuffer();
	/**	Base Amount	*/
	private BigDecimal baseAmount;
	/**	Withholding Amount	*/
	private BigDecimal withholdingAmount;
	/**	Withholding Rate	*/
	private BigDecimal withholdingRate;
	/**	Document	*/
	private PO document;
	/**	Logger							*/
	protected CLogger	log = CLogger.getCLogger (getClass());
	/**
	 * Get Context
	 * @return
	 */
	public Properties getContext() {
		return context;
	}
	
	/**
	 * Set context
	 * @param context
	 */
	private void setContext(Properties context) {
		this.context = context;
	}
	
	/**
	 * Set document
	 * @param document
	 */
	public void setDocument(PO document) {
		this.document = document;
		setTransactionName(document.get_TrxName());
		setContext(document.getCtx());
		clearValues();
	}
	
	/**
	 * Get Document
	 * @return
	 */
	public PO getDocument() {
		return document;
	}
	
	/**
	 * Set Transaction Name
	 * @param transactionName
	 */
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
	
	/**
	 * Get Transaction Name for this process
	 * @return
	 */
	public String getTransactionName() {
		return transactionName;
	}
	
	/**
	 * Add Message for document
	 * @param message
	 */
	protected void addLog(String message) {
		if(processLog.length() > 0) {
			processLog.append(Env.NL);
		}
		processLog.append("- ").append(message);
	}
	
	/**
	 * Get Process Message
	 * @return
	 */
	public String getProcessLog() {
		if(processLog.length() > 0) {
			return processLog.toString();
		}
		//	Default nothing
		return null;
	}
	
	/**
	 * Add description for document
	 * @param description
	 */
	protected void addDescription(String description) {
		if(processDescription.length() > 0) {
			processDescription.append(Env.NL);
		}
		processDescription.append(description);
	}
	
	/**
	 * Get Process Message
	 * @return
	 */
	public String getProcessDescription() {
		if(processDescription.length() > 0) {
			return processDescription.toString();
		}
		//	Default nothing
		return null;
	}
	
	/**
	 * Set Withholding Definition
	 * @param withholdingDefinition
	 */
	public void setWithholdingDefinition(MWHDefinition withholdingDefinition) {
		this.withholdingDefinition = withholdingDefinition;
	}
	
	/**
	 * Get Functional Setting Applicability
	 * @return
	 */
	public MWHDefinition getDefinition() {
		return withholdingDefinition;
	}
	
	/**
	 * Get Functional Setting
	 * @return
	 */
	public MWHSetting getSetting() {
		return setting;
	}
	
	/**
	 * Set Parameter Value
	 * @param key
	 * @param value
	 */
	public void setParameter(String key, Object value) {
		parameters.put(key, value);
	}
	
	/**
	 * Set from Parameters hash
	 * @param parameters
	 */
	public void setParameters(HashMap<String, Object> parameters) {
		for(Entry<String, Object> entry : parameters.entrySet()) {
			this.parameters.put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Get a Parameter value from key
	 * @param key
	 * @return
	 */
	public Object getParameter(String key) {
		return parameters.get(key);
	}

	/**
	 * Get Parameter as Integer
	 * @param key
	 * @return
	 */
	public int getParameterAsInt(String key) {
		Object parameter = getParameter(key);
		if(parameter != null 
				&& parameter instanceof Integer) {
			return ((Integer) parameter).intValue();
		}
		//	Default
		return 0;
	}
	
	/**
	 * Get Parameter as BigDecimal
	 * @param key
	 * @return
	 */
	public BigDecimal getParameterAsBigDecimal(String key) {
		Object parameter = getParameter(key);
		if(parameter != null 
				&& parameter instanceof BigDecimal) {
			return ((BigDecimal) parameter);
		}
		//	Default
		return Env.ZERO;
	}
	
	/**
	 * Set Parameter Value
	 * @param key
	 * @param value
	 */
	public void setReturnValue(String key, Object value) {
		returnValues.put(key, value);
	}
	
	/**
	 * Get a Parameter value from key
	 * @param key
	 * @return
	 */
	public Object getReturnValue(String key) {
		return returnValues.get(key);
	}
	
	/**
	 * Get All return values
	 * @return
	 */
	public HashMap<String, Object> getReturnValues() {
		return returnValues;
	}
	
	/**
	 * Get Calculated Withholding Amount
	 * @return
	 */
	public BigDecimal getWithholdingAmount() {
		return withholdingAmount;
	}

	/**
	 * Set Withholding Amount
	 * @param withholdingAmount
	 */
	public void setWithholdingAmount(BigDecimal withholdingAmount) {
		this.withholdingAmount = withholdingAmount;
	}
	
	/**
	 * Get Calculated Withholding Rate
	 * @return
	 */
	public BigDecimal getWithholdingRate() {
		return getWithholdingRate(false);
	}
	
	/**
	 * Return rate 
	 * @param converted: Apply (withholdingRate / 100)
	 * @return
	 */
	public BigDecimal getWithholdingRate(boolean converted) {
		if(converted) {
			return withholdingRate.divide(Env.ONEHUNDRED);
		}
		return withholdingRate;
	}

	/**
	 * Set Withholding Rate
	 * @param withholdingRate
	 */
	public void setWithholdingRate(BigDecimal withholdingRate) {
		this.withholdingRate = withholdingRate;
	}
	
	/**
	 * Add Amount for Withholding
	 * @param withholdingAmount
	 */
	public void addWithholdingAmount(BigDecimal withholdingAmount) {
		this.withholdingAmount = this.withholdingAmount.add(withholdingAmount);
	}

	/**
	 * Get Base Amount for calculation
	 * @return
	 */
	public BigDecimal getBaseAmount() {
		return baseAmount;
	}

	/**
	 * Base Amount for calculate withholding
	 * @param baseAmount
	 */
	public void setBaseAmount(BigDecimal baseAmount) {
		this.baseAmount = baseAmount;
	}

	/**
	 * Add Base Amount
	 * @param baseAmount
	 */
	public void addBaseAmount(BigDecimal baseAmount) {
		this.baseAmount = this.baseAmount.add(baseAmount);
	}
	
	/**
	 * Validate if the current document is valid for process
	 * @return
	 */
	public abstract boolean isValid();
	
	/**
	 * Run Process
	 * @return
	 */
	public abstract String run();
	
	/**
	 * Create Withholding and clear values
	 */
	protected void saveResult() {
		try {
			if(getWithholdingAmount() != null
					&& getWithholdingAmount().compareTo(Env.ZERO) > 0) {
				createWithholding();
			} else {
				createLog();
			}
		} catch(Exception e) {
			throw e;
		} finally {
			//	Clear Values
			clearValues();
		}
	}
	
	/**
	 * Create Allocation for processed setting
	 * @param clearValues
	 */
	private void createWithholding() {
		MWHWithholding withholding = new MWHWithholding(getContext(), 0, getTransactionName());
		withholding.setDateDoc(new Timestamp(System.currentTimeMillis()));
		withholding.setA_Base_Amount(getBaseAmount());
		withholding.setWithholdingAmt(getWithholdingAmount());
		withholding.setWithholdingRate(getWithholdingRate());
		withholding.setWH_Definition_ID(getDefinition().getWH_Definition_ID());
		withholding.setWH_Setting_ID(getSetting().getWH_Setting_ID());
		withholding.setC_DocType_ID();
		
		Optional<PO> mayBeSourceDocument = Optional.ofNullable(document);
		
		mayBeSourceDocument.ifPresent(sourceDocument -> {
			if (sourceDocument instanceof MInvoice)
				withholding.setSourceInvoice_ID(sourceDocument.get_ID());
			if (sourceDocument instanceof MOrder)
				withholding.setSourceOrder_ID(sourceDocument.get_ID());
			
			withholding.setC_BPartner_ID(sourceDocument.get_ValueAsInt(MWHWithholding.COLUMNNAME_C_BPartner_ID));
		});
		
		//	Description
		if(!Util.isEmpty(getProcessDescription())) {
			withholding.setDescription(Msg.parseTranslation(getContext(), getProcessDescription()));
		}
		//	Add additional references
		//	Note that not exist validation for types
		getReturnValues().entrySet().forEach(value -> {
			if(withholding.get_ColumnIndex(value.getKey()) > 0) {
				if(value.getValue() != null) {
					withholding.set_ValueOfColumn(value.getKey(), value.getValue());
				}
			}
		});
		//	Save
		withholding.setDocStatus(MWHWithholding.DOCSTATUS_Drafted);
		withholding.saveEx();
		//	Complete
		if(withholding.processIt(MWHWithholding.ACTION_Complete)) 
			withholding.saveEx();
		else
			throw new AdempiereException(withholding.getProcessMsg());
		
	}
	
	/**
	 * Clear all values
	 */
	private void clearValues() {
		baseAmount = Env.ZERO;
		withholdingRate = Env.ZERO;
		withholdingAmount = Env.ZERO;
		returnValues = new HashMap<String, Object>();
		processLog = new StringBuffer();
		processDescription = new StringBuffer();
	}
	
	/**
	 * Create Event Log
	 * @param clearValues
	 */
	private void createLog() {
		if(Util.isEmpty(getProcessLog())) {
			return;
		}
		MWHLog log = new MWHLog(getContext(), 0, getTransactionName());
		log.setWH_Definition_ID(getDefinition().getWH_Definition_ID());
		log.setWH_Setting_ID(getSetting().getWH_Setting_ID());
		//	Description
		log.setComments(Msg.parseTranslation(getContext(), getProcessLog()));
		//	Add additional references
		//	Note that not exist validation for types
		getReturnValues().entrySet().forEach(value -> {
			if(log.get_ColumnIndex(value.getKey()) >= 0) {
				if(value.getValue() != null) {
					log.set_ValueOfColumn(value.getKey(), value.getValue());
				}
			}
		});
		//	Save
		log.saveEx();
	}
	
}	//	PaymentExport ???? WTF
