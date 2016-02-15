package com.n9mtq4.exmcl.forgemods.ui

import com.n9mtq4.exmcl.forgemods.data.ModData
import com.n9mtq4.exmcl.forgemods.data.ModProfile
import com.n9mtq4.exmcl.forgemods.utils.browseForMods
import com.n9mtq4.exmcl.tab.forgemods.ui.FModsTable
import com.n9mtq4.logwindow.BaseConsole
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
class ForgeTab(val baseConsole: BaseConsole) :
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
		this.installForge = JButton("Install Forge")
		this.addProfile = JButton("New Profile")
		this.removeProfile = JButton("Delete Profile")
		this.dupProfile = JButton("Duplicate Profile")
		this.addMod = JButton("Add Mod")
		this.removeMod = JButton("Remove Mod")
		addMod.toolTipText = "Add mods the the current profile.\nYou can also drag and drop mods into the table."
		buttonPanel.apply {
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
		sideSplitPane.topComponent = listScroll
		sideSplitPane.bottomComponent = buttonPanel
		sideSplitPane.resizeWeight = 1.0
		sideSplitPane.setDividerLocation(1.0)
		sideSplitPane.isEnabled = false
		
		this.tableScroll = JScrollPane(table)
		
		setLeftComponent(sideSplitPane)
		setRightComponent(tableScroll)
		isOneTouchExpandable = false;
		setDividerLocation(.2)
		
		sideSplitPane.minimumSize = MINIMUM_SIZE
		tableScroll.minimumSize = MINIMUM_SIZE
		
		table.fillsViewportHeight = true
		sideSplitPane.setDividerLocation(.9)
		
		try {
//			TODO: commented
//			ForgeModManager.firstRunCleanup(parent.minecraftLauncher, modData, this)
			modData.save()
			refresh()
		} catch (e: IOException) {
			e.printStackTrace()
		}
//		TODO: commented
//		baseConsole.addListenerAttribute(GameStartHook(parent.minecraftLauncher, modData))
		
	}
	
	fun refresh() {
		refreshList()
		table.refreshModel()
	}
	
	fun refreshList() {
//		list.setListData(modData.getProfileNames())
		list.setListData(modData.getProfileNames().toTypedArray())
		list.selectedIndex = modData.selectedProfileIndex
		table.fireModDataSync()
	}
	
	override fun actionPerformed(e: ActionEvent) {
		
		val button = e.source as JButton
		val text = button.text.toLowerCase()
		
		when (text) {
			"install forge" -> installForge()
			"new profile" -> newProfile()
			"delete profile" -> deleteProfile()
			"duplicate profile" -> dupProfile()
			"add mod" -> addMod()
			"remove mod" -> removeMod()
		}
		
	}
	
	private fun installForge() = InstallForgeDialog(this)
	
	private fun newProfile() {
		val profileName: String? = JOptionPane.showInputDialog(this, "What should the profile be called?")
		if (profileName == null ||profileName.isNullOrBlank()) return
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
//		TODO: yes is 0 and no is 1? WHAT THE FUCK!?!?!?!?!?!?!?!?!?!?
		if (sure != 0) return
		modData.removeProfile(modData.selectedProfileIndex)
		modData.selectedProfileIndex = modData.selectedProfileIndex - 1; // move up one, this will never be -1, cause being at 0 is default profile
		refresh()
	}
	
	private fun dupProfile() {
		val profileName: String? = JOptionPane.showInputDialog(this, "What should the profile be called?")
		if (profileName == null ||profileName.isNullOrBlank()) return
		modData.addProfile(modData.getSelectedProfile().copy(profileName))
		refreshList()
	}
	
	private fun addMod() {
		val mods = browseForMods(this)
		mods.forEach { modData.getSelectedProfile().addMod(it) }
		table.refreshModel()
	}
	
	private fun removeMod() {
		val row = table.selectedRow
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "You haven't selected a mod to remove!", "Error", JOptionPane.ERROR_MESSAGE)
			return
		}
		modData.getSelectedProfile().removeMod(row)
		table.refreshModel()
	}
	
	override fun valueChanged(e: ListSelectionEvent) {
		
		if (list.selectedIndex < 0) return
		modData.selectedProfileIndex = list.selectedIndex
		table.refreshModel()
		
	}
	
}
