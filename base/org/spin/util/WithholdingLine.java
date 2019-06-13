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
import java.util.HashMap;
import java.util.Map.Entry;

import org.compiere.util.Env;

/**
 * Withholding Line storage class for save line by line when has been processed
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class WithholdingLine {

	public WithholdingLine(BigDecimal withholdingAmount) {
		this.withholdingAmount = withholdingAmount;
		attributes = new HashMap<String, Object>();
	}

	/**	Base Amount	*/
	private BigDecimal baseAmount;
	/**	Withholding Amount	*/
	private BigDecimal withholdingAmount;
	/**	Attributes	*/
	private HashMap<String, Object> attributes;
	
	/**
	 * Set Attribute Value
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	/**
	 * Set from Parameters hash
	 * @param attributes
	 */
	public void setAttributes(HashMap<String, Object> attributes) {
		for(Entry<String, Object> entry : attributes.entrySet()) {
			this.attributes.put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Get a Parameter value from key
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	/**
	 * Get Parameter as Integer
	 * @param key
	 * @return
	 */
	public int getAttributeAsInt(String key) {
		Object attribute = getAttribute(key);
		if(attribute != null 
				&& attribute instanceof Integer) {
			return ((Integer) attribute).intValue();
		}
		//	Default
		return 0;
	}
	
	/**
	 * Get Parameter as BigDecimal
	 * @param key
	 * @return
	 */
	public BigDecimal getAttributeAsBigDecimal(String key) {
		Object parameter = getAttribute(key);
		if(parameter != null 
				&& parameter instanceof BigDecimal) {
			return ((BigDecimal) parameter);
		}
		//	Default
		return Env.ZERO;
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
}
