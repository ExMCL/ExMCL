package com.n9mtq4.exmcl.api.hooks.events

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import java.awt.Component

/**
 * Created by will on 4/23/16 at 10:01 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class AllSwingComponentsEvent(val swingComponent: Component, initiatingBaseConsole: BaseConsole) : DefaultGenericEvent(initiatingBaseConsole) {
	override fun toString() = "${this.javaClass.name}{swingComponent=$swingComponent}"
}
