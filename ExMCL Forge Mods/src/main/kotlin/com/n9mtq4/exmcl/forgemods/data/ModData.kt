package com.n9mtq4.exmcl.forgemods.data

import com.n9mtq4.exmcl.forgemods.utils.readSerializable
import com.n9mtq4.exmcl.forgemods.utils.writeSerializable
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
		private val MOD_LOCATION = "data/forgemods.dat"
	}
	
	object Loader {
		
		fun load(): ModData {
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
		
		private fun createNewModData(): ModData {
			val modData = ModData()
			val defaultProfile = ModProfile("Default")
			modData.addProfile(defaultProfile)
			return modData
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
	
	fun getSelectedProfile() = profiles[selectedProfileIndex]
	fun addProfile(profile: ModProfile) = profiles.add(profile)
	fun getProfileByName(name: String) = profiles.find { it.profileName == name }
	fun getProfileNames() = profiles.map { it.profileName }
	fun removeProfile(profileIndex: Int) = profiles.removeAt(profileIndex)
	
}
