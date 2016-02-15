package com.n9mtq4.exmcl.api.hooks.events.swinguserinterface

import com.n9mtq4.logwindow.BaseConsole
import net.minecraft.launcher.Launcher
import net.minecraft.launcher.SwingUserInterface
import net.minecraft.launcher.ui.popups.login.LogInPopup

/**
 * Created by will on 2/14/16 at 7:57 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class SUIShowLoginPromptCallback(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val minecraftLauncher: Launcher, val callBack: LogInPopup.Callback) : 
		SwingUserInterfaceEvent(baseConsole, swingUserInterface) {
}
