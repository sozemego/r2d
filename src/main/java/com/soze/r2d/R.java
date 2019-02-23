package com.soze.r2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class R {

  private static Consumer<Runnable> STATE_UPDATER = (r) -> r.run();

  public static Element createElement(Object type, UiState props, List<Element> children) {
    props.set("children", children);
    return new Element(type, props, children);
  }

  public static Element createElement(Object type, UiState props) {
    return createElement(type, props, new ArrayList<>());
  }

  public static Element createElement(Object type, List<Element> children) {
    return createElement(type, new UiState(), children);
  }

  public static Element createElement(Object type) {
    return createElement(type, new UiState(), new ArrayList<>());
  }

  /**
   * Registers a consumer of runnables.
   * The purpose of this consumer is so that the users of R library
   * can specify their own thread in which renders after setState occur.
   * For example in LibGDX, there are many operations that are only permitted in the main render thread.
   * @param stateUpdater
   */
  public static void registerStateRunner(Consumer<Runnable> stateUpdater) {
    STATE_UPDATER = Objects.requireNonNull(stateUpdater);
  }

  public static Consumer<Runnable> getStateUpdater() {
    return STATE_UPDATER;
  }
}
