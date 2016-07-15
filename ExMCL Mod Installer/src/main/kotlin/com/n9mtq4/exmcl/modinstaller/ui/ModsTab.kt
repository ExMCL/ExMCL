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

import com.n9mtq4.exmcl.modinstaller.GameStartHook
import com.n9mtq4.exmcl.modinstaller.data.ModData
import com.n9mtq4.exmcl.modinstaller.utils.browseForMods
import com.n9mtq4.exmcl.modinstaller.utils.msg
import com.n9mtq4.kotlin.extlib.pst
import com.n9mtq4.kotlin.extlib.pstAndUnit
import com.n9mtq4.logwindow.BaseConsole
import net.minecraft.launcher.Launcher
import net.minecraft.launcher.profile.ProfileManager
import net.minecraft.launcher.profile.RefreshedProfilesListener
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.IOException
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

/**
 * Created by will on 2/17/16 at 5:24 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ModsTab(val minecraftLauncher: Launcher, val baseConsole: BaseConsole) : JSplitPane(), RefreshedProfilesListener, ActionListener, ListSelectionListener {
	
	companion object static {
		val HEADER_HTML: String = "<html><b>Profiles</b></html>"
		val MINIMUM_SIZE: Dimension = Dimension(100, 50)
	}
	
	var modData: ModData
	
	val list: JList<String>
	val listScroll: JScrollPane
	val buttonPanel: JPanel
	val sideSplitPane: JSplitPane
	val tableScroll: JScrollPane
	val table: ModsTable
	
	val addMod: JButton
	val removeMod: JButton
	val up: JButton
	val down: JButton
	
	init {
		
//		add profile listen hook
		minecraftLauncher.profileManager.addRefreshedProfilesListener(this)
		
	}
	
	init {
		
		this.modData = ModData.Loader.load()
		
		isEnabled = false
		
//		set up table and scrolling
		this.buttonPanel = JPanel(GridLayout(4, 1))
		this.list = JList<String>(modData.getProfileNames().toTypedArray())
		list.selectionMode = ListSelectionModel.SINGLE_SELECTION
		list.addListSelectionListener(this)
		this.listScroll = JScrollPane(list)
		listScroll.setColumnHeaderView(JLabel(HEADER_HTML))
		this.table = ModsTable(modData, this)
		
//		buttons
		this.addMod = JButton("Add Mod").apply { toolTipText = "Add mods the the current profile.\nYou can also drag and drop mods into the table." }
		this.removeMod = JButton("Remove Mod").apply { toolTipText = "Remove the selected mod." }
		this.up = JButton("Up").apply { toolTipText = "Mods nearer the top are loaded before\nmods nearer the bottom." }
		this.down = JButton("Down").apply { toolTipText = "Mods nearer the top are loaded before\nmods nearer the bottom." }
		buttonPanel.run {
			add(addMod)
			add(removeMod)
			add(up)
			add(down)
		}
		addMod.addActionListener(this)
		removeMod.addActionListener(this)
		up.addActionListener(this)
		down.addActionListener(this)
		
//		set up split pane
		this.sideSplitPane = JSplitPane(VERTICAL_SPLIT)
		sideSplitPane.run {
			topComponent = listScroll
			bottomComponent = buttonPanel
			resizeWeight = 1.0
			setDividerLocation(1.0)
			isEnabled = false
		}
		
		this.tableScroll = JScrollPane(table)
		
		setLeftComponent(sideSplitPane)
		setRightComponent(tableScroll)
		isOneTouchExpandable = false;
		setDividerLocation(.2)
		
		sideSplitPane.minimumSize = MINIMUM_SIZE
		tableScroll.minimumSize = MINIMUM_SIZE
		
		table.fillsViewportHeight = true
		sideSplitPane.setDividerLocation(.9)
		
		pst {
			refresh()
		}
		baseConsole.addListenerAttribute(GameStartHook(minecraftLauncher, modData))
		
	}
	
	fun refresh() {
		refreshList()
		table.refresh(sync = false)
		syncWithFile()
	}
	
	fun refreshList() {
//		list.setListData(modData.getProfileNames())
		list.setListData(modData.getProfileNames().toTypedArray())
		val profileNameToSet = minecraftLauncher.profileManager?.selectedProfile?.name ?: return
		list.setSelectedValue(profileNameToSet, true)
//		list.selectedIndex = modData.selectedProfileIndex
		modData.selectedProfileIndex = list.selectedIndex
	}
	
	internal fun syncWithFile() {
		try {
			modData.save()
			println("Saved jar mod data")
		}catch (e: IOException) {
			e.printStackTrace()
			msg(parent = table, msg = "Unable to save the Jar Mod Data", title = "Error", msgType = JOptionPane.ERROR_MESSAGE)
		}
	}
	
	override fun actionPerformed(e: ActionEvent) {
		
		val button = e.source as JButton
		
		when (button) {
			addMod -> addMod()
			removeMod -> removeMod()
			up -> moveMod(-1)
			down -> moveMod(1)
		}
		
	}
	
	override fun valueChanged(e: ListSelectionEvent) {
		val newProfileName = list.selectedValue ?: return
		minecraftLauncher.profileManager.setSelectedProfile(newProfileName)
		
		if (list.selectedIndex < 0) return
		modData.selectedProfileIndex = list.selectedIndex
		table.refresh() // only the table
		
	}
	
	override fun onProfilesRefreshed(profileManager: ProfileManager) {
		
		modData.syncWithProfileManager(profileManager)
		refresh()
		
	}
	
	private fun addMod() {
		pstAndUnit {
			val mods = browseForMods(this)
			mods.forEach { modData.getSelectedProfile().addMod(it) }
			refresh()
		}
	}
	
	private fun removeMod() {
		val row = table.selectedRow
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "You haven't selected a mod to remove!", "Error", JOptionPane.ERROR_MESSAGE)
			return
		}
		modData.getSelectedProfile().removeMod(row)
		refresh()
		if (row - 1 > 0) table.setRowSelectionInterval(row - 1, row - 1) // select next mod
	}
	
	private fun moveMod(i: Int) {
		val row = table.selectedRow
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "You haven't selected a mod to remove!", "Error", JOptionPane.ERROR_MESSAGE)
			return
		}
		val ml = modData.getSelectedProfile().modList
		val desiredPos = row + i
		if (desiredPos !in 0..ml.size - 1) return // make sure in range
		val currentMod = ml[row] // tmp copy
		ml.removeAt(row) // remove
		ml.add(desiredPos, currentMod) // re-add
		refresh() // refresh
		table.setRowSelectionInterval(desiredPos, desiredPos) // select moved mod
	}
	
}
