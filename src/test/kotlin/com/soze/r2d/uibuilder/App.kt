package com.soze.r2d.uibuilder

import com.soze.lifegame.uibuilder.Component
import com.soze.lifegame.uibuilder.Element
import com.soze.lifegame.uibuilder.R
import com.soze.lifegame.uibuilder.UiState

class App(props: UiState) : Component(props) {

  override fun render(): Element {
    val children = props["children"] as MutableList<Element>
    return R.createElement("LABEL", UiState(), children)
  }
}