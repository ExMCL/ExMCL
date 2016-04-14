package com.n9mtq4.exmcl.modinstaller.data

import com.n9mtq4.exmcl.modinstaller.utils.readSerializable
import com.n9mtq4.exmcl.modinstaller.utils.writeSerializable
import com.n9mtq4.kotlin.extlib.syntax.def
import net.minecraft.launcher.profile.ProfileManager
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.util.ArrayList
import javax.swing.JOptionPane

/**
 * Created by will on 2/14/16 at 10:10 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ModData(val profiles: ArrayList<ModProfile>, var selectedProfileIndex: Int) : Serializable {
	
	companion object {
		private val serialVersionUID = 6369787388893016352L;
		private const val MOD_LOCATION = "data/jarmods.dat"
	}
	
	object Loader {
		
		fun load(): ModData {
//			return createNewModData()
			val file = File(MOD_LOCATION)
			if (!file.exists()) return createNewModData()
			try {
				val modData: ModData = readSerializable(file)
				return modData
			}catch (e: Exception) {
				e.printStackTrace()
				JOptionPane.showMessageDialog(null, "There was an error loading the ModData.\n" +
						"We are generating a new one.", "Error", JOptionPane.ERROR_MESSAGE);
				return createNewModData()
			}
		}
		
		private fun createNewModData() = def {
			val modData = ModData()
			val defaultProfile = ModProfile("Default")
			modData.addProfile(defaultProfile)
			modData
		}
		
	}
	
	constructor(): this(ArrayList<ModProfile>(), 0)
	
	@Throws(IOException::class)
	fun save() = save(File(MOD_LOCATION))
	
	@Throws(IOException::class)
	private fun save(file: File) {
		file.parentFile.mkdirs()
		writeSerializable(this, file)
	}
	
	fun containsProfileByName(name: String) = profiles.filter { it.profileName == name }.size > 0
	fun getSelectedProfile() = profiles[selectedProfileIndex]
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
