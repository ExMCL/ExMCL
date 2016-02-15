package com.n9mtq4.exmcl.api.hooks.events.swinguserinterface

import com.n9mtq4.logwindow.BaseConsole
import net.minecraft.launcher.SwingUserInterface

/**
 * Created by will on 2/14/16 at 8:00 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class SUIInitializeFrame(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : 
		SwingUserInterfaceEvent(baseConsole, swingUserInterface) {
}
