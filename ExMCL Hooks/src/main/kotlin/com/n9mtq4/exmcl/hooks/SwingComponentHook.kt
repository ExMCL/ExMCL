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
