@file:Suppress("unused", "UNUSED_PARAMETER")
package com.n9mtq4.exmcl.updater

import com.n9mtq4.exmcl.api.BUILD_NUMBER
import com.n9mtq4.exmcl.api.tabs.events.SafeForTabCreationEvent
import com.n9mtq4.exmcl.api.updater.UpdateAvailable
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.Async
import com.n9mtq4.logwindow.annotation.ListensFor
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
class UpdateFinder : GenericListener {
	
	companion object {
		private const val FORCE_UPDATE = false
	}
	
	/**
	 * Having it wait for the tab ready instead of the enabled
	 * gives us the ability to modify the launcher rather than
	 * displaying a popup
	 * */
	@Async
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForTabReady(e: SafeForTabCreationEvent, baseConsole: BaseConsole) {
		
		checkForUpdate(e.initiatingBaseConsole)
		
	}
	
	private fun checkForUpdate(baseConsole: BaseConsole) {
		
		try {
			
//			Download the json data
			val jsonRaw = Jsoup.connect(API_UPDATE_URL).ignoreContentType(true).execute().body()
			
//			parse it
			val parser = JSONParser()
			val json = parser.parse(jsonRaw) as JSONArray
			
//			get the latest version
			val latestVersion = json[0] as HashMap<*, *>
			
//			get the tag name 
			val tagName = latestVersion["tag_name"] as String
			val tokens = tagName.split("-")
			val targetBuildNumber = tokens[tokens.size - 1].toInt() // get the build number
//			val targetBuildNumber = Integer.MAX_VALUE // testing value
			
			if (FORCE_UPDATE || BUILD_NUMBER < targetBuildNumber) baseConsole.pushEvent(UpdateAvailable(baseConsole, latestVersion, targetBuildNumber))
			
		}catch (e: Exception) {
			System.err.println("Failed to get update status")
			e.printStackTrace()
		}
		
	}
	
}
