package com.n9mtq4.exmcl.forgemods.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by will on 2/14/16 at 11:06 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
public class Downloader {
	
	/**
	 * Downloads a url to a file
	 * 
	 * TODO: this code is a MESS
	 * 
	 * @param url the url
	 * @param file the file
	 * */
	public static void downloadFile(String url, File file) {
		
		URL website = null;
		try {
			website = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ReadableByteChannel rbc = null;
		try {
			if (website != null) {
				rbc = Channels.newChannel(website.openStream());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if (fos != null) {
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			}
			if (fos != null) {
				fos.close();
			}
			if (rbc != null) {
				rbc.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
