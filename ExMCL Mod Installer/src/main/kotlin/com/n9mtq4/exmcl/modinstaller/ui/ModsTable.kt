/*
 * MIT License
 *
 * Copyright (c) 2016 Will (n9Mtq4) Bresnahan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.n9mtq4.exmcl.modinstaller.ui

import com.n9mtq4.exmcl.modinstaller.data.ModData
import com.n9mtq4.exmcl.modinstaller.utils.il
import com.n9mtq4.exmcl.modinstaller.utils.isMod
import com.n9mtq4.filedrop.FileDrop
import com.n9mtq4.kotlin.extlib.ignore
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.io.File
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.event.ChangeEvent
import javax.swing.event.ListSelectionEvent
import javax.swing.event.TableColumnModelEvent
import javax.swing.event.TableColumnModelListener
import javax.swing.table.DefaultTableModel

/**
 * Created by will on 11/4/15 at 5:55 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ModsTable(var modData: ModData, val modsTab: ModsTab) : JTable() {
	
	companion object {
		private val TABLE_HEADERS = arrayOf("Enabled", "Mod File Location")
		private val ENABLED_COLUMN_WIDTH = 60
	}
	
	private val tableModel = ForgeTableModel()
	private val columnResizer = ColumnResizer()
	
	init {
		model = tableModel
		
		tableHeader.reorderingAllowed = false
		tableHeader.resizingAllowed = false
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
		
		columnResizer.resizeColumns()
		addComponentListener(columnResizer)
		columnModel.addColumnModelListener(columnResizer)
		
		initFileDrop()
	}
	
	internal fun refresh(select: IntArray? = selectedRows, sync: Boolean = true) {
		tableModel.fireTableDataChanged()
		il {
			clearSelection()
			select?.forEach { ignore { selectionModel.addSelectionInterval(it, it) } }
		}
		if (sync) modsTab.syncWithFile()
	}
	
	private fun initFileDrop() = FileDrop(this, FileDrop.Listener { files: Array<File> ->
		files.filter(File::isMod).forEach { modData.getSelectedProfile().addMod(it) }
		refresh()
	})
	
	private inner class ForgeTableModel : DefaultTableModel() {
		
		override fun getRowCount() = modData.getSelectedProfile().modList.size
		override fun getColumnCount() = TABLE_HEADERS.size
		override fun getColumnName(column: Int) = TABLE_HEADERS[column]
		
		override fun isCellEditable(row: Int, column: Int) = column == 0
		@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
		override fun getColumnClass(columnIndex: Int): Class<out Any> = if (columnIndex == 0) java.lang.Boolean::class.java else String::class.java
		
		override fun getValueAt(row: Int, column: Int): Any {
			val mod = modData.getSelectedProfile().modList[row]
			return when(column) {
				0 -> mod.enabled
				1 -> mod.name
				else -> throw IndexOutOfBoundsException("Invalid mod at column $column")
			}
		}
		
		override fun setValueAt(aValue: Any, row: Int, column: Int) {
			val mod = modData.getSelectedProfile().modList[row]
			when (column) {
				0 -> mod.enabled = aValue as Boolean
			}
			modsTab.syncWithFile()
		}
		
	}
	
	private inner class ColumnResizer : TableColumnModelListener, ComponentListener {
		
		override fun componentResized(e: ComponentEvent?) = resizeColumns()
		override fun columnMarginChanged(e: ChangeEvent?) = resizeColumns()
		
		override fun columnSelectionChanged(e: ListSelectionEvent?) {}
		override fun columnMoved(e: TableColumnModelEvent?) {}
		override fun columnAdded(e: TableColumnModelEvent?) {}
		override fun columnRemoved(e: TableColumnModelEvent?) {}
		override fun componentMoved(e: ComponentEvent?) {}
		override fun componentShown(e: ComponentEvent?) {}
		override fun componentHidden(e: ComponentEvent?) {}
		
		fun resizeColumns() {
			columnModel.getColumn(0).run {
				minWidth = ENABLED_COLUMN_WIDTH
				maxWidth = ENABLED_COLUMN_WIDTH
				preferredWidth = ENABLED_COLUMN_WIDTH
			}
			columnModel.getColumn(1).run {
				val width = this@ModsTable.width // without this line it defaults to column width. then the column vanishes. Spent a MONTH debugging it.
				minWidth = width - ENABLED_COLUMN_WIDTH
				maxWidth = width - ENABLED_COLUMN_WIDTH
				preferredWidth = width - ENABLED_COLUMN_WIDTH
			}
		}
		
	}
	
}
