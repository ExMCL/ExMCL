package com.n9mtq4.exmcl.hooks

import com.n9mtq4.exmcl.api.hooks.events.SwingUserInterfaceEvent
import com.n9mtq4.exmcl.hooks.override.HookedSwingUserInterface
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.ObjectEvent
import com.n9mtq4.logwindow.listener.ObjectListener
import com.n9mtq4.reflection.ReflectionHelper
import net.minecraft.launcher.Launcher
import net.minecraft.launcher.SwingUserInterface

/**
 * Created by will on 7/27/15 at 5:58 PM.
 */
class SwingUserInterfaceHook : ObjectListener {
	
	override fun objectReceived(e: ObjectEvent, baseConsole: BaseConsole) {
		
		if (!e.message.equals("minecraftlauncher", ignoreCase = true)) return
		if (e.obj !is Launcher) return
		
		val ui = ReflectionHelper.getObject<SwingUserInterface>("userInterface", e.obj)
		
		if (HOOK_INTERFACE) {
			try {
				val launcher = e.obj as Launcher
				val hookedSwingUserInterface = HookedSwingUserInterface(ui, baseConsole)
				ReflectionHelper.setObject(hookedSwingUserInterface, "userInterface", launcher)
			} catch (e1: Exception) {
				e1.printStackTrace()
				baseConsole.printStackTrace(e1)
				System.err.println("SwingUserInterface Hook not working...")
				baseConsole.println("SwingUserInterface Hook not working...")
			}
			
		}
		
		e.initiatingBaseConsole.pushEvent(SwingUserInterfaceEvent(baseConsole, ui))
		
	}
	
	companion object {
		
		private val HOOK_INTERFACE = true
	}
	
}
