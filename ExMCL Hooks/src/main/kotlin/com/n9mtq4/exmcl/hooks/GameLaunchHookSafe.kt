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

import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.ObjectEvent
import com.n9mtq4.logwindow.listener.ObjectListener
import javax.swing.JButton

/**
 * Created by will on 7/27/15 at 5:57 PM.
 */
@Deprecated("Use Unsafe version instead", replaceWith = ReplaceWith("GameLaunchHookUnsafe"))
class GameLaunchHookSafe : ObjectListener {
	
	override fun objectReceived(e: ObjectEvent, baseConsole: BaseConsole) {
		
		if (!e.message.equals("playbutton", ignoreCase = true)) return
		if (e.obj !is JButton) return
		
		val playButton = e.obj as JButton
		
		playButton.addActionListener { e1 -> e.initiatingBaseConsole.push(e1, "gamelaunch") }
		
	}
	
}
