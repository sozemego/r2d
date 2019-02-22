package com.soze.r2d;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

public class R2DNode {

  private Object type = "VOID";
  private Component component = null;
  private Actor actor = null;
  private Group group = null;
  private R2DNode parent = null;
  private List<R2DNode> children = new ArrayList<>();

  public R2DNode() {

  }

  public Object getType() {
    return type;
  }

  public void setType(Object type) {
    this.type = type;
  }

  public Component getComponent() {
    return component;
  }

  public void setComponent(Component component) {
    this.component = component;
  }

  public Actor getActor() {
    return actor;
  }

  public void setActor(Actor actor) {
    this.actor = actor;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public R2DNode getParent() {
    return parent;
  }

  public void setParent(R2DNode parent) {
    this.parent = parent;
  }

  public List<R2DNode> getChildren() {
    return children;
  }

  public void setChildren(List<R2DNode> children) {
    this.children = children;
  }

  @Override
  public String toString() {
    return "R2DNode{" +
             "\ntype=" + type +
             "\ncomponent=" + component +
             "\nactor=" + actor +
             "\ngroup=" + group +
             "\nparent=" + parent +
             "\nchildren=" + children +
             '}';
  }
}
