package com.n9mtq4.exmcl.api.hooks.events

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import java.awt.event.ActionEvent

/**
 * Created by will on 2/15/16 at 7:32 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class GameLaunchEvent(val actionEvent: ActionEvent, initiatingBaseConsole: BaseConsole) : DefaultGenericEvent(initiatingBaseConsole) {
	
}
