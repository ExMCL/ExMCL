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

package com.n9mtq4.exmcl.consoleprinter

import com.n9mtq4.exmcl.api.consoleprinter.PrintToConsole
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.events.ObjectEvent
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.logwindow.listener.ObjectListener
import com.n9mtq4.logwindow.utils.StringParser
import com.n9mtq4.reflection.ReflectionHelper
import net.minecraft.launcher.ui.tabs.ConsoleTab
import java.util.ArrayList
import javax.swing.SwingUtilities

/**
 * Created by will on 9/13/2016 at 7:24 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
@Suppress("unused", "UNUSED_PARAMETER")
class ConsolePrintListener : GenericListener, ObjectListener {
	
	private lateinit var consoleTab: ConsoleTab
	
	private var safeToPrint = false
	private val notSent: ArrayList<String> = arrayListOf()
	
	@ListensFor(PrintToConsole::class)
	fun listenForConsolePrintEvent(event: PrintToConsole, baseConsole: BaseConsole) {
		
		if (!safeToPrint) {
			notSent.add(event.message)
			return
		}
		
		cPrint(event.message)
		
	}
	
	@ListensFor(PreDefinedSwingHookEvent::class)
	fun listenForSwingHook(e: PreDefinedSwingHookEvent, baseConsole: BaseConsole) {
		if (e.type == PreDefinedSwingComponent.LAUNCHER_TAB_PANEL) {
			val tabPanel = e.component
			consoleTab = ReflectionHelper.getObject("console", tabPanel)
			safeToPrint = true
			notSent.forEach { cPrint(it) } // lets print out the stuff
			notSent.clear()
		}
	}
	
	override fun objectReceived(event: ObjectEvent, baseConsole: BaseConsole) {
		
		if (!event.isUserInputString) return
		val str = StringParser(event.obj as String)
		
		if (str.text.startsWith("cPrint ", ignoreCase = true)) {
			cPrint(str.getWordsStartingFrom(1).replace("\\n", "\n"))
		}else if (str.text.startsWith("cPrintln", ignoreCase = true)) {
			cPrint(str.getWordsStartingFrom(1).replace("\\n", "\n") + "\n")
		}
		
	}
	
	private fun cPrint(str: String) = SwingUtilities.invokeLater {
		consoleTab.print(str)
	}
	
}
