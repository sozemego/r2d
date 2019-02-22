package com.soze.r2d.uibuilder;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.soze.r2d.Component;
import com.soze.r2d.Element;
import com.soze.r2d.R;
import com.soze.r2d.UiState;

import java.util.Arrays;
import java.util.function.Function;

public class Counter extends Component {

  public Counter(UiState props) {
    super(props);
    getState().set("count", 0);
  }

  private boolean onLabelClick(Event event) {
    UiState nextState = new UiState();
    nextState.set("count", (int) getState().get("count") + 1);
    setState(nextState);
    return true;
  }

  public Element render() {
    String count = String.valueOf(getState().get("count"));
    UiState labelProps = new UiState();
    labelProps.set("text", count);
    labelProps.set("name", getProps().get("labelName"));
    labelProps.set("onClick", (Function<Event, Boolean>) this::onLabelClick);

    UiState tableProps = new UiState();
    tableProps.set("fillParent", true);
    return R.createElement("TABLE", tableProps, Arrays.asList(
      R.createElement("LABEL", labelProps)
    ));
  }
}
