package com.soze.lifegame.uibuilder

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.soze.lifegame.FakeShaderProgram
import com.soze.lifegame.FakeSpriteBatch
import com.soze.lifegame.GdxTestRunner
import com.soze.r2d.uibuilder.App
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(GdxTestRunner::class)
class RTest {

    @Before
    fun before() {

    }

    @Test
    fun createElement() {
        val labelProps = UiState()
        labelProps["text"] = "WHY"
        labelProps["labelStyle"] = Label.LabelStyle(BitmapFont(), Color.WHITE)
        val element = R.createElement(
          App::class, UiState(),
          listOf(
            R.createElement("LABEL", labelProps),
            R.createElement("LABEL", labelProps),
            R.createElement("TABLE", UiState(),
              listOf(
                R.createElement("LABEL", labelProps),
                R.createElement("LABEL", labelProps)
              )
            )
          )
        )

        val group = Group()
        group.width = 500f
        group.height = 500f
        group.touchable = Touchable.enabled
        R2D.render(element, group)
        println(group)

        val stage = Stage(ScreenViewport(), FakeSpriteBatch(FakeShaderProgram("abc", "abc")))
        stage.addActor(group)
        stage.act()
        stage.touchDown(50, 50, 1, 1)
    }

}