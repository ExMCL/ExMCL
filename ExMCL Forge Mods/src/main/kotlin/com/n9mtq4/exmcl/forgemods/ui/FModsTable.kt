package com.n9mtq4.exmcl.forgemods.ui

import com.n9mtq4.exmcl.forgemods.data.ModData
import com.n9mtq4.filedrop.FileDrop
import java.io.File
import javax.swing.JTable
import javax.swing.ListSelectionModel

/**
 * Created by will on 11/4/15 at 5:55 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class FModsTable(var modData: ModData, val forgeTab: ForgeTab) : JTable() {
	
	val forgeModel: FModsTableModel
	
	init {
		
		this.forgeModel = FModsTableModel(modData, forgeTab, this)
		
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
		forgeTab.refresh()
	}
	
	private fun initFileDrop() {
		
		FileDrop(this, FileDrop.Listener { files: Array<File> ->
			files.forEach { modData.profiles[modData.selectedProfileIndex].addMod(it) }
			refreshModel()
		})
		
	}
	
}
