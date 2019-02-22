package com.soze.r2d.uibuilder;

import com.soze.r2d.Component;
import com.soze.r2d.Element;
import com.soze.r2d.R;
import com.soze.r2d.UiState;

import java.util.List;

public class App extends Component {

  public App(UiState props) {
    super(props);
  }

  public Element render() {
    List<Element> children = (List<Element>) getProps().get("children");

    return R.createElement("LABEL", new UiState(), children);
  }

}
