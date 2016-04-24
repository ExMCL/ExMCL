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

@file:JvmName("EchoListener") // just in case
package com.n9mtq4.exmcl.echo

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.events.EnableEvent
import com.n9mtq4.logwindow.events.GenericEvent
import com.n9mtq4.logwindow.events.ObjectEvent
import com.n9mtq4.logwindow.listener.EnableListener
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.logwindow.listener.ListenerContainer
import com.n9mtq4.logwindow.listener.ObjectListener
import com.n9mtq4.reflection.ReflectionHelper
import java.util.ArrayList
import java.util.Arrays

/**
 * Created by will on 3/31/15.<br>
 * 
 * This listener is for debugging purposes.
 * It takes any String or Object that is pushed to the
 * BaseConsole and prints it out so I can see it.
 */
class EchoListener : EnableListener, ObjectListener, GenericListener {
	
	/**
	 * It is important that Echoer is the first listener, so use ReflectionHelper to
	 * accomplish that goal.
	 */
	override fun onEnable(e: EnableEvent) {
//		gets the listeners
		val l: ArrayList<ListenerContainer> = ReflectionHelper.getObject("listenerContainers", e.baseConsole)
//		gets the listener container that is handling the methods for this listener
		val container = e.baseConsole.getContainerFromAttribute(this)
//		removes the container from the list
		l.remove(container)
//		re-adds it first
		l.add(0, container)
		e.baseConsole.println("Used hack to make Echoer the first listener")
	}
	
	/**
	 * Prints the object sent to the BaseConsole
	 */
	override fun objectReceived(e: ObjectEvent, baseConsole: BaseConsole) {
		
		if (e.obj == null) {
			baseConsole.println("Object: " + e.message + " | (null)")
			return
		}
		
		when(e.obj) {
//			null -> baseConsole.println("Object: ${e.message} | (null)") // TODO: does this work, or NPE?
			is Array<*> -> baseConsole.println("Object: ${e.message} | (${Arrays.toString(e.obj as Array<*>)})")
			is Collection<*> -> baseConsole.println("Object: ${e.message} | (${Arrays.toString((e.obj as Collection<*>).toTypedArray())})")
			else -> baseConsole.println("Object: " + e.message + " | (" + e.obj.toString() + ")")
		}
		
	}
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor(GenericEvent::class)
	fun listenForAllGenericEvents(event: GenericEvent, baseConsole: BaseConsole) = baseConsole.println(event.toString())
	
}
