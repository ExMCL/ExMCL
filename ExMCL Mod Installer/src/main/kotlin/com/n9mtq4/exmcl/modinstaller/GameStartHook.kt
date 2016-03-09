package com.n9mtq4.exmcl.modinstaller

import com.n9mtq4.exmcl.api.hooks.events.GameLaunchEvent
import com.n9mtq4.exmcl.modinstaller.data.ModData
import com.n9mtq4.exmcl.modinstaller.ui.PatchingWindow
import com.n9mtq4.exmcl.modinstaller.utils.MinecraftPatcher
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import net.minecraft.launcher.Launcher
import java.awt.Component

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
		
		var pw: PatchingWindow = PatchingWindow(e.actionEvent.source as Component)
		
		mcPatcher.patch()
		
		pw.dispose()
		
	}
	
}
