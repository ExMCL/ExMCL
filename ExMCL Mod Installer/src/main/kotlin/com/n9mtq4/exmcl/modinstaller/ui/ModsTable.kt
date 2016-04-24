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
import com.n9mtq4.exmcl.modinstaller.utils.isMod
import com.n9mtq4.filedrop.FileDrop
import java.io.File
import javax.swing.JTable
import javax.swing.ListSelectionModel

/**
 * Created by will on 11/4/15 at 5:55 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ModsTable(var modData: ModData, val modsTab: ModsTab) : JTable() {
	
	val tableModel: ModsTableModel
	
	init {
		
		this.tableModel = ModsTableModel(modData, modsTab, this)
		
		model = tableModel
		
		tableModel.fireSet()
		
		tableHeader.reorderingAllowed = false
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
		
		initFileDrop()
		
	}
	
	fun fireModDataSync() = tableModel.fireModDataSync()
	
	fun refreshModel() = tableModel.refresh()
	
	fun refreshTab() = modsTab.refresh()
	
	private fun initFileDrop() {
		
		FileDrop(this, FileDrop.Listener { files: Array<File> ->
			files.filter { it.isMod() }.forEach { modData.profiles[modData.selectedProfileIndex].addMod(it) }
			refreshModel()
		})
		
	}
	
}
