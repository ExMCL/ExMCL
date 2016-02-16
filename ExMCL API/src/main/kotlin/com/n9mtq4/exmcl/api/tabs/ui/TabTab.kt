package com.n9mtq4.exmcl.api.tabs.ui

import javax.swing.JTabbedPane
import javax.swing.UIManager

/**
 * Created by will on 2/13/16 at 12:35 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class TabTab : JTabbedPane() {
	
	init {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (e: Exception) {
			e.printStackTrace()
		}
		
		tabLayoutPolicy = JTabbedPane.WRAP_TAB_LAYOUT
	}
	
}
