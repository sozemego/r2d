package com.soze.lifegame.uibuilder

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.utils.Drawable

class NullDrawable: Drawable {

	override fun setRightWidth(rightWidth: Float) {
	}

	override fun getLeftWidth(): Float {
		return 0f
	}

	override fun setMinHeight(minHeight: Float) {
	}

	override fun setBottomHeight(bottomHeight: Float) {
	}

	override fun setTopHeight(topHeight: Float) {
	}

	override fun draw(batch: Batch?, x: Float, y: Float, width: Float, height: Float) {
	}

	override fun getBottomHeight(): Float {
		return 0f
	}

	override fun getRightWidth(): Float {
		return 0f
	}

	override fun getMinWidth(): Float {
		return 0f
	}

	override fun getTopHeight(): Float {
		return 0f
	}

	override fun setMinWidth(minWidth: Float) {
	}

	override fun setLeftWidth(leftWidth: Float) {
	}

	override fun getMinHeight(): Float {
		return 0f
	}

}