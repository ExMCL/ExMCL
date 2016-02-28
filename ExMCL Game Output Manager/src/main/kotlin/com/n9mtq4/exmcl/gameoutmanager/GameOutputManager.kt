@file:JvmName("GameOutputManager")
package com.n9mtq4.exmcl.gameoutmanager

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowGameOutputTab
import com.n9mtq4.exmcl.api.tabs.events.LowLevelCreateTabEvent
import com.n9mtq4.exmcl.api.tabs.events.SafeForLowLevelTabCreationEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.events.EnableEvent
import com.n9mtq4.logwindow.listener.EnableListener
import com.n9mtq4.logwindow.listener.GenericListener
import net.minecraft.launcher.ui.tabs.GameOutputTab
import net.minecraft.launcher.ui.tabs.LauncherTabPanel
import java.awt.Component
import javax.swing.JTabbedPane
import javax.swing.SwingUtilities

/**
 * Created by will on 2/14/16 at 7:43 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class GameOutputManager : EnableListener, GenericListener {
	
	private lateinit var parent: BaseConsole
	private var tabSafe = false
	private var added = false
	private val gameOutputTabs: JTabbedPane by lazy { JTabbedPane() }
	private lateinit var launcherTabPanel: LauncherTabPanel
	
	override fun onEnable(e: EnableEvent) {
		this.parent = e.baseConsole
	}
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor(SUIShowGameOutputTab::class)
	fun listenForShowGameOutputTab(e: SUIShowGameOutputTab, baseConsole: BaseConsole) = fireNewGameOutputTab()
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor(SafeForLowLevelTabCreationEvent::class)
	fun listenForTabSafe(e: SafeForLowLevelTabCreationEvent, baseConsole: BaseConsole) {
		this.tabSafe = true
	}
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor(PreDefinedSwingHookEvent::class)
	fun listenForLauncherTabPanel(e: PreDefinedSwingHookEvent, baseConsole: BaseConsole) {
		if (e.type == PreDefinedSwingComponent.LAUNCHER_TAB_PANEL) launcherTabPanel = e.component as LauncherTabPanel
	}
	
	private fun fireNewGameOutputTab() {
		if (!added) {
			addGameOutputTabs()
			added = true
		}
		for (i in 0..launcherTabPanel.tabCount - 1) {
			val tab = launcherTabPanel.getComponentAt(i)
			if (tab is GameOutputTab) addNewTab(launcherTabPanel.getTitleAt(i), tab)
		}
	}
	
	private fun addNewTab(title: String, component: Component) {
		gameOutputTabs.add(cropTabName(title), component)
	}
	
	private fun addGameOutputTabs() = SwingUtilities.invokeLater {
		try {
			parent.pushEvent(LowLevelCreateTabEvent("Game Output", gameOutputTabs, parent))
		}catch (e: Exception) {
			e.printStackTrace()
		}
	}
	
	private fun cropTabName(title: String) = title.substring(title.indexOf("(") + 1, title.indexOf(")"))
	
}
