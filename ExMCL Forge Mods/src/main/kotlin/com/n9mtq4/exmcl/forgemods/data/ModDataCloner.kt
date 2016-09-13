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

package com.n9mtq4.exmcl.forgemods.data

import java.io.File

/**
 * Created by will on 9/13/2016 at 6:55 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

internal fun ModProfile.deepCopy(profileName: String = this.profileName, modList: ModProfile.ModList = this.modList): ModProfile {
	
	val clonedModList = modList.map { it.deepCopy() }
	val newModList = arrayListOf<ModEntry>()
	
	clonedModList.toCollection(newModList)
	
	val profile = ModProfile(profileName, newModList)
	
	return profile
	
}

private fun ModEntry.deepCopy(file: File = this.file, enabled: Boolean = this.enabled): ModEntry {
	
	val clonedModEntry = ModEntry(File(file.path), enabled) // file may not need to be cloned
	
	return clonedModEntry
	
}
