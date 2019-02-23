package com.soze.r2d;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class RTest {

  public static class RTestComponent extends Component {

    public RTestComponent(UiState props) {
      super(props);
    }

    @Override
    public Element render() {
      UiState labelProps = new UiState();
      labelProps.set("text", "greatlabel");
      labelProps.set("name", "TEST_LABEL");
      labelProps.set("onClick", (Function<Event, Boolean>) this::onTestClick);
      return R.createElement("LABEL", labelProps);
    }

    private boolean onTestClick(Event event) {
      setState(new UiState());
      return true;
    }
  }

  @Test
  public void registerStateRunner() {
    List<String> trap = new ArrayList<>();
    R.registerStateRunner((r) -> {
      r.run();
      trap.add("RUN");
    });

    Element element = R.createElement(RTestComponent.class);

    Stage stage = new Stage(new ScreenViewport(), new FakeSpriteBatch(new FakeShaderProgram("abc", "abc")));
    Group group = stage.getRoot();
    R2D.render(element, group);

    Label label = stage.getRoot().findActor("TEST_LABEL");

    InputEvent clickEvent = new InputEvent();
    clickEvent.setType(InputEvent.Type.touchDown);
    label.fire(clickEvent);

    assertTrue(trap.size() == 1);
  }
}