package com.n9mtq4.exmcl.modinstaller

import com.mojang.launcher.updater.DownloadProgress
import com.n9mtq4.exmcl.api.hooks.events.DefaultGameLaunchEvent
import com.n9mtq4.exmcl.api.hooks.events.GameLaunchEvent
import com.n9mtq4.exmcl.modinstaller.data.ModData
import com.n9mtq4.exmcl.modinstaller.ui.PatchingWindow
import com.n9mtq4.exmcl.modinstaller.utils.MinecraftPatcher
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import net.minecraft.launcher.Launcher
import java.awt.Component
import kotlin.concurrent.thread

/**
 * Created by will on 3/6/16 at 11:49 PM.
 * 
 * FIXME: the PatchingWindow doesn't init its progress bar!?
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
class GameStartHook(val minecraftLauncher: Launcher, val modData: ModData) : GenericListener {
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForGameLaunch(e: GameLaunchEvent, baseConsole: BaseConsole) {
		
		if (modData.getSelectedProfile().modList.size == 0) return // don't patch when there are no mods
		
		val mcPatcher = MinecraftPatcher(minecraftLauncher, modData.getSelectedProfile())
		
		if (!e.isCanceled) {
			
//			if e isn't canceled - for support for other plugins. The progress bar isn't worth breaking things over
			
			e.isCanceled = true
			
			thread(start = true) {
				
				println("Patching")
				mcPatcher.patch() { step, total, task -> 
					minecraftLauncher.userInterface.setDownloadProgress(DownloadProgress(step.toLong(), total.toLong(), "Patching mods into Minecraft: $task"))
				}
				println("Patched")
				
				baseConsole.pushEvent(DefaultGameLaunchEvent(e.actionEvent, baseConsole))
				
				minecraftLauncher.userInterface.hideDownloadProgress()
				
			}
			
		}else {
			
//			there is some other plugin that canceled it
//			fall back to the old method
			
			val pw = PatchingWindow(e.actionEvent.source as Component)
			
			println("Patching")
			mcPatcher.patch() { i, i1, task -> /*do nothing*/ }
			println("Patched")
//			
			pw.dispose()
			
		}
		
	}
	
}
