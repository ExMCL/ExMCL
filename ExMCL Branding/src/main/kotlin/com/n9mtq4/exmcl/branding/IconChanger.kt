package com.n9mtq4.exmcl.branding

import com.n9mtq4.kotlin.extlib.ignore
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.events.ObjectEvent
import com.n9mtq4.logwindow.listener.ObjectListener
import com.n9mtq4.reflection.ReflectionHelper
import javax.imageio.ImageIO
import javax.swing.JFrame

/**
 * Created by will on 11/25/16 at 10:25 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class IconChanger : ObjectListener {
	
	override fun objectReceived(e: ObjectEvent, baseConsole: BaseConsole) {
		
		if (e.message != "jframe") return
		if (e.obj !is JFrame) return
		
		ignore {
			
			val inStream = IconChanger::class.java.getResourceAsStream("/exmclfavicon_old.png")
			inStream?.let {
				
				val image = ImageIO.read(inStream)
				
				(e.obj as JFrame).iconImage = image
				
				ignore {
					if (System.getProperty("os.name").contains("mac", ignoreCase = true)) {
						
						val app: Any = ReflectionHelper.callStaticObjectMethod("getApplication", Class.forName("com.apple.eawt.Application"), arrayOf<Class<*>>(), arrayOf<Any>())
						ReflectionHelper.callVoidMethod("setDockIconImage", app, image)
						
					}
				}
				
			}
			
		}
		
	}
	
}
