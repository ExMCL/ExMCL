package com.n9mtq4.exmcl.api.hooks.events

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import net.minecraft.launcher.Launcher

/**
 * Created by will on 2/15/16 at 8:01 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class MinecraftLauncherEvent(val minecraftLauncher: Launcher, initiatingBaseConsole: BaseConsole) : 
		DefaultGenericEvent(initiatingBaseConsole) {
}
