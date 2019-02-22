package com.soze.r2d;

import java.util.ArrayList;
import java.util.List;

public class Element {

  private final Object type;
  private final UiState props;
  private final List<Element> children;

  public Element(Object type, UiState props, List<Element> children) {
    this.type = type;
    this.props = props;
    this.children = children;
  }

  public Element(Object type, UiState props) {
    this(type, props, new ArrayList<>());
  }

  public Element(Object type, List<Element> children) {
    this(type, new UiState(), children);
  }

  public Element(Object type) {
    this(type, new UiState(), new ArrayList<>());
  }

  public Object getType() {
    return type;
  }

  public UiState getProps() {
    return props;
  }

  public List<Element> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    return "Element{" +
             "type=" + type +
             ", props=" + props +
             ", children=" + children +
             '}';
  }
}
