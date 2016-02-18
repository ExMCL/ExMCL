package com.n9mtq4.exmcl.modinstaller.utils

import com.n9mtq4.exmcl.modinstaller.data.ModData
import net.minecraft.launcher.Launcher
import net.minecraft.launcher.profile.Profile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Created by will on 7/28/15 at 5:02 PM.
 * TODO: this code is an ungodly mess - most of it was converted from java
 */
object MinecraftPatcher {
	
	fun duplicate(launcher: Launcher) {
		val dir = getVersionJarFile(launcher).parentFile
		val destDir = File(getVersionJarFile(launcher).parentFile.parentFile, getMinecraftVersion(launcher) + "_exmcl")
		buildFileTree(dir).forEach { copyFile(it, File(destDir, it.absolutePath.substring(destDir.absolutePath.length))) }
	}
	
	fun copyMods(launcher: Launcher, modData: ModData) {
		modData.getSelectedProfile().modList.forEach { 
			val file = File(getVersionJarFile(launcher).parent, "/tmp/${it.file.name}")
			unzip(it.file, file.absolutePath)
			val fileTree = buildFileTree(file)
			fileTree.forEach { 
				val file1 = File(File(getVersionJarFile(launcher).parent, "/tmp/" + getMinecraftVersion(launcher)), it.absolutePath.substring(file.absolutePath.length))
//				println(file1.absolutePath)
				copyFile(it, file1)
			}
		}
	}
	
	fun getVersionJarPath(launcher: Launcher): String {
		return getVersionJarFile(launcher).absolutePath
	}
	
	fun getVersionJarFile(launcher: Launcher): File {
		
		val launcher1 = launcher.launcher
		val workingDir = launcher1.workingDirectory
		
		val jarLocation = File(workingDir, "versions/" + getMinecraftVersion(launcher) + "/" + getMinecraftVersion(launcher) + ".jar")
		return jarLocation
		
	}
	
	@Throws(IOException::class)
	fun backupJar(launcher: Launcher) {
		
		val source = getVersionJarFile(launcher)
		var dest = source.parentFile
		dest = File(dest, source.name + ".bak")
		copyFile(source, dest)
		
	}
	
	@Throws(IOException::class)
	fun unzip(launcher: Launcher) {
		
		val jarFile = getVersionJarFile(launcher)
		val outputDir = File(jarFile.parent, "/tmp/" + getMinecraftVersion(launcher)).absolutePath
		
		unzip(jarFile, outputDir)
		
	}
	
	/**
	 * http://www.mkyong.com/java/how-to-decompress-files-from-a-zip-file/
	 */
	@Throws(IOException::class)
	private fun unzip(file: File, outputDir: String) {
		
		val buffer = ByteArray(1024)
		val outDir = File(outputDir)
		if (!outDir.exists()) outDir.mkdirs()
		
		val zipInputStream = ZipInputStream(FileInputStream(file))
		var ze = zipInputStream.nextEntry
		
		while (ze != null) {
			
			val fileName = ze.name
			val newFile = File(outputDir + File.separator + fileName)
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
	
	fun zip(launcher: Launcher) {
		val sourceFile = File(getVersionJarFile(launcher).parent, "/tmp/" + getMinecraftVersion(launcher))
		zip(buildFileTree(sourceFile).toTypedArray(), File(sourceFile.parent, "new.jar"), sourceFile)
	}
	
	@Throws(IOException::class)
	fun zip(files: Array<File>, out: File, sourceDir: File) {
		
		val fileNames = Array<String>(files.size) {i -> files[i].absolutePath}
		
/*		val fileNames = arrayOfNulls<String>(files.size)
		for (i in files.indices) {
			fileNames[i] = files[i].absolutePath
		}
		zip(fileNames, out, sourceDir)*/
		zip(fileNames, out, sourceDir)
		
	}
	
	/**
	 * http://www.mkyong.com/java/how-to-compress-files-in-zip-format/
	 */
	@Throws(IOException::class)
	fun zip(files: Array<String>, out: File, sourceDir: File) {
		
		val buffer = ByteArray(1024)
		
		val fos = FileOutputStream(out)
		val zos = ZipOutputStream(fos)
		
		val subIndex = sourceDir.absolutePath.length
		
		for (file in files) {
			
			//			ZipEntry ze = new ZipEntry(file);
			val ze = ZipEntry(file.substring(subIndex, file.length))
			zos.putNextEntry(ze)
			
			val `in` = FileInputStream(file)
			
			var len = `in`.read(buffer)
			while (len > 0) {
				zos.write(buffer, 0, len)
				len = `in`.read(buffer)
			}
			
			`in`.close()
			
		}
		
		zos.closeEntry()
		zos.close()
		
	}
	
	private fun buildFileTree(file: File): ArrayList<File> {
		
		val files = ArrayList<File>()
		if (file.isDirectory) {
			val children = file.listFiles() ?: return files
			for (child in children) {
				files.addAll(buildFileTree(child))
			}
		}else {
			files.add(file)
		}
		return files
		
	}
	
	fun getMinecraftVersion(launcher: Launcher): String {
		
		return getMinecraftVersion(launcher.profileManager.selectedProfile)
		
	}
	
	fun getMinecraftVersion(profile: Profile): String {
		
		return profile.lastVersionId
		
	}
	
}
