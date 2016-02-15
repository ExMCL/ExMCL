package com.n9mtq4.exmcl.api.hooks.events.swinguserinterface

import com.n9mtq4.logwindow.BaseConsole
import net.minecraft.launcher.SwingUserInterface
import net.minecraft.launcher.game.MinecraftGameRunner

/**
 * Created by will on 2/14/16 at 7:52 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class SUIShowGameOutputTabEvent(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val minecraftGameRunner: MinecraftGameRunner) : 
		SwingUserInterfaceEvent(baseConsole, swingUserInterface) {
	
	
}
