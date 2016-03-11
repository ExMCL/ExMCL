package com.n9mtq4.exmcl.forgemods.utils

import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

/**
 * Created by will on 3/10/16 at 8:47 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * Downloads a url to a file
 * 
 * @param url the url
 * @param file the file
 * @return if the download was successful
 * */
internal fun downloadFile(url: String, file: File): Boolean {
	
	try {
		
		val website = URL(url)
		val rbc = Channels.newChannel(website.openStream())
		val fos = FileOutputStream(file)
		
		fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
		fos.close()
		rbc.close()
		
		return true
		
	}catch (e: Exception) {
		e.printStackTrace()
		return false
	}
	
}
