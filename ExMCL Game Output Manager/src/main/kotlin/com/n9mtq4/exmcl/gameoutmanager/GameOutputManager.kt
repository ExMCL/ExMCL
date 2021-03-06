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

@file:JvmName("GameOutputManager")
package com.n9mtq4.exmcl.gameoutmanager

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIShowGameOutputTab
import com.n9mtq4.exmcl.api.tabs.events.LowLevelCreateTabEvent
import com.n9mtq4.exmcl.api.tabs.events.SafeForLowLevelTabCreationEvent
import com.n9mtq4.kotlin.extlib.pstAndUnit
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
	@ListensFor
	fun listenForShowGameOutputTab(e: PostSUIShowGameOutputTab, baseConsole: BaseConsole) {
		fireNewGameOutputTab()
	}
	
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
			if (tab is GameOutputTab) addNewTab(i, launcherTabPanel.getTitleAt(i), tab)
		}
	}
	
	private fun addNewTab(index: Int, title: String, component: Component) {
		SwingUtilities.invokeLater {
			launcherTabPanel.removeTabAt(index)
			gameOutputTabs.add(cropTabName(title), component)
			gameOutputTabs.selectedComponent = component // select the game output. this is the default behavior of vanilla launcher
		}
	}
	
	private fun addGameOutputTabs() = SwingUtilities.invokeLater {
		pstAndUnit {
			parent.pushEvent(LowLevelCreateTabEvent("Game Output", gameOutputTabs, parent))
		}
	}
	
	private fun cropTabName(title: String) = title.substring(title.indexOf("(") + 1, title.indexOf(")"))
	
}
