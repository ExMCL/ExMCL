package com.n9mtq4.exmcl.api.hooks.events.swinguserinterface

import com.n9mtq4.logwindow.BaseConsole
import net.minecraft.launcher.SwingUserInterface

/**
 * Created by will on 2/14/16 at 8:05 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class SUIShutdownLauncher(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : 
		SwingUserInterfaceEvent(baseConsole, swingUserInterface) {
}
