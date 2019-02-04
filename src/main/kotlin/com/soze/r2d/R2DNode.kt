package com.soze.lifegame.uibuilder

import com.badlogic.gdx.scenes.scene2d.Actor

data class R2DNode(
	var type: Any,
	var component: Component?,
	var actor: Actor?,
	var parent: R2DNode?,
	var children: MutableList<R2DNode>
) {

	override fun toString(): String {
		val sb = StringBuilder()
		sb.append("\n")
		sb.append("\n[type = ${type}]")
		sb.append("\n[component = ${component}]")
		sb.append("\n[actor = ${actor?.name}]")
		sb.append("\n[parent = ${parent?.type}]")
		sb.append("\n[children = ${children}]")
		return sb.toString()
	}
}