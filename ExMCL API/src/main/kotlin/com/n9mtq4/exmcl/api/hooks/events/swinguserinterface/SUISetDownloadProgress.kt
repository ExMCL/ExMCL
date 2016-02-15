package com.n9mtq4.exmcl.api.hooks.events.swinguserinterface

import com.mojang.launcher.updater.DownloadProgress
import com.n9mtq4.logwindow.BaseConsole
import net.minecraft.launcher.SwingUserInterface

/**
 * Created by will on 2/14/16 at 8:06 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class SUISetDownloadProgress(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val downloadProgress: DownloadProgress) : 
		SwingUserInterfaceEvent(baseConsole, swingUserInterface) {
}
