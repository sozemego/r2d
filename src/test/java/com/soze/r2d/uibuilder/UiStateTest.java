package com.soze.r2d.uibuilder;

import com.soze.r2d.UiState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UiStateTest {


  @Test
  public void testMerge() {
    UiState state = new UiState();
    state.set("number", 5);
    state.set("text", "SOME TEXT");
    state.set("unchanged", 12f);

    UiState toMerge = new UiState();
    toMerge.set("number", 15);
    toMerge.set("text", "NEXT TEXT");

    state.merge(toMerge);

    assertEquals(15, state.get("number"));
    assertEquals("NEXT TEXT", state.get("text"));
    assertEquals(12f, state.get("unchanged"));
  }

  @Test
  public void testClear() {
    UiState state = new UiState();
    state.set("number", 5);
    state.set("text", "SOME TEXT");
    state.set("unchanged", 12f);

    assertEquals(5, state.get("number"));
    assertEquals("SOME TEXT", state.get("text"));
    assertEquals(12f, state.get("unchanged"));

    state.clear();
    assertEquals(null, state.get("number"));
    assertEquals(null, state.get("text"));
    assertEquals(null, state.get("unchanged"));
  }


}
