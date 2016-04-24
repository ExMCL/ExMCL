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

package com.n9mtq4.exmcl.modinstaller.ui

import java.awt.Component
import javax.swing.JDialog
import javax.swing.JProgressBar

/**
 * Created by will on 3/8/16 at 2:46 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class PatchingWindow(val parent: Component) : JDialog() {
	
	private val progress: JProgressBar
	
	init {
		
		title = "Patching Minecraft"
		defaultCloseOperation = DO_NOTHING_ON_CLOSE
		
		this.progress = JProgressBar(0, 100)
		progress.isIndeterminate = true
		
		add(progress)
		
		forceUpdate()
		
	}
	
	internal fun forceUpdate() {
		pack()
		isVisible = true
		setSize(300, size.height)
		isResizable = false
		setLocationRelativeTo(parent)
	}
	
}
