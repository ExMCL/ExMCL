/*
package com.n9mtq4.exmcl.forgemods.ui

import com.n9mtq4.exmcl.forgemods.data.ModData
import com.n9mtq4.exmcl.tab.forgemods.ui.FModsTable
import com.n9mtq4.exmcl.tab.forgemods.ui.ForgeTab
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.event.ChangeEvent
import javax.swing.event.ListSelectionEvent
import javax.swing.event.TableColumnModelEvent
import javax.swing.event.TableColumnModelListener
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.DefaultTableModel

*/
/**
 * Created by will on 2/14/16 at 11:14 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 *//*


private const val ENABLED_COLUMN_WIDTH = 60

class FModsTableModel(private val modeData: ModData, private val forgeTab: ForgeTab, private val table: FModsTable) : 
		DefaultTableModel(), TableModelListener, TableColumnModelListener, ComponentListener {
	
	private var setBefore = false
	
	protected fun fireSet() {
		if (setBefore) return
		setBefore = true
		refresh()
		addTableModelListener(this)
		table.columnModel.addColumnModelListener(this)
		table.addComponentListener(this)
	}
	
	fun refresh() {
		
		val selectedProfileInex = if (modeData.selectedProfile == -1) 0 else modeData.selectedProfile
		val selectedProfile = modeData.getSelectedProfile()
		
		
		
	}
	
	override fun isCellEditable(row: Int, column: Int) = column == 0
	
	override fun getColumnClass(columnIndex: Int): Class<*> = if (columnIndex == 0) Boolean::class.java else super.getColumnClass(columnIndex)
	
	override fun tableChanged(e: TableModelEvent) {
	}
	
	override fun columnSelectionChanged(e: ListSelectionEvent) {
	}
	
	override fun columnMoved(e: TableColumnModelEvent) {
	}
	
	override fun columnAdded(e: TableColumnModelEvent) {
	}
	
	override fun columnRemoved(e: TableColumnModelEvent) {
	}
	
	override fun columnMarginChanged(e: ChangeEvent) {
	}
	
	override fun componentMoved(e: ComponentEvent) {
	}
	
	override fun componentResized(e: ComponentEvent) {
	}
	
	override fun componentShown(e: ComponentEvent) {
	}
	
	override fun componentHidden(e: ComponentEvent) {
	}
	
}
*/
