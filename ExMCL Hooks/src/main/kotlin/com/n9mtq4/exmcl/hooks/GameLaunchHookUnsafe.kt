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

import com.n9mtq4.exmcl.api.hooks.events.DefaultGameLaunchEvent
import com.n9mtq4.exmcl.api.hooks.events.GameLaunchEvent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton

/**
 * Created by will on 7/28/15 at 3:53 PM.
 * This class does some advanced and sneaky things to let
 * us process the play button action BEFORE the minecraft launcher
 * can. Because swing randomizes the order of ActionListeners we
 * can't just add us the first one in the list. We have to
 * remove all other listeners, while keeping a copy of the
 * listeners for us. When our ActionListener gets sent the event,
 * we send it to all the BaseConsole listener and then if none of them
 * set the canceled flag to true, then we will send it to the listeners
 * we removed from before.
 * 
 * NOTE: THIS LISTENER IS FAR LESS COMPLEX WITH LogWindowFramework-5.1
 */
class GameLaunchHookUnsafe : ActionListener, GenericListener {
	
	private var listeners: Array<ActionListener>? = null
	private lateinit var baseConsole: BaseConsole
	
	/**
	 * Gets the play button and adds the GameLaunchHook onto it.
	 */
	@Suppress("unused")
	@ListensFor(PreDefinedSwingHookEvent::class)
	fun ListensForPlayButton(e: PreDefinedSwingHookEvent, baseConsole: BaseConsole) {
		
//		makes sure it is the playbutton
		if (e.type !== PreDefinedSwingComponent.PLAY_BUTTON) return
		
//		set the baseConsole
		this.baseConsole = baseConsole
		
//		get the button and listeners
		val playButton = e.component as JButton
		this.listeners = playButton.actionListeners
//		remove all the action listeners on the button
//		we will handle them
		for (listener in listeners!!) {
			playButton.removeActionListener(listener)
		}
		
//		add them into the DefaultActionListenerDispatcher
		val defaultGameRunner = DefaultMinecraftGameRunner(listeners as Array<ActionListener>)
		baseConsole.addListenerAttribute(defaultGameRunner)
		
//		now add us as the only action listener
		playButton.addActionListener(this)
		
	}
	
	/**
	 * ActionListener actionPerformed - push the ActionEvent to the baseConsole,
	 * then maybe send it to the listeners
	 */
	override fun actionPerformed(e: ActionEvent) {
		
//		send the event to people listening with the BaseConsole first.
//		baseConsole.push(e, "gamelaunch");
		val gameLaunchEvent = GameLaunchEvent(e, baseConsole)
		baseConsole.pushEvent(gameLaunchEvent)
		
		if (!gameLaunchEvent.isCanceled) {
			baseConsole.pushEvent(DefaultGameLaunchEvent(e, baseConsole))
		}
		
	}
	
	private inner class DefaultMinecraftGameRunner(private val listeners: Array<ActionListener>) : GenericListener {
		
		@Suppress("unused")
		@ListensFor
		fun onDefaultGameLaunch(event: DefaultGameLaunchEvent, baseConsole: BaseConsole) {
			for (listener in listeners) {
				listener.actionPerformed(event.actionEvent)
			}
		}
		
	}
	
}
