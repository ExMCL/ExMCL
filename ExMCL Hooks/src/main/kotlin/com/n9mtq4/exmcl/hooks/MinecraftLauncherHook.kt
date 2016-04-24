package com.n9mtq4.exmcl.hooks

import com.n9mtq4.exmcl.api.hooks.events.MinecraftLauncherEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.ObjectEvent
import com.n9mtq4.logwindow.listener.ObjectListener
import net.minecraft.launcher.Launcher

/**
 * Created by will on 2/15/16 at 8:02 PM.
 * 
 * A low level listener for the minecraft launcher
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
class MinecraftLauncherHook : ObjectListener {
	
	override fun objectReceived(e: ObjectEvent, baseConsole: BaseConsole) {
		
		if (e.message != "minecraftlauncher") return
		if (e.obj !is Launcher) return
		
		baseConsole.pushEvent(MinecraftLauncherEvent(e.obj as Launcher, baseConsole))
		
	}
	
}
