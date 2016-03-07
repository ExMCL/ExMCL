package com.n9mtq4.exmcl.modinstaller

import com.n9mtq4.exmcl.api.hooks.events.GameLaunchEvent
import com.n9mtq4.exmcl.modinstaller.data.ModData
import com.n9mtq4.exmcl.modinstaller.utils.MinecraftPatcher
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import net.minecraft.launcher.Launcher

/**
 * Created by will on 3/6/16 at 11:49 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class GameStartHook(val minecraftLauncher: Launcher, val modData: ModData) : GenericListener {
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForGameLaunch(e: GameLaunchEvent, baseConsole: BaseConsole) {
		
		val mcPatcher = MinecraftPatcher(minecraftLauncher, modData.getSelectedProfile())
		mcPatcher.patch()
		
	}
	
}
