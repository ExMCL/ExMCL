package com.n9mtq4.exmcl.api.tabs.creators

import java.awt.Component

/**
 * Created by will on 1/6/16 at 7:55 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
interface TabCreator {
	
	fun addTab(title: String, tab: Component)
	
}
