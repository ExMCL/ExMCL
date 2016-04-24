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

import com.n9mtq4.exmcl.forgemods.data.ModData
import com.n9mtq4.exmcl.forgemods.data.ModProfile
import com.n9mtq4.exmcl.forgemods.ui.ForgeTab
import com.n9mtq4.kotlin.extlib.pstAndUnit
import net.minecraft.launcher.Launcher
import java.io.File
import java.io.IOException
import javax.swing.JOptionPane

/**
 * Created by will on 2/15/16 at 8:19 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

fun copyToMods(launcher: Launcher, modProfile: ModProfile) {
	
	val mojangLauncher: com.mojang.launcher.Launcher = launcher.launcher
	val workingDir = mojangLauncher.workingDirectory
	val modDir = File(workingDir, "mods/")
	modDir.mkdirs()
	
	modProfile.modList.filter { it.enabled }.forEach { 
		try {
			
			copyFile(it.file, File(modDir, it.getName()))
			println("Copied ${it.file.absolutePath}")
			
		}catch (e: IOException) {
			e.printStackTrace()
			JOptionPane.showMessageDialog(null, "Error setting up mod " + it.getName() + "!\n" +
					"Did you move/delete it?", "Error", JOptionPane.ERROR_MESSAGE)
		}
	}
	
}

fun cleanup(launcher: Launcher) {
	
	val mojangLauncher: com.mojang.launcher.Launcher = launcher.launcher
	val workingDir = mojangLauncher.workingDirectory
	val modDir = File(workingDir, "mods/")
	if (!modDir.exists()) return
	
	val mods = modDir.listFiles() ?: return
	mods.
			filter { !it.isDirectory }.
			filter { it.name.endsWith(".jar") || it.name.endsWith(".zip") }.
			forEach { 
				println("Deleted: ${it.absolutePath}")
				it.delete()
			}
	
}

fun firstRunCleanup(launcher: Launcher, modData: ModData, forgeTab: ForgeTab) {
	
	val mojangLauncher: com.mojang.launcher.Launcher = launcher.launcher
	val workingDir = mojangLauncher.workingDirectory
	val modDir = File(workingDir, "mods/")
	val hasRun = File(modDir, "exmcl.txt")
	
	if (hasRun.exists()) return
	
	val filesInModsDir = modDir.listFiles() // get mod contents
//	if there is no mods dir
	if (filesInModsDir == null) {
//		if null make the ran file and stop
//		make text file
		hasRun.parentFile.mkdirs()
		hasRun.createNewFile()
//		job is done
		return
	}
	
	val exmclModsDir = File(workingDir, "mods_exmcl/")
	
	//	copy the children and delete the original
	filesInModsDir.filter { !it.isDirectory }.filter { it.name.endsWith(".jar") || it.name.endsWith(".zip") }.forEach {
//		DON'T BUBBLE AND BREAK EVERYTHING
		pstAndUnit {
			val newFile = File(exmclModsDir, it.name)
			copyFile(it, newFile)
			it.delete()
		}
	}
	
//	make the ran file
	hasRun.parentFile.mkdirs()
	hasRun.createNewFile()
	
//	get the default profile
	val defaultProfile = modData.profiles[0]
	
	val filesInExmclMods = exmclModsDir.listFiles() ?: return // get all mods copied, if there are none, return
	
//	go through and add all the mods
	filesInExmclMods.
			filter { !it.isDirectory }.
			filter { it.name.endsWith(".jar") || it.name.endsWith(".zip") }.
			forEach { defaultProfile.addMod(it) }
	
//	refresh the forge tab
	forgeTab.refresh()
	
}
