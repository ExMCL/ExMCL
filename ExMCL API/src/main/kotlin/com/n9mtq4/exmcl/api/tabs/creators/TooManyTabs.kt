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
