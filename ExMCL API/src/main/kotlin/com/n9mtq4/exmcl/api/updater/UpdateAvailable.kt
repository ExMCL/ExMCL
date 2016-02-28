package com.n9mtq4.exmcl.api.updater

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import java.util.HashMap

/**
 * Created by will on 2/27/16 at 4:18 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
@Suppress("unused", "UNUSED_PARAMETER")
class UpdateAvailable(initiatingBaseConsole: BaseConsole, val updateInfo: HashMap<*, *>, val targetBuildNumber: Int) : DefaultGenericEvent(initiatingBaseConsole) {
	
	override fun toString() = "UpdateEvent{targetBuildNumber=$targetBuildNumber, updateInfo=${updateInfo.toString()}}"
	
}
