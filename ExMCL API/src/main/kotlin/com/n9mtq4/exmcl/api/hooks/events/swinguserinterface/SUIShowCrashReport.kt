package com.n9mtq4.exmcl.api.hooks.events.swinguserinterface

import com.mojang.launcher.versions.CompleteVersion
import com.n9mtq4.logwindow.BaseConsole
import net.minecraft.launcher.SwingUserInterface
import java.io.File

/**
 * Created by will on 2/14/16 at 8:08 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class SUIShowCrashReport(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val version: CompleteVersion, val crashReportFile: File, val crashReport: String) : 
		SwingUserInterfaceEvent(baseConsole, swingUserInterface) {
}
