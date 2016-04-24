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
