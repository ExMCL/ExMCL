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

package com.n9mtq4.exmcl.hooks

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.reflection.ReflectionHelper
import net.minecraft.launcher.ui.bottombar.PlayButtonPanel

import javax.swing.JButton

/**
 * Created by will on 7/27/15 at 6:15 PM.
 */
class PlayButtonHook : GenericListener {
	
	@Suppress("unused")
	@ListensFor(PreDefinedSwingHookEvent::class)
	fun ListensForSwingEvent(e: PreDefinedSwingHookEvent, baseConsole: BaseConsole) {
		
		if (e.type !== PreDefinedSwingComponent.PLAY_BUTTON_PANEL) return
		
		val playButtonPanel = e.component as PlayButtonPanel
		val playButton = ReflectionHelper.getObject<JButton>("playButton", playButtonPanel)
		
		baseConsole.pushEvent(PreDefinedSwingHookEvent(playButton, PreDefinedSwingComponent.PLAY_BUTTON, baseConsole))
		
	}
	
}
