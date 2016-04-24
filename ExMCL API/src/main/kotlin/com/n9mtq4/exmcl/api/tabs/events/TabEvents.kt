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

package com.n9mtq4.exmcl.api.tabs.events

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import java.awt.Component

/**
 * Created by will on 2/27/16 at 3:09 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class CreateTabEvent(val title: String, val component: Component, baseConsole: BaseConsole) : DefaultGenericEvent(baseConsole) {
	override fun toString() = "${this.javaClass.name}{title=$title, component=$component}"
}
class LowLevelCreateTabEvent(val title: String, val component: Component, baseConsole: BaseConsole) : DefaultGenericEvent(baseConsole) {
	override fun toString() = "${this.javaClass.name}{title=$title, component=$component}"
}
class SafeForLowLevelTabCreationEvent(initiatingBaseConsole: BaseConsole) : DefaultGenericEvent(initiatingBaseConsole)
class SafeForTabCreationEvent(initiatingBaseConsole: BaseConsole) : DefaultGenericEvent(initiatingBaseConsole)
