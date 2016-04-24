package com.n9mtq4.exmcl.hooks

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.reflection.ReflectionHelper
import net.minecraft.launcher.ui.BottomBarPanel
import net.minecraft.launcher.ui.LauncherPanel

/**
 * Created by will on 7/28/15 at 2:25 PM.
 */
class BottomBarPanelHook : GenericListener {
	
	@Suppress("unused")
	@ListensFor(PreDefinedSwingHookEvent::class)
	fun ListensForSwingEvent(e: PreDefinedSwingHookEvent, baseConsole: BaseConsole) {
		
		if (e.type !== PreDefinedSwingComponent.LAUNCHER_PANEL) return
		
		val launcherPanel = e.component as LauncherPanel
		val bottomBarPanel = ReflectionHelper.getObject<BottomBarPanel>("bottomBar", launcherPanel)
		
		baseConsole.pushEvent(PreDefinedSwingHookEvent(bottomBarPanel, PreDefinedSwingComponent.BOTTOM_BAR_PANEL, baseConsole))
		
	}
	
}
