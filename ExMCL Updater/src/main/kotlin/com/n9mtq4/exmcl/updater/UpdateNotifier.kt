package com.n9mtq4.exmcl.updater

import com.n9mtq4.exmcl.api.BUILD_NUMBER
import com.n9mtq4.logwindow.annotation.Async
import com.n9mtq4.logwindow.events.EnableEvent
import com.n9mtq4.logwindow.listener.EnableListener
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser
import org.jsoup.Jsoup
import java.util.HashMap
import javax.swing.JOptionPane

/**
 * Created by will on 2/16/16 at 2:41 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class UpdateNotifier : EnableListener {
	
	@Async
	override fun onEnable(e: EnableEvent) {
		
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
			
			if (BUILD_NUMBER < targetBuildNumber) {
				JOptionPane.showMessageDialog(null, "There is an update available")
			}
			
		}catch (e: Exception) {
			System.err.println("Failed to get update status")
			e.printStackTrace()
		}
		
	}
	
}
