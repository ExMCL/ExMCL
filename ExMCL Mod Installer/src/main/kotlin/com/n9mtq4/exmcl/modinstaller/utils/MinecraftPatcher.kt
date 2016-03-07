package com.n9mtq4.exmcl.modinstaller.utils

import com.n9mtq4.exmcl.modinstaller.data.ModProfile
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
import javax.swing.JOptionPane

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
	
	
	fun patch() {
//		clean up from past runs
		if (newVersionDir.exists()) newVersionDir.deleteRecursively()
//		create the new version directory
		newVersionDir.mkdirs()
//		copy all files from the old version to the new version
		oldVersionDir.copyRecursively(newVersionDir)
//		make a tmp directory
		val tmp = File(newVersionDir, "tmp")
		tmp.mkdirs()
//		extract the old jar to the tmp directory
		val oldInNewPlace = File(newVersionDir, "$oldVersion.jar")
		val jarDir = File(tmp, "ejar")
		unzip(oldInNewPlace, jarDir)
//		extract the mods
		val modsDir = File(tmp, "mods")
		modProfile.modList.filter { it.enabled }.map { it.file }.forEach { 
			val toExtractTo = File(modsDir, it.name)
			unzip(it, toExtractTo)
		}
//		create the mix folder
		val mix = File(tmp, "mix")
		mix.mkdirs()
//		copy the jar into it
		jarDir.copyRecursively(mix)
//		copy each mod into it
		val mods = modsDir.listFiles()
		mods.forEach { it.copyRecursively(mix, overwrite = true) }
//		delete META-INF
		File(mix, "META-INF").deleteRecursively()
//		zip up the mix
		val newJar = File(tmp, "new.jar")
		zip(buildFileTree(mix).filterNot { it.name.startsWith(".") }.toTypedArray(), newJar, mix)
//		copy the zipped jar back to the profile
		newJar.copyTo(newVersionJarFile)
//		rename the profile in the json file
		modifyJson()
//		delete the tmp directory
		tmp.deleteRecursively()
//		delete any other files
		File(newVersionDir, "$oldVersion.jar").delete()
		File(newVersionDir, "$oldVersion.json").delete()
//		set the profile to use this version
		val pm = minecraftLauncher.profileManager
		pm.selectedProfile.lastVersionId = newVersion
		pm.saveProfiles()
		pm.fireRefreshEvent()
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
			
			val fileName = ze.name
			val newFile = File(outDir, fileName)
			//noinspection ResultOfMethodCallIgnored
			File(newFile.parent).mkdirs()
			
			val fos = FileOutputStream(newFile)
			
			var len = zipInputStream.read(buffer)
			while (len > 0) {
				fos.write(buffer, 0, len)
				len = zipInputStream.read(buffer)
			}
			fos.close()
			
			ze = zipInputStream.nextEntry
			
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
		
		for (file in files) {
			
			val ze = ZipEntry(file.substring(subIndex, file.length))
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
		val oldJsonFile = open(File(newVersionDir, "$oldVersion.json"), "r")
		val oldJsonText = oldJsonFile.readAll()
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
	
	private fun findVersion() = minecraftLauncher.profileManager.selectedProfile.lastVersionId ?: getLatestVersion()
	private fun getLatestVersion() = askForVersion() //TODO: we can't get the latest version yet :(
	private fun askForVersion(): String {
		val allVersions = getAllVersions()
		val version = JOptionPane.showInputDialog(null, 
				"We don't have support for 'Latest Version' profiles yet.\nChange the profile to a specific version\nor select the version here.", 
				"Version?", 
				JOptionPane.QUESTION_MESSAGE, 
				null, 
				allVersions.toTypedArray(), 
				allVersions[0]) //TODO: possibility of index out of bounds if there are no versions
		
		return (version ?: allVersions[0]) as String
		
	}
	private fun getAllVersions() = File(workingDir, "versions/").listFiles().map { it.name }.filterNot { it.startsWith(".") }
	
}
