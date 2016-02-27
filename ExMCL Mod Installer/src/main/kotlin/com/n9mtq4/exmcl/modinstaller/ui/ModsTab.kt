package com.n9mtq4.exmcl.modinstaller.ui

import com.n9mtq4.exmcl.modinstaller.data.ModData
import com.n9mtq4.exmcl.modinstaller.utils.browseForMods
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
import javax.swing.JTextArea
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
	
	init {
		
//		add profile listen hook
		minecraftLauncher.profileManager.addRefreshedProfilesListener(this)
		
	}
	
	init {
		
		this.modData = ModData.Loader.load()
		
		isEnabled = false
		
		//		set up table and scrolling
		this.buttonPanel = JPanel(GridLayout(2, 1))
		this.list = JList<String>(modData.getProfileNames().toTypedArray())
		list.selectionMode = ListSelectionModel.SINGLE_SELECTION
		list.addListSelectionListener(this)
		this.listScroll = JScrollPane(list)
		listScroll.setColumnHeaderView(JLabel(HEADER_HTML))
		this.table = ModsTable(modData, this)
		
		//		buttons
		this.addMod = JButton("Add Mod")
		this.removeMod = JButton("Remove Mod")
		addMod.toolTipText = "Add mods the the current profile.\nYou can also drag and drop mods into the table."
		buttonPanel.apply {
			add(addMod)
			add(removeMod)
		}
		addMod.addActionListener(this)
		removeMod.addActionListener(this)
		
		//		set up split pane
		this.sideSplitPane = JSplitPane(VERTICAL_SPLIT)
		sideSplitPane.apply {
			topComponent = listScroll
			bottomComponent = buttonPanel
			resizeWeight = 1.0
			setDividerLocation(1.0)
			isEnabled = false
		}
		
//		TODO: NOT YET DONE MESSAGE
		val label = JTextArea("This mod tab is NOT finished. Please use the forge mod tab instead.\n" +
				"This mod tab is 80% done in developer builds, but isn't stable enough yet and can't run on its own.\n" +
				"Current Progress:\n" +
				"[x] Mod managing\n" +
				"[x] Receiving play button push\n" +
				"[x] Copying profile data\n" +
				"[ ] Configuring the profile\n" +
				"[x] Unzipping version.jar\n" +
				"[x] Unzipping the mods\n" +
				"[x] Copying files\n" +
				"[x] Zipping version\n" +
				"[ ] Renaming version");
		label.isEditable = false;
		
		this.tableScroll = JScrollPane(table)
//		this.tableScroll = JScrollPane(label)
		
		setLeftComponent(sideSplitPane)
		setRightComponent(tableScroll)
		isOneTouchExpandable = false;
		setDividerLocation(.2)
		
		sideSplitPane.minimumSize = MINIMUM_SIZE
		tableScroll.minimumSize = MINIMUM_SIZE
		
		table.fillsViewportHeight = true
		sideSplitPane.setDividerLocation(.9)
		
		try {
//			firstRunCleanup(minecraftLauncher, modData, this)
			modData.save()
			refresh()
		} catch (e: IOException) {
			e.printStackTrace()
		}
//		baseConsole.addListenerAttribute(GameStartHook(minecraftLauncher, modData))
		
	}
	
	fun refresh() {
		refreshList()
		table.refreshModel()
	}
	
	fun refreshList() {
		//		list.setListData(modData.getProfileNames())
		list.setListData(modData.getProfileNames().toTypedArray())
		val profileNameToSet = minecraftLauncher.profileManager.selectedProfile.name
		list.setSelectedValue(profileNameToSet, true)
//		list.selectedIndex = modData.selectedProfileIndex
		modData.selectedProfileIndex = list.selectedIndex
		table.fireModDataSync()
	}
	
	override fun actionPerformed(e: ActionEvent) {
		
		val button = e.source as JButton
		val text = button.text.toLowerCase()
		
		when (text) {
			"add mod" -> addMod()
			"remove mod" -> removeMod()
		}
		
	}
	
	override fun valueChanged(e: ListSelectionEvent) {
		val newProfileName = list.selectedValue ?: return
		minecraftLauncher.profileManager.setSelectedProfile(newProfileName)
	}
	
	override fun onProfilesRefreshed(profileManager: ProfileManager) {
		
		modData.syncWithProfileManager(profileManager)
		refresh()
		
	}
	
	private fun addMod() {
/*		MinecraftPatcher.duplicate(minecraftLauncher)
		MinecraftPatcher.backupJar(minecraftLauncher)
		MinecraftPatcher.unzip(minecraftLauncher)
		MinecraftPatcher.copyMods(minecraftLauncher, modData)
		MinecraftPatcher.zip(minecraftLauncher)
		return*/
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
	
}