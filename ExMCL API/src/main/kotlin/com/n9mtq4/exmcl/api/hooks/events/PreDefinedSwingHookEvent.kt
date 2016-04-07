package com.n9mtq4.exmcl.api.hooks.events

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent

/**
 * Created by will on 2/16/16 at 5:57 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
@Suppress("unused", "UNUSED_PARAMETER")
class PreDefinedSwingHookEvent(val component: Any, val type: PreDefinedSwingComponent, initiatingBaseConsole: BaseConsole) :
		DefaultGenericEvent(initiatingBaseConsole) {
	override fun toString() = "${this.javaClass.name} carrying type: ${type.name} with value ${component.toString()}"
}
