package com.n9mtq4.exmcl.hooks

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.reflection.ReflectionHelper
import net.minecraft.launcher.ui.BottomBarPanel
import net.minecraft.launcher.ui.bottombar.PlayButtonPanel

/**
 * Created by will on 7/27/15 at 6:23 PM.
 */
class PlayButtonPanelHook : GenericListener {
	
	@Suppress("unused")
	@ListensFor(PreDefinedSwingHookEvent::class)
	fun ListensForSwingEvent(e: PreDefinedSwingHookEvent, baseConsole: BaseConsole) {
		
		if (e.type !== PreDefinedSwingComponent.BOTTOM_BAR_PANEL) return
		
		val bottomBarPanel = e.component as BottomBarPanel
		val playButtonPanel: PlayButtonPanel = ReflectionHelper.getObject("playButtonPanel", bottomBarPanel)
		
		baseConsole.pushEvent(PreDefinedSwingHookEvent(playButtonPanel, PreDefinedSwingComponent.PLAY_BUTTON_PANEL, baseConsole))
		
	}
	
}
