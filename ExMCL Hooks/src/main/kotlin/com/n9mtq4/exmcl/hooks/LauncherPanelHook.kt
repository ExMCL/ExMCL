package com.n9mtq4.exmcl.hooks

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent
import com.n9mtq4.exmcl.api.hooks.events.SwingUserInterfaceEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.reflection.ReflectionHelper
import net.minecraft.launcher.ui.LauncherPanel

/**
 * Created by will on 7/27/15 at 6:04 PM.
 */
class LauncherPanelHook : GenericListener {
	
	@Suppress("unused")
	@ListensFor(SwingUserInterfaceEvent::class)
	fun ListensForSwingEvent(e: SwingUserInterfaceEvent, baseConsole: BaseConsole) {
		
		val ui = e.swingUserInterface
		val launcherPanel: LauncherPanel = ReflectionHelper.getObject("launcherPanel", ui)
		
		baseConsole.pushEvent(PreDefinedSwingHookEvent(launcherPanel, PreDefinedSwingComponent.LAUNCHER_PANEL, baseConsole))
		
	}
	
}
