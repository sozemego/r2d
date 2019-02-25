package com.soze.r2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class R2D {

  private static final Logger LOG = LoggerFactory.getLogger(R2D.class);
  private static int RENDERS = 0;

  private static Runnable stateChangeCallback = () -> {
  };
  private static R2DNode currentVDom = new R2DNode();


  public static void render(Element element, Group group) {
    long time0 = System.nanoTime();
    LOG.debug("First render");
    RENDERS++;

    Actor focus = null;
    if (group.getStage() != null) {
      focus = group.getStage().getKeyboardFocus();
    }
    stateChangeCallback = () -> {
      LOG.debug("stateChangeCallback called, re-rendering everything!");
      render(element, group);
    };
    renderChild(currentVDom, element, group);
    if (group.getStage() != null) {
      group.getStage().setKeyboardFocus(focus);
    }
    long totalTime = System.nanoTime() - time0;
    LOG.debug("{} ms to render", TimeUnit.NANOSECONDS.toMillis(totalTime));
    LOG.debug("Rendered {} times", RENDERS);
  }

  private static void renderChild(R2DNode vdom, Element element, Group group) {
    vdom.setGroup(group);
    Object type = element.getType();
    boolean differentType = vdom.getType() != type;
    if (differentType) {
      group.clearChildren();
      vdom.getChildren().clear();
    }
    vdom.setType(type);
    if (type instanceof String) {
      renderStringType(vdom, element, group, differentType);
    }
    if (type instanceof Class<?>) {
      renderComponentType(vdom, element, group, differentType);
    }
  }

  private static void renderStringType(R2DNode vdom, Element element, Group group, boolean differentType) {
    UiState props = element.getProps();
    boolean show = props.get("show", true);
    Actor actor = getActor(vdom, element, differentType);
    if (!show) {
      group.removeActor(actor);
      return;
    }
    
    vdom.setActor(actor);
    
    if (group instanceof Table) {
      Table table = (Table) group;
      boolean row = props.get("row", false);
      if (row) {
        table.row();
      }
      Cell cell = table.add(actor);
      int colspan = (int) props.get("colspan", 1);
      cell.colspan(colspan);

    } else {
      group.addActor(actor);
    }
    if (actor instanceof Group) {
      Group currentGroup = (Group) actor;
      currentGroup.clearChildren();
      List<Element> children = (List<Element>) props.get("children");
      List<R2DNode> nextChildren = new ArrayList(children.size());
      for (Element child : children) {
        R2DNode node = new R2DNode();
        node.setType("VOID");
        nextChildren.add(node);
      }
      List<R2DNode> previousChildren = vdom.getChildren();
      for (int i = 0; i < children.size(); i++) {
        Element childElement = children.get(i);
        R2DNode currentChild = previousChildren.size() > i ? previousChildren.get(i) : null;
        R2DNode nextVDom;

        //if current child is null, we are creating a new one
        if (currentChild == null) {
          nextVDom = new R2DNode();
          nextVDom.setType(childElement.getType());
          nextVDom.setParent(vdom);
        } else {
          // a child was present already, we need to check if it's the same or different
          boolean differentChildType = childElement.getType() != currentChild.getType();
          if (!differentChildType) {
            nextVDom = currentChild;
          } else {
            nextVDom = new R2DNode();
            nextVDom.setType(childElement.getType());
            nextVDom.setParent(vdom);
          }
        }
        nextChildren.set(i, nextVDom);
        renderChild(nextVDom, childElement, currentGroup);
      }

      vdom.getChildren().clear();
      vdom.getChildren().addAll(nextChildren);
    }

  }

  private static Actor getActor(R2DNode vdom, Element element, boolean differentType) {
    if (!differentType && vdom.getActor() != null) {
      return applyProps(element.getProps(), vdom.getActor());
    }
    return createActor((String) element.getType(), element.getProps());
  }

  private static void renderComponentType(R2DNode vdom, Element element, Group group, boolean differentType) {
    Component component = getComponent(vdom, element, differentType);
    component.setStateChangeCallback(R2D.stateChangeCallback);
    Element nextElement = component.render();
    vdom.setComponent(component);
    R2DNode nextVdom = getNextVdomComponent(vdom, nextElement, differentType);
    vdom.getChildren().clear();
    vdom.getChildren().add(nextVdom);
    renderChild(nextVdom, nextElement, group);
  }

  private static Component getComponent(R2DNode vdom, Element element, boolean differentType) {
    if (!differentType && vdom.getComponent() != null) {
      return vdom.getComponent();
    }
    return createComponent((Class<Component>) element.getType(), element.getProps());
  }

  private static R2DNode getNextVdomComponent(R2DNode vdom, Element nextElement, boolean differentType) {
    if (!differentType && !vdom.getChildren().isEmpty()) {
      return vdom.getChildren().get(0);
    }
    R2DNode nextVDom = new R2DNode();
    nextVDom.setType(nextElement.getType());
    nextVDom.setParent(vdom);
    return nextVDom;
  }

  private static Actor createActor(String type, UiState props) {
    Actor actor = null;
    switch (type) {
      case "LABEL":
        actor = createLabel(props);
        break;
      case "TABLE":
        actor = createTable(props);
        break;
      case "TEXT_FIELD":
        actor = createTextField(props);
        break;
      case "BUTTON":
        actor = createButton(props);
        break;
      case "WINDOW":
        actor = createWindow(props);
        break;
      case "DIALOG":
        actor = createDialog(props);
        break;
      default:
        actor = new Table();
    }
    return applyPropsActor(props, actor);
  }

  private static Actor createLabel(UiState props) {
    Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
    return applyProps(props, new Label(null, style));
  }

  private static Actor createTable(UiState props) {
    return applyProps(props, new Table());
  }

  private static Actor createTextField(UiState props) {
    TextField.TextFieldStyle style = new TextField.TextFieldStyle();
    style.font = new BitmapFont();
    return applyProps(props, new TextField(null, style));
  }

  private static Actor applyProps(UiState props, Actor actor) {
    Class<?> clazz = actor.getClass();
    applyPropsActor(props, actor);
    if (clazz.isAssignableFrom(Table.class)) {
      return applyProps(props, (Table) actor);
    }
    if (clazz.isAssignableFrom(TextField.class)) {
      return applyProps(props, (TextField) actor);
    }
    if (clazz.isAssignableFrom(Label.class)) {
      return applyProps(props, (Label) actor);
    }
    if (clazz.isAssignableFrom(Dialog.class)) {
      return applyProps(props, (Dialog) actor);
    }
    return actor;
  }

  private static Table applyProps(UiState props, Table table) {
    table.setFillParent(props.get("fillParent", false));
    return table;
  }

  private static TextField applyProps(UiState props, TextField textField) {
    TextField.TextFieldStyle defaultStyle = new TextField.TextFieldStyle();
    defaultStyle.font = new BitmapFont();
    defaultStyle.fontColor = Color.WHITE;
    defaultStyle.cursor = DefaultCursor.getCursor();
    defaultStyle.selection = DefaultCursor.getCursor();

    TextField.TextFieldStyle style = props.get("textFieldStyle", defaultStyle);
    String text = props.get("text", "");
    textField.setText(text);
    textField.setStyle(style);

    BiConsumer<TextField, Character> onChange = (BiConsumer<TextField, Character>) props.get("onChange");
    if (onChange != null) {
      textField.setTextFieldListener(onChange::accept);
    }
    return textField;
  }

  private static Label applyProps(UiState props, Label label) {
    Label.LabelStyle defaultStyle = new Label.LabelStyle();
    defaultStyle.font = new BitmapFont();
    defaultStyle.fontColor = Color.WHITE;
    String text = props.get("text", "");
    Label.LabelStyle style = props.get("labelStyle", defaultStyle);
    label.setText(text);
    label.setStyle(style);
    boolean disabled = props.get("disabled", false);
    if (disabled) {
      style.fontColor = Color.GRAY;
    }
    Function<Event, Boolean> onClick = (Function<Event, Boolean>) props.get("onClick");
    if (label.getListeners().size > 0) {
      label.getListeners().removeIndex(0);
    }
    if (onClick != null) {
      label.addListener((event) -> {
        if (!disabled && event instanceof InputEvent && ((InputEvent) event).getType() == InputEvent.Type.touchDown) {
          return onClick.apply(event);
        }
        return false;
      });
    }
    return label;
  }
  
  private static Dialog applyProps(UiState props, Dialog dialog) {
    dialog.setPosition(props.get("x", 0f), props.get("y", 0f), Align.center);
    dialog.getTitleLabel().setText(props.get("title", ""));
    
    return dialog;
  }

  private static Actor applyPropsActor(UiState props, Actor actor) {
    actor.setName((String) props.get("name"));
    return actor;
  }

  private static Actor createButton(UiState props) {
    LOG.warn("CREATING NON IMPLEMENTED BUTTON");
    return new Table();
  }
  
  private static Actor createWindow(UiState props) {
    LOG.warn("CREATING NON IMPLEMENTED WINDOW");
    return new Table();
  }
  
  private static Actor createDialog(UiState props) {
    Window.WindowStyle style = new Window.WindowStyle();
    style.titleFont = new BitmapFont();
    style.titleFontColor = Color.WHITE;
    
    Dialog dialog = new Dialog(props.get("title", ""), style);
    return applyProps(props, dialog);
  }

  private static Component createComponent(Class<Component> clazz, UiState props) {
    try {
      Constructor<Component> constructor = clazz.getDeclaredConstructor(props.getClass());
      return constructor.newInstance(props);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    throw new IllegalStateException(clazz + " does not have a proper constructor");
  }

}
