package com.n9mtq4.exmcl.api.tabs.events

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import java.awt.Component

/**
 * Created by will on 2/13/16 at 12:48 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class LowLevelCreateTabEvent(val title: String, val component: Component, baseConsole: BaseConsole) : DefaultGenericEvent(baseConsole) {
	
}
