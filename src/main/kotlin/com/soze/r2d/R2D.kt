package com.soze.lifegame.uibuilder

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.DelayedRemovalArray
import com.soze.r2d.DefaultCursor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

fun com.badlogic.gdx.scenes.scene2d.ui.Table.lastCell(): Cell<*>? {
  val cells = this.cells
  return if (cells.size > 0) cells.get(cells.size - 1) else null
}

/**
 * Renders R elements into a Scene2D structure.
 */
object R2D {

  private var currentVDom: R2DNode = R2DNode("INITIAL")
  private var LOG: Logger = LoggerFactory.getLogger(R2D::class.java)

  private var stateChangeCallback: () -> Unit = fun() {}

  @JvmStatic
  fun render(element: Element, group: Group) {
    val time0 = System.nanoTime()
    LOG.debug("First render")
    val focus = group.stage?.keyboardFocus
    R2D.stateChangeCallback = fun() {
      LOG.debug("stateChangeCallback called, re-rendering everything!")
      render(element, group)
    }
    renderChild(currentVDom, element, group)

    group.stage?.keyboardFocus = focus
    val totalTime = System.nanoTime() - time0
    LOG.debug("${TimeUnit.NANOSECONDS.toMillis(totalTime)} ms to render")
  }

  private fun renderChild(vdom: R2DNode, element: Element, group: Group) {
    vdom.group = group
    val type = element.type
    var differentType = vdom.type != type
    if (differentType) {
      group.clearChildren()
      vdom.children.clear()
    }
    vdom.type = type
    if (type is String) {
      renderStringType(vdom, element, group, differentType)
    }
    if (type is KClass<*> || type is Class<*>) {
      renderComponentType(vdom, element, group, differentType)
    }
  }

  private fun renderStringType(vdom: R2DNode, element: Element, group: Group, differentType: Boolean) {
    val props = element.props
    val actor = getActor(vdom, element, differentType)
    vdom.actor = actor

    if (group is Table) {
      val row = props["row"] as Boolean? ?: false
      if (row) {
        group.lastCell()?.let {
          it.row()
        }
      }
      group.add(actor).apply {
        val colspan = props["colspan"] as Int? ?: 1
        colspan(colspan)
      }
    } else {
      group.addActor(actor)
    }
    if (actor is Group) {
      actor.clearChildren()
      val children = props["children"] as List<Element>
      val nextChildren: MutableList<R2DNode> = ArrayList(children.size)
      for (i in 0 until children.size) {
        nextChildren.add(R2DNode("VOID"))
      }
      val previousChildren = vdom.children
      children.forEachIndexed { index, element ->
        var currentChild: R2DNode? = if (previousChildren.size > index) previousChildren[index] else null
        var nextVdom: R2DNode?
        //if current child is null, we are creating a new one
        if (currentChild == null) {
          nextVdom = R2DNode(type = element.type, parent = vdom)
        } else {
          // a child was present already, we need to check if it's the same or different
          val differentChildType = element.type != currentChild.type
          //if type is the same
          if (!differentChildType) {
            nextVdom = currentChild
          } else {
            nextVdom = R2DNode(type = element.type, parent = vdom)
          }
        }
        nextChildren[index] = nextVdom
        renderChild(nextVdom, element, actor)
      }
      vdom.children = nextChildren
    }

  }

  private fun getActor(vdom: R2DNode, element: Element, differentType: Boolean): Actor {
    if (!differentType && vdom.actor != null) {
      return applyProps(element.props, vdom.actor!!)
    }
    return createActor(element.type as String, element.props)
  }

  private fun renderComponentType(vdom: R2DNode, element: Element, group: Group, differentType: Boolean) {
    val component = getComponent(vdom, element, differentType)
    component.stateChangeCallback = R2D.stateChangeCallback
    val nextElement = component.render()
    vdom.component = component
    val nextVdom = getNextVdomComponent(vdom, nextElement, differentType)
    vdom.children.clear()
    vdom.children.add(nextVdom)
    renderChild(nextVdom, nextElement, group)
  }

  private fun getComponent(vdom: R2DNode, element: Element, differentType: Boolean): Component {
    if (!differentType && vdom.component != null) {
      return vdom.component!!
    }
    return createComponent(element.type, element.props)
  }

  private fun getNextVdomComponent(vdom: R2DNode, nextElement: Element, differentType: Boolean): R2DNode {
    if (!differentType && vdom.children.isNotEmpty()) {
      return vdom.children[0]
    }
    return R2DNode(type = nextElement.type, parent = vdom)
  }

  private fun createActor(type: String, props: UiState): Actor {
    val actor = when (type) {
      "LABEL" -> createLabel(props)
      "TABLE" -> createTable(props)
      "TEXT_FIELD" -> createTextField(props)
      "BUTTON" -> createButton(props)
      else -> Table()
    }
    return applyPropsActor(props, actor)
  }

  private fun createLabel(props: UiState): Actor {
    val style = Label.LabelStyle(BitmapFont(), Color.WHITE)
    return applyProps(props, Label(null, style))
  }

  private fun createTable(props: UiState): Actor {
    return applyProps(props, Table())
  }

  private fun createTextField(props: UiState): Actor {
    val style = TextField.TextFieldStyle()
    style.font = BitmapFont()
    return applyProps(props, TextField(null, style))
  }

  private fun applyProps(props: UiState, actor: Actor): Actor {
    val clazz = actor::class.java
    applyPropsActor(props, actor)
    return when (clazz) {
      Table::class.java -> applyProps(props, actor as Table)
      TextField::class.java -> applyProps(props, actor as TextField)
      Label::class.java -> applyProps(props, actor as Label)
      else -> actor
    }
  }

  private fun applyProps(props: UiState, table: Table): Table {
    table.setFillParent(props["fillParent"] as Boolean? ?: false)
    return table
  }

  private fun applyProps(props: UiState, textField: TextField): TextField {
    val defaultStyle = TextField.TextFieldStyle()
    defaultStyle.font = BitmapFont()
    defaultStyle.fontColor = Color.WHITE
    defaultStyle.cursor = DefaultCursor.get()
    defaultStyle.selection = DefaultCursor.get()

    val style = props["textFieldStyle"] as TextField.TextFieldStyle? ?: defaultStyle
    val text = props["text"] as String? ?: ""
    textField.text = text
    textField.style = style

    val onChange = props["onChange"] as ((field: TextField, char: Char) -> Unit)?
    onChange?.let {
      textField.setTextFieldListener(onChange)
    }
    return textField
  }

  private fun applyProps(props: UiState, label: Label): Label {
    val defaultStyle = Label.LabelStyle()
    defaultStyle.font = BitmapFont()
    defaultStyle.fontColor = Color.WHITE
    val text = props["text"] as String? ?: ""
    val style = props["labelStyle"] as Label.LabelStyle? ?: defaultStyle
    label.setText(text)
    label.style = style
    val disabled = props["disabled"] as Boolean? ?: false
    if (disabled) {
      style.fontColor = Color.GRAY
    }
    val onClick = props["onClick"] as ((event: Event) -> Boolean)?
    if (label.listeners.size > 0) {
      (label.listeners as DelayedRemovalArray).removeIndex(0)
    }
    onClick?.let {
      label.addListener(fun(event: Event): Boolean {
        if (!disabled && event is InputEvent && event.type == InputEvent.Type.touchDown) {
          return onClick(event)
        }
        return false
      })
    }
    return label
  }

  private fun applyPropsActor(props: UiState, actor: Actor): Actor {
    actor.name = props["name"] as String?
    return actor
  }

  private fun createButton(props: UiState): Actor {
    LOG.warn("CREATING NON IMPLEMENTED BUTTON")
    return Table()
  }

  private fun createComponent(clazz: Any, props: UiState): Component {
    if (clazz is Class<*>) {
      val declaredConstructor = clazz.getDeclaredConstructor(UiState::class.java)
      return declaredConstructor.newInstance(props) as Component
    }
    if (clazz is KClass<*>) {
      val primaryConstructor = clazz.primaryConstructor!!
      return primaryConstructor.call(props) as Component
    }
    throw IllegalStateException("$clazz is not a class")
  }


}