package com.n9mtq4.exmcl.api.cleaner

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import java.io.File

/**
 * Created by will on 2/14/16 at 6:40 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class AddToDelete(val file: File, initiatingBaseConsole: BaseConsole?) : DefaultGenericEvent(initiatingBaseConsole) {
	
}
