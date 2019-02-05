package com.soze.r2d.uibuilder

import com.badlogic.gdx.scenes.scene2d.Event
import com.soze.lifegame.uibuilder.Component
import com.soze.lifegame.uibuilder.Element
import com.soze.lifegame.uibuilder.R
import com.soze.lifegame.uibuilder.UiState

class Counter(props: UiState): Component(props) {

  init {
    state["count"] = 0
  }

  private fun onLabelClick(event: Event): Boolean {
    setState("count" to (state["count"] as Int + 1))
    (props["trap"] as MutableList<String>).add("CLICKED")
    return true
  }

  override fun render(): Element {
    val count = state["count"].toString()
    val labelProps = UiState(
      "text" to "$count",
      "onClick" to this::onLabelClick,
      "name" to props["labelName"]
    )
    return R.createElement("TABLE", UiState("fillParent" to true), listOf(
      R.createElement("LABEL", labelProps)
    ))
  }
}