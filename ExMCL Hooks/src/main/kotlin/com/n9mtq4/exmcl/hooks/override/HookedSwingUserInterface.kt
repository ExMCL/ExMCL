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

package com.n9mtq4.exmcl.hooks.override

import com.mojang.launcher.events.GameOutputLogProcessor
import com.mojang.launcher.updater.DownloadProgress
import com.mojang.launcher.versions.CompleteVersion
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIGameLaunchFailure
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIGetFrame
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIGetTitle
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIHideDownloadProgress
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIInitializeFrame
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUISetDownloadProgress
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUISetVisible
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIShowCrashReport
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIShowGameOutputTab
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIShowLoginPrompt
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIShowLoginPromptCallback
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIShowOutdatedNotice
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIShutdownLauncher
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.PostSUIUpdatePlayState
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIGameLaunchFailure
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIGetFrame
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIGetTitle
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIHideDownloadProgress
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIInitializeFrame
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUISetDownloadProgress
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUISetVisible
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowCrashReport
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowGameOutputTab
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowLoginPrompt
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowLoginPromptCallback
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowOutdatedNotice
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShutdownLauncher
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIUpdatePlayState
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.reflection.ReflectionHelper
import com.n9mtq4.reflection.ReflectionWrapper
import net.minecraft.launcher.Launcher
import net.minecraft.launcher.SwingUserInterface
import net.minecraft.launcher.game.MinecraftGameRunner
import net.minecraft.launcher.ui.popups.login.LogInPopup
import java.io.File
import javax.swing.JFrame

/**
 * Created by will on 8/31/15 at 5:06 PM.
 * A class for better com.n9mtq4.exmcl.hooks into the SwingUserInterface
 * Wont necessarily be forwards compatible, so this is dangerous.
 */
class HookedSwingUserInterface(parent: SwingUserInterface, private val baseConsole: BaseConsole) : SwingUserInterface(ReflectionHelper.getObject<Any>("minecraftLauncher", parent) as Launcher, parent.frame) {
	private val thisWrapper: ReflectionWrapper<Any>
	private val parentWrapper: ReflectionWrapper<Any>
	
	init {
		thisWrapper = ReflectionWrapper.attachToObject(this)
		parentWrapper = ReflectionWrapper.attachToObject(parent)
		thisWrapper.set("launcherPanel", parentWrapper.get("launcherPanel"))
	}
	
	override fun showLoginPrompt(minecraftLauncher: Launcher, callback: LogInPopup.Callback) {
		val event = SUIShowLoginPromptCallback(baseConsole, this, minecraftLauncher, callback)
		baseConsole.pushEvent(event)
		if (!event.stopMojangsCode) super.showLoginPrompt(minecraftLauncher, callback)
		baseConsole.pushEvent(PostSUIShowLoginPromptCallback(baseConsole, this, minecraftLauncher, callback))
	}
	
	override fun initializeFrame() {
		val event = SUIInitializeFrame(baseConsole, this)
		baseConsole.pushEvent(event)
		if (!event.stopMojangsCode) super.initializeFrame()
		baseConsole.pushEvent(PostSUIInitializeFrame(baseConsole, this))
	}
	
	override fun showOutdatedNotice() {
		val event = SUIShowOutdatedNotice(baseConsole, this)
		baseConsole.pushEvent(event)
		if (!event.stopMojangsCode) super.showOutdatedNotice()
		baseConsole.pushEvent(PostSUIShowOutdatedNotice(baseConsole, this))
	}
	
	override fun showLoginPrompt() {
		val event = SUIShowLoginPrompt(baseConsole, this)
		baseConsole.pushEvent(event)
		if (!event.stopMojangsCode) super.showLoginPrompt()
		baseConsole.pushEvent(PostSUIShowLoginPrompt(baseConsole, this))
	}
	
	override fun setVisible(visible: Boolean) {
		val event = SUISetVisible(baseConsole, this, visible)
		baseConsole.pushEvent(event)
		if (!event.stopMojangsCode) super.setVisible(visible)
		baseConsole.pushEvent(PostSUISetVisible(baseConsole, this, visible))
	}
	
	override fun shutdownLauncher() {
		val event = SUIShutdownLauncher(baseConsole, this)
		baseConsole.pushEvent(event)
		if (!event.stopMojangsCode) super.shutdownLauncher()
		baseConsole.pushEvent(PostSUIShutdownLauncher(baseConsole, this))
	}
	
	override fun setDownloadProgress(downloadProgress: DownloadProgress) {
		val event = SUISetDownloadProgress(baseConsole, this, downloadProgress)
		baseConsole.pushEvent(event)
		if (!event.stopMojangsCode) super.setDownloadProgress(downloadProgress)
		baseConsole.pushEvent(PostSUISetDownloadProgress(baseConsole, this, downloadProgress))
	}
	
	override fun hideDownloadProgress() {
		val event = SUIHideDownloadProgress(baseConsole, this)
		baseConsole.pushEvent(event)
		if (!event.stopMojangsCode) super.hideDownloadProgress()
		baseConsole.pushEvent(PostSUIHideDownloadProgress(baseConsole, this))
	}
	
	override fun showCrashReport(version: CompleteVersion, crashReportFile: File, crashReport: String) {
		val event = SUIShowCrashReport(baseConsole, this, version, crashReportFile, crashReport)
		baseConsole.pushEvent(event)
		if (!event.stopMojangsCode) super.showCrashReport(version, crashReportFile, crashReport)
		baseConsole.pushEvent(PostSUIShowCrashReport(baseConsole, this, version, crashReportFile, crashReport))
	}
	
	override fun gameLaunchFailure(reason: String) {
		val event = SUIGameLaunchFailure(baseConsole, this, reason)
		baseConsole.pushEvent(event)
		if (!event.stopMojangsCode) super.gameLaunchFailure(reason)
		baseConsole.pushEvent(PostSUIGameLaunchFailure(baseConsole, this, reason))
	}
	
	override fun updatePlayState() {
		val event = SUIUpdatePlayState(baseConsole, this)
		baseConsole.pushEvent(event)
		if (!event.stopMojangsCode) super.updatePlayState()
		baseConsole.pushEvent(PostSUIUpdatePlayState(baseConsole, this))
	}
	
	override fun showGameOutputTab(gameRunner: MinecraftGameRunner): GameOutputLogProcessor? {
		val event = SUIShowGameOutputTab(baseConsole, this, gameRunner)
		baseConsole.pushEvent(event)
		val toReturn = if (event.stopMojangsCode) event.gameOutputLogProcessor else super.showGameOutputTab(gameRunner)
		baseConsole.pushEvent(PostSUIShowGameOutputTab(baseConsole, this, gameRunner))
		return toReturn
	}
	
	override fun getTitle(): String? {
		val event = SUIGetTitle(baseConsole, this)
		baseConsole.pushEvent(event)
		val toReturn = if (event.stopMojangsCode) event.title else super.getTitle()
		baseConsole.pushEvent(PostSUIGetTitle(baseConsole, this))
		return toReturn
	}
	
	override fun getFrame(): JFrame? {
		val event = SUIGetFrame(baseConsole, this)
		baseConsole.pushEvent(event)
		val toReturn = if (event.stopMojangsCode) event.frame else super.getFrame()
		baseConsole.pushEvent(PostSUIGetFrame(baseConsole, this))
		return toReturn
	}
	
}
