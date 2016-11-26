package com.n9mtq4.exmcl.branding

import com.n9mtq4.exmcl.api.BUILD_NUMBER
import com.n9mtq4.exmcl.api.TIME_STAMP
import com.n9mtq4.exmcl.api.consoleprinter.PrintlnToConsole
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import java.util.Date

/**
 * Created by will on 11/25/16 at 11:52 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class InfoPrinter : GenericListener {
	
	@ListensFor(PreDefinedSwingHookEvent::class)
	fun listenForSwingHook(e: PreDefinedSwingHookEvent, baseConsole: BaseConsole) {
		if (e.type != PreDefinedSwingComponent.LAUNCHER_TAB_PANEL) return
		
		val msg = "The Extendable Minecraft Launcher\nExMCL using API #$BUILD_NUMBER, built on ${Date(TIME_STAMP)}"
		
		baseConsole.pushEvent(PrintlnToConsole(msg, baseConsole))
		
	}
	
}
