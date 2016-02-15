package com.n9mtq4.exmcl.forgemods.ui

import javax.swing.table.DefaultTableModel

/**
 * Created by will on 2/15/16 at 5:43 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ImmutableTableModel(p0: Array<out Array<out Any>>, p1: Array<out Any>?) : DefaultTableModel(p0, p1) {
	
	override fun isCellEditable(p0: Int, p1: Int) = false
	
}
