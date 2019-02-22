package com.soze.r2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class DefaultCursor {

  private static final Drawable CURSOR;

  static {
    Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
    Label oneCharSizeCalibrationThrowAway = new Label("|", labelStyle);

    Pixmap cursorPixmap = new Pixmap(
      (int) oneCharSizeCalibrationThrowAway.getWidth(),
      (int) oneCharSizeCalibrationThrowAway.getHeight(),
      Pixmap.Format.RGB888
    );
    cursorPixmap.setColor(Color.GRAY);
    cursorPixmap.fill();

    CURSOR = new Image(new Texture(cursorPixmap)).getDrawable();
  }

  public static Drawable getCursor() {
    return CURSOR;
  }

}
