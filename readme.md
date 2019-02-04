R2D
----

This project aims to create React-like experience for creating user interfaces
in Scene 2D. It's developed to use together with LibGDX game engine, but that can later change.
This project is in its infancy, don't expect it to work fully.

Example usage
---

```kotlin
R2D.render(App::class, root)

class App(props: UiState): Component(props) {

    init {
      state["count"] = 1
    }
    
    private fun onLabelClick(event: Event): Boolean {
      setState("count" to (state["count"] + 1))
    }

    fun render(): Element {
      val count = state["count"].toString()
      val labelProps = UiState(
        "text" to count,
        "onClick" to this::onLabelClick
      )
      return R.createElement("TABLE", UiState(), listOf(
        R.createElement("LABEL", labelProps)
      ))
    }
}

```

This will render the App component into the root (Group from scene 2d), recursively.
When you click the label, the UI will be rerendered, the count will be updated (state is saved).
If there are no significant UI changes (e.g. LABEL changes into a TABLE), then the same Actor
will be reused (same as React)

At this point, this library renders Label, Table, TextField ui elements, but it does not support all possible properties
of those elements. They will be added as this project progresses.

Roadmap
---
What else needs to be done?

setState right now is synchronous and updates the entire tree again
    - setState should be asynchronous (happen after e.g. handler code is done, otherwise we might be changing a component that no longer exists)
    - setState should only rerender a sub tree of the entire tree. So from the calling component down

Rendering children
    - Right now children are compared one by one when rendered. This is problematic however, because if they would be reordered after each rerender
    we will pass them wrong props (belonging to another element)
    
Performance
    - It's ok right now, but needs to be monitored as the project develops
