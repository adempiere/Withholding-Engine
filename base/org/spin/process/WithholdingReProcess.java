/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.									  *
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

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MTable;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.process.DocAction;
import org.compiere.util.Util;
import org.spin.util.WithholdingEngine;

/** 
 * Re-Process a Document for Withholding
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class WithholdingReProcess extends WithholdingReProcessAbstract {
	@Override
	protected void prepare() {
		super.prepare();
		if(getRecord_ID() == 0) {
			throw new AdempiereException("@Record_ID@ @NotFound@");
		}
	}

	@Override
	protected String doIt() throws Exception {
		PO entity = MTable.get(getCtx(), getTable_ID()).getPO(getRecord_ID(), get_TrxName());
		if(!DocAction.class.isAssignableFrom(entity.getClass())) {
			throw new AdempiereException("@RecordIsNotADocument@");
		}
		DocAction document = (DocAction) entity;
		if(!document.getDocStatus().equals(DocAction.STATUS_Completed)) {
			throw new AdempiereException("@InvoiceCreateDocNotCompleted@");
		}
		String error = WithholdingEngine.get().fireDocValidate(entity, ModelValidator.TIMING_AFTER_COMPLETE);
		if(Util.isEmpty(error)) {
			return "Ok";
		}
		//	return
		return error;
	}
}