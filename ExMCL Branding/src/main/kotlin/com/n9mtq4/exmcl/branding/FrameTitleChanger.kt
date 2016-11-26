package com.n9mtq4.exmcl.branding

import com.n9mtq4.exmcl.api.BUILD_NUMBER
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.ObjectEvent
import com.n9mtq4.logwindow.listener.ObjectListener
import javax.swing.JFrame

/**
 * Created by will on 11/25/16 at 11:48 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class FrameTitleChanger : ObjectListener {
	
	override fun objectReceived(e: ObjectEvent, baseConsole: BaseConsole) {
		
		if (e.message != "jframe") return
		if (e.obj !is JFrame) return
		
		val frame = e.obj as JFrame
		
		frame.title += " (ExMCL $BUILD_NUMBER)"
		
	}
	
}
