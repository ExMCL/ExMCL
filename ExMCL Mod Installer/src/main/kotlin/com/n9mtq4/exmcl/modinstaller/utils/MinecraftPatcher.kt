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

package com.n9mtq4.exmcl.modinstaller.utils

import com.n9mtq4.exmcl.modinstaller.data.ModProfile
import com.n9mtq4.kotlin.extlib.io.open
import net.minecraft.launcher.Launcher
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Created by will on 2/28/16 at 3:26 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class MinecraftPatcher(val minecraftLauncher: Launcher, val modProfile: ModProfile) {
	
	val mojangLauncher = minecraftLauncher.launcher
	val workingDir = mojangLauncher.workingDirectory
	
	val version = findVersion()
	
	val oldVersion = if (version.endsWith("_exmcl")) version.substring(0, version.length - "_exmcl".length) else version
	val oldVersionDir = File(workingDir, "versions/$oldVersion")
	val oldVersionJarFile = File(oldVersionDir, "$oldVersion.jar")
	
	val newVersion = oldVersion + "_exmcl"
	val newVersionDir = File(workingDir, "versions/$newVersion")
	val newVersionJarFile = File(newVersionDir, "$newVersion.jar")
	
	@Throws(Exception::class)
	fun patch(progressCallback: (Int, Int, String) -> Unit) {
//		make sure that we can patch it first
		if (!oldVersionDir.exists()) {
			msg(null, "Jar mods can't be installed because minecraft isn't downloaded.\n" +
					"You will need to relaunch the game a second time for mods to get\n" +
					"installed.", "Jar Mod Patcher")
			return
		}
//		clean up from past runs
		if (newVersionDir.exists()) newVersionDir.deleteRecursively()
//		calculate total jobs
		val modJobs = modProfile.modList.size
		val totalJobs = modJobs + 3 // plus 3 for extracting minecraft; re-zipping; copying
//		create the new version directory
		newVersionDir.mkdirs()
//		copy all files from the old version to the new version
		oldVersionDir.copyRecursively(newVersionDir)
//		make a tmp directory
		val tmp = File(newVersionDir, "tmp")
		tmp.mkdirs()
//		step 0 callback
		progressCallback(0, totalJobs, "Extracting")
//		extract the old jar to the tmp directory
		val oldInNewPlace = File(newVersionDir, "$oldVersion.jar")
		val jarDir = File(tmp, "ejar")
		unzip(oldInNewPlace, jarDir)
//		extract the mods into the ejar
		modProfile.modList.filter { it.enabled }.map { it.file }.forEachIndexed { i, it ->
//			val toExtractTo = File(jarDir, it.name)
//			mod progress callback
			progressCallback(i + 1, totalJobs, it.name) // plus one cause we completed extracting minecraft jar
			unzip(it, jarDir)
		}
//		delete META-INF
		File(jarDir, "META-INF").deleteRecursively()
//		step modJobs + 2 callback
		progressCallback(modJobs + 2, totalJobs, "Zipping")
//		zip up the ejar
		val newJar = File(tmp, "new.jar")
		zip(buildFileTree(jarDir).filterNot { it.name.startsWith(".") }.toTypedArray(), newJar, jarDir)
//		step modJobs + 2 callback
		progressCallback(modJobs + 3, totalJobs, "Moving")
//		copy the zipped jar back to the profile
		newJar.copyTo(newVersionJarFile)
//		rename the profile in the json file
		modifyJson()
//		delete the tmp directory
		tmp.deleteRecursively()
//		delete any other files
		File(newVersionDir, "$oldVersion.jar").delete()
		File(newVersionDir, "$oldVersion.json").delete()
//		set the game to launch the patched mc version
		minecraftLauncher.launcher.versionManager.refreshVersions()
		val pm = minecraftLauncher.profileManager
		pm.selectedProfile.lastVersionId = newVersion
		pm.saveProfiles()
		pm.loadProfiles()
	}
	
	/**
	 * http://www.mkyong.com/java/how-to-decompress-files-from-a-zip-file/
	 */
	@Throws(IOException::class)
	private fun unzip(file: File, outDir: File) {
		
		val buffer = ByteArray(1024)
		if (!outDir.exists()) outDir.mkdirs()
		
		val zipInputStream = ZipInputStream(FileInputStream(file))
		var ze = zipInputStream.nextEntry
		
		while (ze != null) {
			
			try {
				val fileName = ze.name
				val newFile = File(outDir, fileName)
				//noinspection ResultOfMethodCallIgnored
				File(newFile.parent).mkdirs()
				
				if (ze.isDirectory) {
					ze = zipInputStream.nextEntry
					continue
				}
				
				val fos = FileOutputStream(newFile, false) // overwrite
				
				var len = zipInputStream.read(buffer)
				while (len > 0) {
					fos.write(buffer, 0, len)
					len = zipInputStream.read(buffer)
				}
				fos.close()
				
				ze = zipInputStream.nextEntry
				
			}catch (e: Exception) {
				System.err.println("Error extracting ${ze.name}")
				e.printStackTrace()
				ze = zipInputStream.nextEntry
			}
			
		}
		
		zipInputStream.closeEntry()
		zipInputStream.close()
		
	}
	
	@Throws(IOException::class)
	private fun zip(files: Array<File>, out: File, sourceDir: File) {
		val fileNames = Array<String>(files.size) {i -> files[i].absolutePath}
		zip(fileNames, out, sourceDir)
	}
	
	/**
	 * http://www.mkyong.com/java/how-to-compress-files-in-zip-format/
	 */
	@Throws(IOException::class)
	private fun zip(files: Array<String>, out: File, sourceDir: File) {
		
		val buffer = ByteArray(1024)
		
		val fos = FileOutputStream(out)
		val bfos = BufferedOutputStream(fos)
		val zos = ZipOutputStream(bfos)
		
		val subIndex = sourceDir.absolutePath.length + 1
		
		files.forEach { file ->
		
			val zeName = file.substring(subIndex, file.length).replace("\\", "/") // windows support
			val ze = ZipEntry(zeName)
			zos.putNextEntry(ze)
			
			val fins = FileInputStream(File(file))
			val inputStream = BufferedInputStream(fins)
			
			var len = inputStream.read(buffer)
			while (len > 0) {
				zos.write(buffer, 0, len)
				len = inputStream.read(buffer)
			}
			
			inputStream.close()
			zos.closeEntry()
			
		}
		
		zos.close()
		fos.close()
		
	}
	
	private fun modifyJson() {
//		TODO: change to use gson, so we only have 1 json library in this project
		val oldJsonFile = open(File(newVersionDir, "$oldVersion.json"), "r")
		val oldJsonText = oldJsonFile.readText()
		oldJsonFile.close()
		val parser = JSONParser()
		val json = parser.parse(oldJsonText) as JSONObject
		json["id"] = newVersion
		json.remove("downloads")
		json.remove("assetIndex")
		val libs = json["libraries"] as JSONArray
		libs.map { it as JSONObject }.forEach { 
			it.remove("downloads")
		}
		val newJsonText = json.toJSONString().replace("\\/", "/") // for some reason the JsonParser changes "/" to "\/", so we undo it
		val newJsonFile = open(File(newVersionDir, "$newVersion.json"), "w")
		newJsonFile.write(newJsonText)
		newJsonFile.close()
	}
	
	private fun buildFileTree(file: File): ArrayList<File> {
		
		val files = ArrayList<File>()
		if (file.isDirectory) {
			val children = file.listFiles() ?: return files
			children.forEach { files.addAll(buildFileTree(it)) }
		}else {
			files.add(file)
		}
		return files
		
	}
	
	/**
	 * Logic for finding version comes from net.minecraft.launcher.game.GameLaunchDispatcher.play()
	 * at line ~80 and ~105
	 * */
	private fun findVersion() = minecraftLauncher.profileManager.selectedProfile.lastVersionId ?: 
		minecraftLauncher.launcher.versionManager.getVersions(minecraftLauncher.profileManager.selectedProfile.versionFilter)[0].localVersion.id
	
}
