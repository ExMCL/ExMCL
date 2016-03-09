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
import kotlin.concurrent.thread

/**
 * Created by will on 3/6/16 at 11:49 PM.
 * 
 * TODO: OMG the threads!!!
 * FIXME: the PatchingWindow doesn't init its progress bar!?
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
class GameStartHook(val minecraftLauncher: Launcher, val modData: ModData) : GenericListener {
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForGameLaunch(e: GameLaunchEvent, baseConsole: BaseConsole) {
		
//		TODO: this should be done on the swing thread
		var pw: PatchingWindow? = null
		val pwt = thread(start = true) { pw = PatchingWindow(e.actionEvent.source as Component) }
		
//		TODO: what's kotlin's version of notify and wait?
		val t: Thread = object : Thread("MC Patcher") {
			override fun run() {
				synchronized(this) {
					val mcPatcher = MinecraftPatcher(minecraftLauncher, modData.getSelectedProfile())
					mcPatcher.patch()
					(this as java.lang.Object).notify() // TODO: very bad
				}
			}
		}.apply { start() }
		
		synchronized(t) {
			(t as java.lang.Object).wait() // TODO: very bad
		}
		
		pw?.dispose()
		pwt.join()
		
	}
	
}
