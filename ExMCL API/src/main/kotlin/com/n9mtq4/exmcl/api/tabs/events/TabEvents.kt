package com.n9mtq4.exmcl.api.tabs.events

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import java.awt.Component

/**
 * Created by will on 2/27/16 at 3:09 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class CreateTabEvent(val title: String, val component: Component, baseConsole: BaseConsole) : DefaultGenericEvent(baseConsole)
class LowLevelCreateTabEvent(val title: String, val component: Component, baseConsole: BaseConsole) : DefaultGenericEvent(baseConsole)
class SafeForLowLevelTabCreationEvent(initiatingBaseConsole: BaseConsole) : DefaultGenericEvent(initiatingBaseConsole)
class SafeForTabCreationEvent(initiatingBaseConsole: BaseConsole) : DefaultGenericEvent(initiatingBaseConsole)
