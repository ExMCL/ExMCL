@file:Suppress("unused", "UNUSED_PARAMETER")

package com.n9mtq4.exmcl.api.hooks.events.swinguserinterface

import com.mojang.launcher.events.GameOutputLogProcessor
import com.mojang.launcher.updater.DownloadProgress
import com.mojang.launcher.versions.CompleteVersion
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import net.minecraft.launcher.Launcher
import net.minecraft.launcher.SwingUserInterface
import net.minecraft.launcher.game.MinecraftGameRunner
import net.minecraft.launcher.ui.popups.login.LogInPopup
import java.io.File
import javax.swing.JFrame

/**
 * Created by will on 2/27/16 at 2:22 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
open class SUIEvent(baseConsole: BaseConsole, val swingUserInterface: SwingUserInterface) : DefaultGenericEvent(baseConsole) {
	var stopMojangsCode: Boolean = false
}

class SUIUpdatePlayState(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : SUIEvent(baseConsole, swingUserInterface)
class SUIShutdownLauncher(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : SUIEvent(baseConsole, swingUserInterface)
class SUIShowOutdatedNotice(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : SUIEvent(baseConsole, swingUserInterface)
class SUIShowLoginPromptCallback(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val minecraftLauncher: Launcher, val callBack: LogInPopup.Callback) : SUIEvent(baseConsole, swingUserInterface)
class SUIShowLoginPrompt(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : SUIEvent(baseConsole, swingUserInterface)
class SUIShowGameOutputTab(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val minecraftGameRunner: MinecraftGameRunner) : SUIEvent(baseConsole, swingUserInterface) {
	var gameOutputLogProcessor: GameOutputLogProcessor? = null
}
class SUIShowCrashReport(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val version: CompleteVersion, val crashReportFile: File, val crashReport: String) : SUIEvent(baseConsole, swingUserInterface)
class SUISetVisible(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val visible: Boolean) : SUIEvent(baseConsole, swingUserInterface)
class SUISetDownloadProgress(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val downloadProgress: DownloadProgress) : SUIEvent(baseConsole, swingUserInterface)
class SUIInitializeFrame(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : SUIEvent(baseConsole, swingUserInterface)
class SUIHideDownloadProgress(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : SUIEvent(baseConsole, swingUserInterface)
class SUIGetTitle(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : SUIEvent(baseConsole, swingUserInterface) {
	var title: String? = null
}
class SUIGetFrame(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : SUIEvent(baseConsole, swingUserInterface) {
	var frame: JFrame? = null
}
class SUIGameLaunchFailure(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val reason: String) : SUIEvent(baseConsole, swingUserInterface)
