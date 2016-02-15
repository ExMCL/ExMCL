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
	val addMod: JButton
	val removeMod: JButton
	
	init {
		
		this.modData = ModData.Loader.load()
		
		isEnabled = false
		
//		set up table and scrolling
		this.buttonPanel = JPanel(GridLayout(5, 1))
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
		this.addMod = JButton("Add Mod")
		this.removeMod = JButton("Remove Mod")
		addMod.toolTipText = "Add mods the the current profile.\nYou can also drag and drop mods into the table."
		buttonPanel.apply {
			add(installForge)
			add(addProfile)
			add(removeProfile)
			add(addMod)
			add(removeMod)
		}
		installForge.addActionListener(this)
		addProfile.addActionListener(this)
		removeProfile.addActionListener(this)
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
		val text = button.text
		
		if (text.equals("dev", true)) {
			println("This is a placeholder for testing.")
		}else if (text.equals("install forge", true)) {
			InstallForgeDialog(this)
		}else if (text.equals("new profile", true)) {
			val profileName: String? = JOptionPane.showInputDialog(this, "What should the profile be called?")
			if (profileName == null ||profileName.isNullOrBlank()) return
			val profile: ModProfile = ModProfile(profileName)
			modData.profiles.add(profile)
			refresh()
		}else if (text.equals("delete profile", true)) {
			if (modData.profiles[modData.selectedProfileIndex].profileName.equals("default", true)) {
				JOptionPane.showMessageDialog(this, "You can't delete the default profile.", "Error", JOptionPane.ERROR_MESSAGE)
				return
			}
			val sure = JOptionPane.showConfirmDialog(this,
					"Are you sure you want to delete\nthe profile ${modData.getProfileNames()[modData.selectedProfileIndex]}?",
					"Delete?", JOptionPane.YES_NO_OPTION)
			if (sure == 1) {
				modData.profiles.removeAt(modData.selectedProfileIndex)
				modData.selectedProfileIndex = 0;
				refreshList()
			}
		}else if (text.equals("add mod", true)) {
//			val mods = FileBrowseUtils.promptOpen(this)
			val mods = browseForMods(this)
			if (mods.size == 0) return
			for (mod in mods) {
				modData.profiles[modData.selectedProfileIndex].addMod(mod)
			}
			table.refreshModel()
		}else if (text.equals("remove mod", true)) {
			val row = table.selectedRow
			val selectedProfile = modData.profiles[modData.selectedProfileIndex]
			if (row < 0) {
				JOptionPane.showMessageDialog(this, "You haven't selected a mod to remove!", "Error", JOptionPane.ERROR_MESSAGE)
				return
			}
			selectedProfile.modList.removeAt(row)
			table.refreshModel()
		}
		
	}
	
	override fun valueChanged(e: ListSelectionEvent) {
		
		if (list.selectedIndex < 0) return
		modData.selectedProfileIndex = list.selectedIndex
		table.refreshModel()
		
	}
	
}
