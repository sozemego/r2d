package com.soze.lifegame.uibuilder

import org.junit.Assert.*
import org.junit.Test

class UiStateTest  {


  @Test
  fun testMerge() {
    val state = UiState()
    state["number"] = 5
    state["text"] = "SOME TEXT"
    state["unchanged"] = 12f

    val toMerge = UiState()
    toMerge["number"] = 15
    toMerge["text"] = "NEXT TEXT"

    state.merge(toMerge)

    assertEquals(15, state["number"])
    assertEquals("NEXT TEXT", state["text"])
    assertEquals(12f, state["unchanged"])
  }

  @Test
  fun testClear() {
    val state = UiState()
    state["number"] = 5
    state["text"] = "SOME TEXT"
    state["unchanged"] = 12f

    assertEquals(5, state["number"])
    assertEquals("SOME TEXT", state["text"])
    assertEquals(12f, state["unchanged"])

    state.clear()
    assertEquals(null, state["number"])
    assertEquals(null, state["text"])
    assertEquals(null, state["unchanged"])
  }

}