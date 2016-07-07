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
package com.n9mtq4.exmcl.modinstaller

import com.n9mtq4.exmcl.api.hooks.events.MinecraftLauncherEvent
import com.n9mtq4.exmcl.api.tabs.events.CreateTabEvent
import com.n9mtq4.exmcl.api.tabs.events.SafeForTabCreationEvent
import com.n9mtq4.exmcl.modinstaller.ui.ModsTab
import com.n9mtq4.exmcl.modinstaller.utils.il
import com.n9mtq4.kotlin.extlib.pst
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.Async
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import net.minecraft.launcher.Launcher

/**
 * Created by will on 2/17/16 at 5:22 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class InitModInstaller : GenericListener {
	
	private lateinit var minecraftLauncher: Launcher
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForMinecraftLauncher(e: MinecraftLauncherEvent, baseConsole: BaseConsole) {
		
		this.minecraftLauncher = e.minecraftLauncher
		
	}
	
	@Suppress("unused")
	@Async
	@ListensFor
	fun listenForTabSafe(e: SafeForTabCreationEvent, baseConsole: BaseConsole) {
		
		il {
			pst {
				val modsTab = ModsTab(minecraftLauncher, baseConsole)
				baseConsole.pushEvent(CreateTabEvent("Jar Mods", modsTab, baseConsole))
			}
		}
		
		baseConsole.disableListenerAttribute(this)
		
	}
	
}
