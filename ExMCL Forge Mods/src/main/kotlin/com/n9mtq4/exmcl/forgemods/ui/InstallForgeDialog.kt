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

package com.n9mtq4.exmcl.forgemods.ui

import com.n9mtq4.exmcl.forgemods.utils.downloadFile
import com.n9mtq4.exmcl.forgemods.utils.msg
import com.n9mtq4.kotlin.extlib.ignore
import com.n9mtq4.kotlin.extlib.pst
import com.n9mtq4.kotlin.extlib.pstAndGiven
import com.n9mtq4.kotlin.extlib.syntax.def
import com.n9mtq4.logwindow.utils.StringParser
import org.jsoup.Jsoup
import java.awt.BorderLayout
import java.io.File
import java.io.IOException
import java.util.ArrayList
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.JTable

/**
 * Created by will on 2/14/16 at 10:39 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

private const val MCVERSION_SELECTOR = "body > div.contents > div.wrapper > section:nth-child(1) > div.versions-wrapper > div > ul > li > div > ul > li > a"
private const val LINK_SELECTOR = "#downloadsTable > tbody > tr > td:nth-child(3) > ul > li:nth-child(2) > a:nth-child(1)"
private const val FORGE_FILES_URL = "http://files.minecraftforge.net/"
private const val URL_SPLIT = "&url="
private const val BETWEEN_START = "/maven/net/minecraftforge/forge/"
private const val BETWEEN_END = "-installer.jar"

class InstallForgeDialog(val forgeTab: ForgeTab) {
	
	private val frame: JFrame
	private val select: JButton
	private val table: JTable
	private val scroll: JScrollPane
	
	init {
		
		this.frame = JFrame("Select Forge Version")
		
		val model = ImmutableTableModel(getListOfForges(), arrayOf("MC Versions", "Forge Versions", "URL"))
		
		this.table = JTable(model)
		table.tableHeader.reorderingAllowed = false
		this.scroll = JScrollPane(table)
		this.select = JButton("Download and Install")
		
		frame.add(scroll, BorderLayout.CENTER)
		frame.add(select, BorderLayout.SOUTH)
		
		frame.run {
			pack()
			isVisible = true
			setLocationRelativeTo(forgeTab)
			rootPane.defaultButton = select
		}
		
		select.addActionListener { event ->
			pst {
				JOptionPane.showMessageDialog(frame, "After installing forge, a launcher restart\nis required.", "Info", JOptionPane.INFORMATION_MESSAGE)
				val f = download()
				run(f)
				frame.dispose()
			}
		}
		
	}
	
	private fun download() = def {
		
		val row = table.selectedRow
		val forgeVersion = table.getValueAt(row, 1) as String
		val url = table.getValueAt(row, 2) as String
		println(url)
		
		val tmpDir = File("tmp/")
		tmpDir.mkdirs()
		val file = File(tmpDir, "forge_$forgeVersion.jar")
		downloadFile(url, file)
		file
		
	}
	
	/**
	 * Tries to run the downloaded forge installer
	 * should work with most versions
	 * */
	private fun run(file: File) {
		
		val re = Runtime.getRuntime()
		try {
			re.exec("java -jar ${file.path}")
		} catch (e: IOException) {
			e.printStackTrace()
			forgeTab.baseConsole.printStackTrace(e)
			msg(forgeTab, "Error launching forge installer", "Error", JOptionPane.ERROR_MESSAGE)
		}
		
	}
	
	private fun getListOfForges() = pstAndGiven(arrayOf(arrayOf("Error Downloading Forge List", "Please try again"))) {
		
		val versions = ArrayList<Array<out Any>>()
		
		val index = Jsoup.connect(FORGE_FILES_URL).get()
		val mcVersions = index.select(MCVERSION_SELECTOR)
		val linksIndex = index.select(LINK_SELECTOR)
		
		linksIndex.forEach {
			val adLink = it.attr("href")
			val link = adLink.split(URL_SPLIT)[1]
			
			val parser = StringParser(link)
			var chop = parser.getBetween(BETWEEN_START, BETWEEN_END)
			chop = chop.split("/")[0]
			val tokens = chop.split("-")
			
			versions.add(arrayOf(tokens[0], tokens[1], link))
			
		}
		
		mcVersions.forEach {
			
//			prevent a bad formatting on an older version to mess with things
//			This is expected behavior and there are not downsides
			ignore {
				
				val versionHref = it.attr("href")
				val doc = Jsoup.connect(formatUrl(FORGE_FILES_URL, versionHref)).get()
				val links = doc.select(LINK_SELECTOR)
				
				links.forEach {
					val adLink = it.attr("href")
					val link = adLink.split(URL_SPLIT)[1]
					
					val parser = StringParser(link)
					var chop = parser.getBetween(BETWEEN_START, BETWEEN_END)
					chop = chop.split("/")[0]
					val tokens = chop.split("-")
					
					versions.add(arrayOf(tokens[0], tokens[1], link))
				}
				
			}
			
		}
		
		versions.toTypedArray()
		
	}
	
	/**
	 * Forge changed their links from absolute to relative, 
	 * so we now need to account for that :(
	 * */
	private fun formatUrl(prefix: String, suffix: String): String {
//		a url should start with http
		if (!suffix.startsWith("http", ignoreCase = true)) {
			return prefix + suffix
		}
		return suffix
	}
	
}

