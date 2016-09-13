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

package com.n9mtq4.exmcl.forgemods.ui

import com.n9mtq4.exmcl.forgemods.GameStartHook
import com.n9mtq4.exmcl.forgemods.data.ModData
import com.n9mtq4.exmcl.forgemods.data.ModProfile
import com.n9mtq4.exmcl.forgemods.data.deepCopy
import com.n9mtq4.exmcl.forgemods.utils.browseForMods
import com.n9mtq4.exmcl.forgemods.utils.firstRunCleanup
import com.n9mtq4.exmcl.forgemods.utils.msg
import com.n9mtq4.kotlin.extlib.pst
import com.n9mtq4.logwindow.BaseConsole
import net.minecraft.launcher.Launcher
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
 * Created by will on 11/4/15 at 2:53 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ForgeTab(val minecraftLauncher: Launcher, val baseConsole: BaseConsole) :
		JSplitPane(JSplitPane.HORIZONTAL_SPLIT), ListSelectionListener, ActionListener {
	
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
	val table: FModsTable
	
	val installForge: JButton
	val addProfile: JButton
	val removeProfile: JButton
	val dupProfile: JButton
	val addMod: JButton
	val removeMod: JButton
	
	init {
		
		this.modData = ModData.Loader.load()
		
		isEnabled = false
		
//		set up table and scrolling
		this.buttonPanel = JPanel(GridLayout(6, 1))
		this.list = JList<String>(modData.getProfileNames().toTypedArray())
		list.selectionMode = ListSelectionModel.SINGLE_SELECTION
		list.addListSelectionListener(this)
		this.listScroll = JScrollPane(list)
		listScroll.setColumnHeaderView(JLabel(HEADER_HTML))
		this.table = FModsTable(modData, this)
		
//		buttons
		this.installForge = JButton("Install Forge").apply { toolTipText = "Install forge. Provides a list that you can pick from." }
		this.addProfile = JButton("New Profile").apply { toolTipText = "Makes a new profile." }
		this.removeProfile = JButton("Delete Profile").apply { toolTipText = "Deletes the selected profile." }
		this.dupProfile = JButton("Duplicate Profile").apply { toolTipText = "Duplicates the selected profile." }
		this.addMod = JButton("Add Mod").apply { toolTipText = "Add mods the the current profile.\nYou can also drag and drop mods into the table." }
		this.removeMod = JButton("Remove Mod").apply { toolTipText = "Removes the selected mod." }
		buttonPanel.run {
			add(installForge)
			add(addProfile)
			add(removeProfile)
			add(dupProfile)
			add(addMod)
			add(removeMod)
		}
		installForge.addActionListener(this)
		addProfile.addActionListener(this)
		removeProfile.addActionListener(this)
		dupProfile.addActionListener(this)
		addMod.addActionListener(this)
		removeMod.addActionListener(this)
		
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
		isOneTouchExpandable = false
		setDividerLocation(.2)
		
		sideSplitPane.minimumSize = MINIMUM_SIZE
		tableScroll.minimumSize = MINIMUM_SIZE
		
		table.fillsViewportHeight = true
		sideSplitPane.setDividerLocation(.9)
		
		pst {
			firstRunCleanup(minecraftLauncher, modData, this)
			refresh() // mod data is also saved
		}
		baseConsole.addListenerAttribute(GameStartHook(minecraftLauncher, modData))
		
	}
	
	fun refresh() {
		refreshList()
		table.refresh(sync = false) // it is synced in refresh list instead
		syncWithFile()
	}
	
	fun refreshList() {
		list.setListData(modData.getProfileNames().toTypedArray())
		list.selectedIndex = modData.selectedProfileIndex
	}
	
	internal fun syncWithFile() {
		try {
			modData.save()
			println("Saved forge mod data")
		}catch (e: IOException) {
			e.printStackTrace()
			msg(parent = table, msg = "Unable to save the Forge Mod Data", title = "Error", msgType = JOptionPane.ERROR_MESSAGE)
		}
	}
	
	override fun actionPerformed(e: ActionEvent) {
		
		val button = e.source as JButton
		
		when (button) {
			installForge -> installForge()
			addProfile -> newProfile()
			removeProfile -> deleteProfile()
			dupProfile -> dupProfile()
			addMod -> addMod()
			removeMod -> removeMod()
		}
		
	}
	
	private fun installForge() = InstallForgeDialog(this)
	
	private fun newProfile() {
		val profileName: String? = JOptionPane.showInputDialog(this, "What should the profile be called?")
		if (profileName == null || profileName.isNullOrBlank()) return
		val profile = ModProfile(profileName)
		modData.addProfile(profile)
		refreshList()
	}
	
	private fun deleteProfile() {
		if (modData.getSelectedProfile().profileName.equals("default", true)) {
			JOptionPane.showMessageDialog(this, "You can't delete the default profile.", "Error", JOptionPane.ERROR_MESSAGE)
			return
		}
		val sure = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete\nthe profile ${modData.getSelectedProfile().profileName}?",
				"Delete?", JOptionPane.YES_NO_OPTION)
		if (sure != JOptionPane.YES_NO_OPTION) return
		modData.removeProfile(modData.selectedProfileIndex)
		modData.selectedProfileIndex = modData.selectedProfileIndex - 1; // move up one, this will never be -1, cause being at 0 is default profile
		refresh()
	}
	
	private fun dupProfile() {
		val profileName: String? = JOptionPane.showInputDialog(this, "What should the profile be called?")
		if (profileName == null ||profileName.isNullOrBlank()) return
		modData.addProfile(modData.getSelectedProfile().deepCopy(profileName))
		refreshList()
	}
	
	private fun addMod() {
		val mods = browseForMods(this)
		mods.forEach { modData.getSelectedProfile().addMod(it) }
		table.refresh()
	}
	
	private fun removeMod() {
		val row = table.selectedRow
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "You haven't selected a mod to remove!", "Error", JOptionPane.ERROR_MESSAGE)
			return
		}
		modData.getSelectedProfile().removeMod(row)
		table.refresh()
	}
	
	override fun valueChanged(e: ListSelectionEvent) {
		
		if (list.selectedIndex < 0) return
		modData.selectedProfileIndex = list.selectedIndex
		table.refresh() // only refresh the table
		
	}
	
}
