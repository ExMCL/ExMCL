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

import com.n9mtq4.exmcl.api.hooks.events.MinecraftLauncherEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.ObjectEvent
import com.n9mtq4.logwindow.listener.ObjectListener
import net.minecraft.launcher.Launcher

/**
 * Created by will on 2/15/16 at 8:02 PM.
 * 
 * A low level listener for the minecraft launcher
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
class MinecraftLauncherHook : ObjectListener {
	
	override fun objectReceived(e: ObjectEvent, baseConsole: BaseConsole) {
		
		if (e.message != "minecraftlauncher") return
		if (e.obj !is Launcher) return
		
		baseConsole.pushEvent(MinecraftLauncherEvent(e.obj as Launcher, baseConsole))
		
	}
	
}
