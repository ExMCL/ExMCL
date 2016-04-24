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
package com.n9mtq4.exmcl.updater

import com.n9mtq4.exmcl.api.hooks.events.SwingUserInterfaceEvent
import com.n9mtq4.exmcl.api.updater.UpdateAvailable
import com.n9mtq4.kotlin.extlib.pstAndUnit
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.reflection.ReflectionWrapper
import net.minecraft.launcher.SwingUserInterface
import net.minecraft.launcher.ui.LauncherPanel
import javax.swing.SwingUtilities

/**
 * Created by will on 2/27/16 at 4:20 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
@Suppress("unused", "UNUSED_PARAMETER")
class UpdateAlerter : GenericListener {
	
	private lateinit var swingUserInterface: SwingUserInterface
	private lateinit var swingUserInterfaceRR: ReflectionWrapper<SwingUserInterface>
	private lateinit var launcherPanel: LauncherPanel
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForSwingUserInterface(e: SwingUserInterfaceEvent, baseConsole: BaseConsole) {
		this.swingUserInterface = e.swingUserInterface
		this.swingUserInterfaceRR = ReflectionWrapper.attachToObject(swingUserInterface)
		this.launcherPanel = swingUserInterfaceRR["launcherPanel"]
	}
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForUpdateEvent(e: UpdateAvailable, baseConsole: BaseConsole) = SwingUtilities.invokeLater {
		pstAndUnit {
			val updatePanel = UpdatePanel(launcherPanel, e)
			updatePanel.addThisCard()
		}
	}
	
}
