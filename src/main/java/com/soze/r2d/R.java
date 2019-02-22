package com.soze.r2d;

import java.util.ArrayList;
import java.util.List;

public class R {

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

}
