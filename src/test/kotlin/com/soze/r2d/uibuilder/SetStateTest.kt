package com.soze.r2d.uibuilder

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.soze.lifegame.FakeShaderProgram
import com.soze.lifegame.FakeSpriteBatch
import com.soze.lifegame.GdxTestRunner
import com.soze.lifegame.uibuilder.R
import com.soze.lifegame.uibuilder.R2D
import com.soze.lifegame.uibuilder.UiState
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(GdxTestRunner::class)
class SetStateTest {

  @Test
  fun counterTest() {
    val stage = Stage(ScreenViewport(), FakeSpriteBatch(FakeShaderProgram("abc", "abc")))
    val group = stage.root

    val trap = ArrayList<String>()

    val labelName = "Counter Label"
    R2D.render(R.createElement(Counter::class, UiState("trap" to trap, "labelName" to labelName)), group)

    val label = stage.root.findActor<Label>(labelName)

    val clickEvent = InputEvent()
    clickEvent.type = InputEvent.Type.touchDown
    label.fire(clickEvent)
    assertEquals("1", label.text.toString())
  }

}