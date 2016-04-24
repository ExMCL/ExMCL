package com.n9mtq4.exmcl.hooks

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.reflection.ReflectionHelper
import net.minecraft.launcher.ui.bottombar.PlayButtonPanel

import javax.swing.JButton

/**
 * Created by will on 7/27/15 at 6:15 PM.
 */
class PlayButtonHook : GenericListener {
	
	@Suppress("unused")
	@ListensFor(PreDefinedSwingHookEvent::class)
	fun ListensForSwingEvent(e: PreDefinedSwingHookEvent, baseConsole: BaseConsole) {
		
		if (e.type !== PreDefinedSwingComponent.PLAY_BUTTON_PANEL) return
		
		val playButtonPanel = e.component as PlayButtonPanel
		val playButton = ReflectionHelper.getObject<JButton>("playButton", playButtonPanel)
		
		baseConsole.pushEvent(PreDefinedSwingHookEvent(playButton, PreDefinedSwingComponent.PLAY_BUTTON, baseConsole))
		
	}
	
}
