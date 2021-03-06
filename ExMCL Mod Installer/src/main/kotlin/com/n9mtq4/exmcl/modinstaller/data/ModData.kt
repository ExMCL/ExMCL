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

package com.n9mtq4.exmcl.modinstaller.data

import com.n9mtq4.exmcl.modinstaller.utils.msg
import com.n9mtq4.exmcl.modinstaller.utils.readModDataFromFile
import com.n9mtq4.exmcl.modinstaller.utils.writeToFile
import com.n9mtq4.kotlin.extlib.syntax.def
import net.minecraft.launcher.profile.ProfileManager
import java.io.File
import java.io.IOException
import java.util.ArrayList
import javax.swing.JOptionPane

/**
 * Created by will on 2/14/16 at 10:10 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ModData(val profiles: ProfileList, var selectedProfileIndex: Int) {
	
	private typealias ProfileList = ArrayList<ModProfile>
	
	companion object {
		private const val MOD_LOCATION = "data/jarmods.json.lzma"
	}
	
	object Loader {
		
		fun load(): ModData {
//			return createNewModData()
			val file = File(MOD_LOCATION)
			if (!file.exists()) return createNewModData()
			try {
				val modData: ModData = readModDataFromFile(file)
				if (modData.selectedProfileIndex == -1) modData.selectedProfileIndex = 0 //hotfix
				return modData
			}catch (e: Exception) {
				e.printStackTrace()
				msg(null, "There was an error loading the ModData.\n" +
						"We are generating a new one.", "Error", JOptionPane.ERROR_MESSAGE)
				return createNewModData()
			}
		}
		
		private fun createNewModData() = def {
			val modData = ModData()
			val defaultProfile = ModProfile("Default")
			modData.addProfile(defaultProfile)
			modData.selectedProfileIndex = 0
			modData
		}
		
	}
	
	constructor(): this(ProfileList(), 0)
	
	@Throws(IOException::class)
	fun save() = save(File(MOD_LOCATION))
	
	@Throws(IOException::class)
	private fun save(file: File) {
		file.parentFile.mkdirs()
		writeToFile(file)
	}
	
	fun containsProfileByName(name: String) = profiles.filter { it.profileName == name }.isNotEmpty()
	fun getSelectedProfile() = def {
		if (selectedProfileIndex == -1) selectedProfileIndex = 0
		profiles[selectedProfileIndex]
	}
	fun addProfile(profile: ModProfile) = profiles.add(profile)
	fun getProfileByName(name: String) = profiles.find { it.profileName == name }
	fun getProfileNames() = profiles.map { it.profileName }
	fun removeProfile(profileIndex: Int) = profiles.removeAt(profileIndex)
	fun removeProfile(modProfile: ModProfile) = profiles.remove(modProfile)
	fun removeProfileByName(name: String) = profiles.filter { it.profileName == name }.forEach { profiles.remove(it) }
	
	fun syncWithProfileManager(profileManager: ProfileManager) {
		
//		adds any new profiles
		profileManager.profiles.filterNot { containsProfileByName(it.key) }.forEach { addProfile(ModProfile(it.key)) }
//		deletes any old profiles
		profiles.filter { profileManager.profiles[it.profileName] == null }.forEach { removeProfile(it) }
		
	}
	
}
