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
		
		baseConsole.disableListenerAttribute(this)
		
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
