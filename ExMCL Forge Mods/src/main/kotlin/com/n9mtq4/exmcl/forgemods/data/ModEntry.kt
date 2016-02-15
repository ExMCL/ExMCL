package com.n9mtq4.exmcl.forgemods.data

import java.io.File
import java.io.Serializable

/**
 * Created by will on 2/14/16 at 10:05 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
data class ModEntry(var file: File, var enabled: Boolean) : Serializable {
	
	companion object {
		private val serialVersionUID = 4972165914163468102L;
	}
	
}
