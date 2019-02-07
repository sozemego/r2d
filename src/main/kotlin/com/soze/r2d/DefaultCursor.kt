package com.soze.r2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.Drawable

object DefaultCursor {

  private val cursor: Drawable

  init {
    val labelStyle = Label.LabelStyle(BitmapFont(), Color.WHITE)
    val oneCharSizeCalibrationThrowAway = Label("|", labelStyle)

    val cursorColor = Pixmap(
      oneCharSizeCalibrationThrowAway.width.toInt(),
      oneCharSizeCalibrationThrowAway.height.toInt(),
      Pixmap.Format.RGB888
    )
    cursorColor.setColor(Color.GRAY)
    cursorColor.fill()

    cursor = Image(Texture(cursorColor)).drawable
  }

  fun get(): Drawable {
    return cursor
  }

}