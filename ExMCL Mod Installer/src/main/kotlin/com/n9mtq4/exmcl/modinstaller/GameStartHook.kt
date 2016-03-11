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
import javax.swing.JOptionPane
import kotlin.concurrent.thread

/**
 * Created by will on 3/6/16 at 11:49 PM.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
class GameStartHook(val minecraftLauncher: Launcher, val modData: ModData) : GenericListener {
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForGameLaunch(e: GameLaunchEvent, baseConsole: BaseConsole) {
		
		if (modData.getSelectedProfile().modList.filter { it.enabled }.size == 0) return // don't patch when there are no mods
		
		val mcPatcher = MinecraftPatcher(minecraftLauncher, modData.getSelectedProfile())
		
		if (!e.isCanceled) {
			
//			if e isn't canceled - for support for other plugins. The progress bar isn't worth breaking things over
			
			e.isCanceled = true
			
			thread(start = true) {
				
				println("Patching")
				try {
					mcPatcher.patch() { step, total, task ->
						minecraftLauncher.userInterface.setDownloadProgress(DownloadProgress(step.toLong(), total.toLong(), "Patching mods into Minecraft: $task"))
					}
				}catch (e1: Exception) {
					JOptionPane.showMessageDialog(e.actionEvent.source as Component, "${e1.cause}\n${e1.message}", "Error Patching", JOptionPane.ERROR_MESSAGE)
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
			try {
				mcPatcher.patch() { i, i1, task -> /*do nothing*/ }
			}catch (e1: Exception) {
				JOptionPane.showMessageDialog(e.actionEvent.source as Component, "${e1.cause}\n${e1.message}", "Error Patching", JOptionPane.ERROR_MESSAGE)
			}
			println("Patched")
//			
			pw.dispose()
			
		}
		
	}
	
}
