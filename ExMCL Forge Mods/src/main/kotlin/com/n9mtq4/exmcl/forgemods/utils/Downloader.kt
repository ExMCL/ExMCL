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

package com.n9mtq4.exmcl.forgemods.utils

import com.n9mtq4.kotlin.extlib.pstAndGiven
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
internal fun downloadFile(url: String, file: File) = pstAndGiven(false) {
	
	val website = URL(url)
	val rbc = Channels.newChannel(website.openStream())
	val fos = FileOutputStream(file)
	
	fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
	fos.close()
	rbc.close()
	
	true
	
}
