package com.n9mtq4.exmcl.modinstaller.ui

import com.n9mtq4.exmcl.modinstaller.data.ModData
import com.n9mtq4.exmcl.modinstaller.data.ModEntry
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.io.File
import java.io.IOException
import java.util.ArrayList
import javax.swing.JOptionPane
import javax.swing.event.ChangeEvent
import javax.swing.event.ListSelectionEvent
import javax.swing.event.TableColumnModelEvent
import javax.swing.event.TableColumnModelListener
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.DefaultTableModel

/**
 * Created by will on 2/15/16 at 6:24 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ModsTableModel(val modData: ModData, val modsTab: ModsTab, val table: ModsTable) : 
		DefaultTableModel(), TableModelListener, TableColumnModelListener, ComponentListener {
	
	companion object {
		private val TABLE_TITLES = arrayOf<Any>("Enabled", "Mod File Location")
		private val ENABLED_COLUMN_WIDTH = 60
	}
	
	private var setBefore = false
	
	internal fun fireSet() {
		if (setBefore) return
		setBefore = true
		refresh()
		addTableModelListener(this)
		table.getColumnModel().addColumnModelListener(this)
		table.addComponentListener(this)
	}
	
	internal fun refresh() {
		val selectedProfileIndex = if (modData.selectedProfileIndex == -1) 0 else modData.selectedProfileIndex
		val selectedProfile = modData.profiles[selectedProfileIndex] // use the old method for selected profile
		val t = Array<Array<out Any>>(selectedProfile.modList.size) { i -> 
			arrayOf(selectedProfile.modList[i].enabled, selectedProfile.modList[i].file.absolutePath)
		}
		setDataVector(t, TABLE_TITLES)
	}
	
	private fun resizeColumns() {
		try {
			table.getColumnModel().getColumn(0).run {
				minWidth = ENABLED_COLUMN_WIDTH
				maxWidth = ENABLED_COLUMN_WIDTH
				preferredWidth = ENABLED_COLUMN_WIDTH
			}
			table.getColumnModel().getColumn(1).run {
				minWidth = table.width - ENABLED_COLUMN_WIDTH
				maxWidth = table.width - ENABLED_COLUMN_WIDTH
				preferredWidth = table.width - ENABLED_COLUMN_WIDTH
			}
		}catch (e: ArrayIndexOutOfBoundsException) {
			System.err.println("Resizing Columns, but they haven't been created yet!")
			e.printStackTrace()
		}
	}
	
	internal fun fireModDataSync() {
		if (modData.selectedProfileIndex == -1) {
			println("Selected Profile is -1, so canceling mod data save")
			return
		}
		println("Saving the jar ModData")
		
		val selectedProfile = modData.getSelectedProfile()
		selectedProfile.modList = ArrayList<ModEntry>()
		fireSet()
		
		for (i in 0..table.rowCount - 1) {
			val enabled = table.getValueAt(i, 0) as Boolean
			val file = File(table.getValueAt(i, 1) as String)
			selectedProfile.addMod(file, enabled)
		}
		
		try {
			modData.save()
		} catch (e: IOException) {
			e.printStackTrace()
			JOptionPane.showMessageDialog(table, "Error saving the ModData!", "Error", JOptionPane.ERROR_MESSAGE)
		}
		
	}
	
	@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
	override fun getColumnClass(columnIndex: Int) = if (columnIndex == 0) java.lang.Boolean::class.java else super.getColumnClass(columnIndex)
	override fun isCellEditable(row: Int, column: Int) = column == 0
	override fun tableChanged(e: TableModelEvent) = fireModDataSync()
	override fun columnSelectionChanged(e: ListSelectionEvent) {}
	override fun columnMoved(e: TableColumnModelEvent) {}
	override fun columnAdded(e: TableColumnModelEvent) {}
	override fun columnRemoved(e: TableColumnModelEvent) {}
	override fun columnMarginChanged(e: ChangeEvent) = resizeColumns()
	override fun componentMoved(e: ComponentEvent) {}
	override fun componentResized(e: ComponentEvent) = resizeColumns()
	override fun componentShown(e: ComponentEvent) {}
	override fun componentHidden(e: ComponentEvent) {}
	
}
