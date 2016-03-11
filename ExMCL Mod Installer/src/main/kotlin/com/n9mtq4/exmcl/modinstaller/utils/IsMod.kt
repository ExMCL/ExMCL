package com.n9mtq4.exmcl.modinstaller.utils

import java.io.File

/**
 * Created by will on 3/10/16 at 7:30 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
fun File.isMod() = this.name.trim().toLowerCase().endsWith(".zip") || this.name.trim().toLowerCase().endsWith(".jar")
