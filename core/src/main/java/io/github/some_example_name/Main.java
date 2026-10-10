package io.github.some_example_name;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch spriteBatch;
    public Texture image;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        image = new Texture(Gdx.files.internal("Backgrounds/bg6.jpg"));
        setScreen(new TitleScreen(this));
    }
    @Override
    public void dispose() {
        spriteBatch.dispose();
        image.dispose();
    }
    //@Override
//    public void render() {
//        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//        spriteBatch.begin();
//        spriteBatch.draw(image, 140, 210);
//        spriteBatch.end();
//    }

//    public void resize(){
//        spriteBatch.begin();
//    }

}
