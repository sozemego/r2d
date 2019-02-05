package com.soze.lifegame.uibuilder

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group

data class R2DNode(
	var type: Any = "VOID",
	var component: Component? = null,
	var actor: Actor? = null,
	var group: Group? = null,
	var parent: R2DNode? = null,
	var children: MutableList<R2DNode> = ArrayList()
) {

	override fun toString(): String {
		val sb = StringBuilder()
		sb.append("[ ")
		sb.append("\ntype = ${type}")
		sb.append("\ncomponent = ${component}")
		sb.append("\nactor = ${actor?.name}")
		sb.append("\ngroup = ${actor?.name}")
		sb.append("\nparent = ${parent?.type}")
		sb.append("\nchildren = ${children}")
		sb.append(" ]")
		return sb.toString()
	}
}