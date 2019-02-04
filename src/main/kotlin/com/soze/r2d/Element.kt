package com.soze.lifegame.uibuilder

import java.lang.StringBuilder

data class Element(
	val type: Any,
	val props: UiState = UiState(),
	val children: List<Element> = ArrayList()
) {

	override fun toString(): String {
		return StringBuilder().apply<StringBuilder> {
			append("[\n")
			append(" [type = $type] \n")
			append(" [props = $props] \n")
			append(" [children = $children] \n")
			append("]")
		}.toString()
	}

}