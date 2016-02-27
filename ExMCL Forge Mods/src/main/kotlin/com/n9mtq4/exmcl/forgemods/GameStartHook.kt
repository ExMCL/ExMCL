@file:Suppress("unused", "UNUSED_PARAMETER")
package com.n9mtq4.exmcl.forgemods

import com.n9mtq4.exmcl.api.hooks.events.GameLaunchEvent
import com.n9mtq4.exmcl.forgemods.data.ModData
import com.n9mtq4.exmcl.forgemods.utils.cleanup
import com.n9mtq4.exmcl.forgemods.utils.copyToMods
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import net.minecraft.launcher.Launcher

/**
 * Created by will on 2/15/16 at 7:10 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class GameStartHook(val minecraftLauncher: Launcher, val modData: ModData) : GenericListener {
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForGameLaunch(e: GameLaunchEvent, baseConsole: BaseConsole) {
		
		cleanup(minecraftLauncher)
		val selectedProfile = modData.getSelectedProfile()
		copyToMods(minecraftLauncher, selectedProfile)
		
	}
	
}
