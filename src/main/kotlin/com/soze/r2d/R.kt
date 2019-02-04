package com.soze.lifegame.uibuilder

object R {

	fun createElement(
		type: Any,
		props: UiState = UiState(),
		children: List<Element> = ArrayList()
	): Element {
		props["children"] = children
		return Element(type, props, children)
	}

}