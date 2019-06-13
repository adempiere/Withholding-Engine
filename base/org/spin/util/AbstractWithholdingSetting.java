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
 * Copyright (C) 2003-2014 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.compiere.util.Env;
import org.spin.model.MWHDefinition;
import org.spin.model.MWHSetting;

/**
 * Abstract class for handle all withholding document
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public abstract class AbstractWithholdingSetting {
	
	
	public AbstractWithholdingSetting(MWHSetting setting) {
		this.setting = setting;
		this.ctx = setting.getCtx();
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
	private Properties ctx;
	/**	Transaction Name	*/
	private String transactionName;
	/**	Processing Message	*/
	private StringBuffer processMessage = new StringBuffer();
	/**	Withholding Lines	*/
	private List<WithholdingLine> withholdingLines = new ArrayList<>();
	/**	Default parameters	*/
	public static final String PO = "PO";
	
	/**
	 * Get Context
	 * @return
	 */
	public Properties getCtx() {
		return ctx;
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
	protected void addMessage(String message) {
		if(processMessage.length() > 0) {
			processMessage.append(Env.NL);
		}
		processMessage.append(message);
	}
	
	/**
	 * Get Process Message
	 * @return
	 */
	public String getProcessMessage() {
		if(processMessage.length() > 0) {
			return processMessage.toString();
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
	public MWHDefinition getWithholdingDefinition() {
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
	 * Add a line to list
	 * @param line
	 */
	protected void addWithholdingLine(WithholdingLine line) {
		if(line == null) {
			return;
		}
		withholdingLines.add(line);
	}
	
	/**
	 * Get withholding lines processed
	 * @return
	 */
	public List<WithholdingLine> getWithholdingLines() {
		return withholdingLines;
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
	
}	//	PaymentExport ???? WTF
