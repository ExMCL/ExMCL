package com.n9mtq4.exmcl.api.hooks.events

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import java.awt.event.ActionEvent

/**
 * Created by will on 3/8/16 at 10:20 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class DefaultGameLaunchEvent(val actionEvent: ActionEvent, initiatingBaseConsole: BaseConsole) : DefaultGenericEvent(initiatingBaseConsole)
