package com.n9mtq4.exmcl.modinstaller.ui

import com.n9mtq4.exmcl.modinstaller.data.ModData
import com.n9mtq4.exmcl.modinstaller.utils.FileDrop
import java.io.File
import javax.swing.JTable
import javax.swing.ListSelectionModel

/**
 * Created by will on 11/4/15 at 5:55 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ModsTable(var modData: ModData, val modsTab: ModsTab) : JTable() {
	
	val forgeModel: FModsTableModel
	
	init {
		
		this.forgeModel = FModsTableModel(modData, modsTab, this)
		
		model = forgeModel
		
		forgeModel.fireSet()
		
		tableHeader.reorderingAllowed = false
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
		
		initFileDrop()
		
	}
	
	fun fireModDataSync() {
		forgeModel.fireModDataSync()
	}
	
	fun refreshModel() {
		forgeModel.refresh()
	}
	
	fun refreshTab() {
		modsTab.refresh()
	}
	
	private fun initFileDrop() {
		
		FileDrop(this, FileDrop.Listener { files: Array<File> ->
			files.forEach { modData.profiles[modData.selectedProfileIndex].addMod(it) }
			refreshModel()
		})
		
	}
	
}
