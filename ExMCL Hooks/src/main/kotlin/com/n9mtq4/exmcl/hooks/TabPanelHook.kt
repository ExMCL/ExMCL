package com.n9mtq4.exmcl.hooks

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.reflection.ReflectionHelper
import net.minecraft.launcher.ui.LauncherPanel
import net.minecraft.launcher.ui.tabs.LauncherTabPanel

/**
 * Created by will on 7/27/15 at 5:56 PM.
 */
class TabPanelHook : GenericListener {
	
	/**
	 * This listens for the minecraft launcher to be sent,
	 * and then sends a LauncherTabPanel back
	 */
	@Suppress("unused")
	@ListensFor(PreDefinedSwingHookEvent::class)
	fun ListensForSwingEvent(e: PreDefinedSwingHookEvent, baseConsole: BaseConsole) {
		
		if (e.type !== PreDefinedSwingComponent.LAUNCHER_PANEL) return
		
		val launcherPanel = e.component as LauncherPanel
		val launcherTabPanel: LauncherTabPanel = ReflectionHelper.getObject("tabPanel", launcherPanel)
		
		baseConsole.pushEvent(PreDefinedSwingHookEvent(launcherTabPanel, PreDefinedSwingComponent.LAUNCHER_TAB_PANEL, baseConsole))
		
	}
	
}
