package com.n9mtq4.exmcl.api.hooks.events

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import net.minecraft.launcher.SwingUserInterface

/**
 * Created by will on 2/27/16 at 4:26 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
@Suppress("unused", "UNUSED_PARAMETER")
class SwingUserInterfaceEvent(initiatingBaseConsole: BaseConsole, val swingUserInterface: SwingUserInterface) : DefaultGenericEvent(initiatingBaseConsole) {
	override fun toString() = "${this.javaClass.name}{swingUserInterface=$swingUserInterface}"
}
