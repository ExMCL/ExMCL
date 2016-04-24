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

import com.n9mtq4.exmcl.api.hooks.events.AllSwingComponentsEvent
import com.n9mtq4.exmcl.api.hooks.events.SwingUserInterfaceEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.Async
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.reflection.ReflectionHelper
import java.awt.Component
import java.awt.Container
import java.util.ArrayList
import javax.swing.JFrame

/**
 * Created by will on 7/28/15 at 2:18 PM.
 */
class SwingComponentHook : GenericListener {
	
	@Suppress("unused")
	@Async
	@ListensFor(SwingUserInterfaceEvent::class)
	fun listenForSwingEvent(e: SwingUserInterfaceEvent, baseConsole: BaseConsole) {
		
		val frame: JFrame = ReflectionHelper.getObject("frame", e.swingUserInterface)
		
		val components = getAllComponents(frame)
		components.forEach { component ->
			e.initiatingBaseConsole.let { it.pushEvent(AllSwingComponentsEvent(component, it)) }
		}
		
		baseConsole.disableListenerAttribute(this)
		
	}
	
	companion object {
		
		fun getAllComponents(c: Container): ArrayList<Component> {
			val comps = c.components
			val compList = ArrayList<Component>()
			comps.forEach {
				compList.add(it)
				if (it is Container) compList.addAll(getAllComponents(it))
			}
			return compList
		}
		
	}
	
}
