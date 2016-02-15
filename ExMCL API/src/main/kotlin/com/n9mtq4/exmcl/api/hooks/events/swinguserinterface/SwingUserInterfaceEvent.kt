package com.n9mtq4.exmcl.api.hooks.events.swinguserinterface

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import net.minecraft.launcher.SwingUserInterface

/**
 * Created by will on 2/14/16 at 7:51 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
open class SwingUserInterfaceEvent(baseConsole: BaseConsole, val swingUserInterface: SwingUserInterface) : DefaultGenericEvent(baseConsole) {
	
}
