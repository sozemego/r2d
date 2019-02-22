package com.soze.r2d.uibuilder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soze.r2d.Element;
import com.soze.r2d.FakeShaderProgram;
import com.soze.r2d.FakeSpriteBatch;
import com.soze.r2d.GdxTestRunner;
import com.soze.r2d.R;
import com.soze.r2d.R2D;
import com.soze.r2d.UiState;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(GdxTestRunner.class)
public class RTest {

  @Test
  public void createElement() {
    UiState labelProps = new UiState();
    labelProps.set("text", "WHY");
    labelProps.set("labelStyle", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    Element element = R.createElement(
      App.class, new UiState(), Arrays.asList(
        R.createElement("LABEL", labelProps),
        R.createElement("LABEL", labelProps),
        R.createElement("TABLE", new UiState(), Arrays.asList(
          R.createElement("LABEL", labelProps),
          R.createElement("LABEL", labelProps)
                        )
        )
      )
    );

    Group group = new Group();
    group.setWidth(500f);
    group.setHeight(500f);
    group.setTouchable(Touchable.enabled);
    R2D.render(element, group);
    System.out.println(group);

    Stage stage = new Stage(new ScreenViewport(), new FakeSpriteBatch(new FakeShaderProgram("abc", "abc")));
    stage.addActor(group);
    stage.act();
    stage.touchDown(50, 50, 1, 1);
  }

}
