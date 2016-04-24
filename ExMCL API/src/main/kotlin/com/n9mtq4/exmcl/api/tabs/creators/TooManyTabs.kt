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

package com.n9mtq4.exmcl.api.tabs.creators

import com.n9mtq4.exmcl.api.tabs.events.CreateTabEvent
import com.n9mtq4.exmcl.api.tabs.events.LowLevelCreateTabEvent
import com.n9mtq4.exmcl.api.tabs.events.SafeForLowLevelTabCreationEvent
import com.n9mtq4.exmcl.api.tabs.events.SafeForTabCreationEvent
import com.n9mtq4.exmcl.api.tabs.ui.TabTab
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.events.EnableEvent
import com.n9mtq4.logwindow.listener.EnableListener
import com.n9mtq4.logwindow.listener.GenericListener
import java.awt.Component
import java.util.ArrayList

/**
 * Created by will on 2/13/16 at 12:34 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class TooManyTabs : EnableListener, GenericListener, TabCreator {
	
	private lateinit var tabTab: TabTab
	private var tabPanel: Any? = null
	private var tabSafe = false
	private val unAddedTabs: ArrayList<CreateTabEvent> = ArrayList()
	
	override fun onEnable(e: EnableEvent) {
		this.tabTab = TabTab()
	}
	
	@Suppress("unused")
	@ListensFor(SafeForLowLevelTabCreationEvent::class)
	fun listenForLLTabSafety(e: SafeForLowLevelTabCreationEvent, baseConsole: BaseConsole) {
		baseConsole.pushEvent(LowLevelCreateTabEvent("ExMCL", tabTab, baseConsole))
		unAddedTabs.forEach { addTab(it.title, it.component) }
		this.tabSafe = true
		baseConsole.pushEvent(SafeForTabCreationEvent(baseConsole))
	}
	
	@Suppress("unused")
	@ListensFor(CreateTabEvent::class)
	fun listenForTabAdding(e: CreateTabEvent, baseConsole: BaseConsole) {
		if (!tabSafe) unAddedTabs.add(e)
		addTab(e.title, e.component)
	}
	
	override fun addTab(title: String, tab: Component) {
		tabTab.addTab(title, tab)
	}
	
}
