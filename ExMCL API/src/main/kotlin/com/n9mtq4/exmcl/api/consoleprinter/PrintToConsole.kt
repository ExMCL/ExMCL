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

package com.n9mtq4.exmcl.api.consoleprinter

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.DefaultGenericEvent
import java.util.Arrays

/**
 * Created by will on 9/13/2016 at 7:27 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
@Suppress("unused", "UNUSED_PARAMETER")
open class PrintToConsole(val message: String, initiatingBaseConsole: BaseConsole?) : DefaultGenericEvent(initiatingBaseConsole) {
	
	constructor(array: Array<*>, initiatingBaseConsole: BaseConsole?) : this(Arrays.toString(array), initiatingBaseConsole)
	constructor(collection: Collection<*>, initiatingBaseConsole: BaseConsole?) : this(Arrays.toString(collection.toTypedArray()), initiatingBaseConsole)
	constructor(obj: Any, initiatingBaseConsole: BaseConsole?) : this(obj.toString(), initiatingBaseConsole)
	
	override fun toString() = "${this.javaClass.name}{message=$message}"
	
}

@Suppress("unused", "UNUSED_PARAMETER")
class PrintlnToConsole(message: String, initiatingBaseConsole: BaseConsole?) : PrintToConsole("$message\n", initiatingBaseConsole) {
	
	constructor(array: Array<*>, initiatingBaseConsole: BaseConsole?) : this(Arrays.toString(array), initiatingBaseConsole)
	constructor(collection: Collection<*>, initiatingBaseConsole: BaseConsole?) : this(Arrays.toString(collection.toTypedArray()), initiatingBaseConsole)
	constructor(obj: Any, initiatingBaseConsole: BaseConsole?) : this(obj.toString(), initiatingBaseConsole)
	
	override fun toString() = "${this.javaClass.name}{message=${message.trimEnd()}"
	
}
