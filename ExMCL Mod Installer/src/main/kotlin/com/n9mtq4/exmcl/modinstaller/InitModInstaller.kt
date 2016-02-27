@file:Suppress("unused", "UNUSED_PARAMETER")
package com.n9mtq4.exmcl.modinstaller

import com.n9mtq4.exmcl.api.hooks.events.MinecraftLauncherEvent
import com.n9mtq4.exmcl.api.tabs.events.CreateTabEvent
import com.n9mtq4.exmcl.api.tabs.events.SafeForTabCreationEvent
import com.n9mtq4.exmcl.modinstaller.ui.ModsTab
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.Async
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import net.minecraft.launcher.Launcher
import javax.swing.SwingUtilities

/**
 * Created by will on 2/17/16 at 5:22 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class InitModInstaller : GenericListener {
	
	private lateinit var minecraftLauncher: Launcher
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForMinecraftLauncher(e: MinecraftLauncherEvent, baseConsole: BaseConsole) {
		
		this.minecraftLauncher = e.minecraftLauncher
		
	}
	
	@Suppress("unused")
	@Async
	@ListensFor
	fun listenForTabSafe(e: SafeForTabCreationEvent, baseConsole: BaseConsole) {
		
		SwingUtilities.invokeLater {
			val modsTab = ModsTab(minecraftLauncher, baseConsole)
			baseConsole.pushEvent(CreateTabEvent("Jar Mods", modsTab, baseConsole))
		}
		
		baseConsole.disableListenerAttribute(this)
		
	}
	
}
