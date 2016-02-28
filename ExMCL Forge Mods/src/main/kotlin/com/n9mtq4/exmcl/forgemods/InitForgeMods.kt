@file:Suppress("unused", "UNUSED_PARAMETER")
package com.n9mtq4.exmcl.forgemods

import com.n9mtq4.exmcl.api.cleaner.AddToDelete
import com.n9mtq4.exmcl.api.hooks.events.MinecraftLauncherEvent
import com.n9mtq4.exmcl.api.tabs.events.CreateTabEvent
import com.n9mtq4.exmcl.api.tabs.events.SafeForTabCreationEvent
import com.n9mtq4.exmcl.forgemods.ui.ForgeTab
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.Async
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.events.EnableEvent
import com.n9mtq4.logwindow.listener.EnableListener
import com.n9mtq4.logwindow.listener.GenericListener
import net.minecraft.launcher.Launcher
import java.io.File
import javax.swing.SwingUtilities

/**
 * Created by will on 2/14/16 at 11:33 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class InitForgeMods : GenericListener, EnableListener {
	
	private lateinit var minecraftLauncher: Launcher
	
	override fun onEnable(enableEvent: EnableEvent) {
		
		// adds tml/ to be deleted
		enableEvent.baseConsole.pushEvent(AddToDelete(File("tmp/"), enableEvent.baseConsole))
		
		// adds forge's log junk to be deleted
		val workingDir: File = File(System.getProperty("user.dir"))
		val children: Array<File> = workingDir.listFiles()
		children.filter { it.name.contains("forge_") && it.name.endsWith(".jar.log") }.
				forEach { enableEvent.baseConsole.pushEvent(AddToDelete(it, enableEvent.baseConsole)) }
		
	}
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForMinecraftLauncher(e: MinecraftLauncherEvent, baseConsole: BaseConsole) {
		
		System.out.println("HELLO WORLD")
		this.minecraftLauncher = e.minecraftLauncher
		
	}
	
	@Suppress("unused")
	@Async
	@ListensFor
	fun listenForTabSafe(e: SafeForTabCreationEvent, baseConsole: BaseConsole) {
		
		SwingUtilities.invokeLater { 
			try {
				val forgeTab = ForgeTab(minecraftLauncher, baseConsole)
				baseConsole.pushEvent(CreateTabEvent("Forge Mods", forgeTab, baseConsole))
			}catch (e: Exception) {
				e.printStackTrace()
			}
		}
		
		baseConsole.disableListenerAttribute(this)
		
	}
	
}
