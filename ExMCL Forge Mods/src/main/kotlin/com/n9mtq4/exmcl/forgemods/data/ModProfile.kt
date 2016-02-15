package com.n9mtq4.exmcl.forgemods.data

import java.io.File
import java.io.Serializable
import java.util.ArrayList

/**
 * Created by will on 2/14/16 at 10:07 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
data class ModProfile(var profileName: String, var modList: ArrayList<ModEntry>) : Serializable {
	
	companion object {
		private val serialVersionUID = 2512763657900345972L;
	}
	
	constructor(profileName: String) : this(profileName, ArrayList<ModEntry>())
	
	fun addMod(file: File, enabled: Boolean) = modList.add(ModEntry(file, enabled))
	fun addMod(file: File) = addMod(file, true)
	
}
