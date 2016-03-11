package com.n9mtq4.exmcl.forgemods.ui

import com.n9mtq4.exmcl.forgemods.utils.downloadFile
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

private const val MCVERSION_SELECTOR = "body > div.contents > div.wrapper > section:nth-child(1) > div.versions-wrapper > div > ul > li > div > ul > li > a";
private const val LINK_SELECTOR = "#downloadsTable > tbody > tr > td:nth-child(3) > ul > li:nth-child(2) > a:nth-child(1)";
private const val FORGE_FILES_URL = "http://files.minecraftforge.net/";
private const val URL_SPLIT = "&url=";
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
		
		frame.apply { 
			pack()
			isVisible = true
			setLocationRelativeTo(forgeTab)
			rootPane.defaultButton = select
		}
		
		select.addActionListener({ event -> 
			JOptionPane.showMessageDialog(frame, "After installing forge, a launcher restart\nis required.", "Info", JOptionPane.INFORMATION_MESSAGE)
			val f = download()
			run(f)
			frame.dispose()
		})
		
	}
	
	private fun download(): File {
		
		val row = table.selectedRow
		val forgeVersion = table.getValueAt(row, 1) as String
		val url = table.getValueAt(row, 2) as String
		println(url)
		
		val tmpDir = File("tmp/")
		tmpDir.mkdirs()
		val file = File(tmpDir, "forge_$forgeVersion.jar")
		downloadFile(url, file)
		return file
		
	}
	
	/**
	 * Tries to run the downloaded forge installer
	 * should work with most versions
	 * */
	private fun run(file: File) {
		
		val re = Runtime.getRuntime()
		try {
			re.exec("java -jar ${file.absolutePath}")
		}catch (e: IOException) {
			e.printStackTrace()
			forgeTab.baseConsole.printStackTrace(e)
			JOptionPane.showMessageDialog(forgeTab, "Error launching forge installer", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private fun getListOfForges(): Array<out Array<out Any>> {
		
		val versions = ArrayList<Array<out Any>>()
		
		try {
			
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
				
				try {
					
					val doc = Jsoup.connect(it.attr("href")).get()
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
					
				}catch (ignored: Exception) {
//					prevent a bad formatting on an older version to mess with things
//					This is expected behavior and there are not downsides
//					e.printStackTrace()
				}
				
			}
			
			return versions.toTypedArray()
			
		}catch (e: Exception) {
			e.printStackTrace()
			return arrayOf(arrayOf("Error Downloading Forge List", "Please try again"))
		}
		
	}
	
}
