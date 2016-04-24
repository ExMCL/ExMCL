/*
 * MIT License
 *
 * Copyright (c) 2016 Will (n9Mtq4) Bresnahan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
 * Too many classes in very dense code
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

open class PostSUIEvent(baseConsole: BaseConsole, val swingUserInterface: SwingUserInterface) : DefaultGenericEvent(baseConsole)
class PostSUIUpdatePlayState(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUIShutdownLauncher(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUIShowOutdatedNotice(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUIShowLoginPromptCallback(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val minecraftLauncher: Launcher, val callBack: LogInPopup.Callback) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUIShowLoginPrompt(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUIShowGameOutputTab(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val minecraftGameRunner: MinecraftGameRunner) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUIShowCrashReport(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val version: CompleteVersion, val crashReportFile: File, val crashReport: String) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUISetVisible(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val visible: Boolean) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUISetDownloadProgress(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val downloadProgress: DownloadProgress) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUIInitializeFrame(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUIHideDownloadProgress(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUIGetTitle(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUIGetFrame(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface) : PostSUIEvent(baseConsole, swingUserInterface)
class PostSUIGameLaunchFailure(baseConsole: BaseConsole, swingUserInterface: SwingUserInterface, val reason: String) : PostSUIEvent(baseConsole, swingUserInterface)
