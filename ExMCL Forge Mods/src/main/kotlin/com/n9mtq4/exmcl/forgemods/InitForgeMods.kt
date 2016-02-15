package com.n9mtq4.exmcl.forgemods

import com.n9mtq4.exmcl.api.tabs.events.CreateTabEvent
import com.n9mtq4.exmcl.api.tabs.events.SafeForTabCreationEvent
import com.n9mtq4.exmcl.forgemods.ui.ForgeTab
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.Async
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import javax.swing.SwingUtilities

/**
 * Created by will on 2/14/16 at 11:33 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class InitForgeMods : GenericListener {
	
	@Async
	@ListensFor
	fun listenForTabSafe(e: SafeForTabCreationEvent, baseConsole: BaseConsole) {
		
		SwingUtilities.invokeLater { 
			val forgeTab = ForgeTab(baseConsole)
			baseConsole.pushEvent(CreateTabEvent("Forge Mods", forgeTab, baseConsole))
		}
		
		baseConsole.disableListenerAttribute(this)
		
	}
	
}
