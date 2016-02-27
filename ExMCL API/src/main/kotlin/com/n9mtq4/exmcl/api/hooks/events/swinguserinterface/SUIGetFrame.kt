package com.n9mtq4.exmcl.api.hooks.events.swinguserinterface

import com.n9mtq4.logwindow.BaseConsole
import net.minecraft.launcher.SwingUserInterface
import javax.swing.JFrame

/**
 * Created by will on 2/14/16 at 8:13 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class SUIGetFrame(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : 
		SwingUserInterfaceEvent(baseConsole, swingUserInterface) {
	
	var frame: JFrame? = null
	
}
