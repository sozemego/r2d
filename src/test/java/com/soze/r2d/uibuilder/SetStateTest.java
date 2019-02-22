package com.soze.r2d.uibuilder;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soze.r2d.FakeShaderProgram;
import com.soze.r2d.FakeSpriteBatch;
import com.soze.r2d.GdxTestRunner;
import com.soze.r2d.R;
import com.soze.r2d.R2D;
import com.soze.r2d.UiState;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class SetStateTest {

  @Test
  public void counterTest() {
    Stage stage = new Stage(new ScreenViewport(), new FakeSpriteBatch(new FakeShaderProgram("abc", "abc")));
    Group group = stage.getRoot();

    List<String> trap = new ArrayList<String>();

    String labelName = "Counter Label";
    UiState counterProps = new UiState();
    counterProps.set("trap", trap);
    counterProps.set("labelName", labelName);
    R2D.render(R.createElement(Counter.class, counterProps), group);

    Label label = stage.getRoot().findActor(labelName);

    InputEvent clickEvent = new InputEvent();
    clickEvent.setType(InputEvent.Type.touchDown);
    label.fire(clickEvent);
    assertEquals("1", label.getText().toString());
  }

}
