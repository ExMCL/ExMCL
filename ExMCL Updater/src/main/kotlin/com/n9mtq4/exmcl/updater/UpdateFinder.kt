@file:Suppress("unused", "UNUSED_PARAMETER")
package com.n9mtq4.exmcl.updater

import com.n9mtq4.exmcl.api.BUILD_NUMBER
import com.n9mtq4.exmcl.api.tabs.events.SafeForTabCreationEvent
import com.n9mtq4.exmcl.api.updater.UpdateAvailable
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.events.EnableEvent
import com.n9mtq4.logwindow.listener.EnableListener
import com.n9mtq4.logwindow.listener.GenericListener
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser
import org.jsoup.Jsoup
import java.util.HashMap

/**
 * Created by will on 2/16/16 at 2:41 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
@Suppress("unused", "UNUSED_PARAMETER")
class UpdateFinder : EnableListener, GenericListener {
	
	override fun onEnable(p0: EnableEvent?) {}
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForTabReady(e: SafeForTabCreationEvent, baseConsole: BaseConsole) {
		
		checkForUpdate(e.initiatingBaseConsole)
		
	}
	
/*	@Async
	override fun onEnable(e: EnableEvent) {
		checkForUpdate(e.baseConsole)
	}
	*/
	
	private fun checkForUpdate(baseConsole: BaseConsole) {
		
//		baseConsole.pushEvent(UpdateAvailable(baseConsole, 100)) //TODO: remove me
		
		try {
			
//			Download the json data
			val jsonRaw = Jsoup.connect(UPDATE_URL).ignoreContentType(true).execute().body()
			
//			parse it
			val parser = JSONParser()
			val json = parser.parse(jsonRaw) as JSONArray
			
//			get the latest version
			val latestVersion = json[0] as HashMap<*, *>
			
//			get the tag name
			val tagName = latestVersion["tag_name"] as String
			val targetBuildNumber = tagName.split("-")[2].toInt() // get the build number
			
//			TODO: uncomment me
			if (BUILD_NUMBER < targetBuildNumber) baseConsole.pushEvent(UpdateAvailable(baseConsole, targetBuildNumber))
			
		}catch (e: Exception) {
			System.err.println("Failed to get update status")
			e.printStackTrace()
		}
		
	}
	
}