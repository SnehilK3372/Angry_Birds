package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Level extends ScreenAdapter {
    private Main game;
    private Texture background;
    private Texture pauseTex, playTex;
    private ImageButton pauseButton, playButton;
    private Stage menuStage;
    private Stage gameStage;
    private SpriteBatch spriteBatch;
    private InputMultiplexer inputMultiplexer;

    private Table overlay;
    private boolean isPaused = false;


    Level(Main game) {
        this.game = game;
        inputMultiplexer = new InputMultiplexer();

        background = new Texture(Gdx.files.internal("Levels/Level1background.jpg"));
        spriteBatch = new SpriteBatch();

        menuStage = new Stage(new ScreenViewport());
        gameStage = new Stage(new ScreenViewport());

        // Overlay for the pause, win, and lose screens
        overlay = new Table();
        overlay.setFillParent(true);
        overlay.setColor(0, 0, 0, 0.5f);
        overlay.setVisible(false); // Initially hidden
    }

    @Override
    public void show() {
        // Pause button setup
        pauseTex = new Texture(Gdx.files.internal("Buttons/pauseButton.png"));
        TextureRegionDrawable pauseDraw = new TextureRegionDrawable(pauseTex);
        pauseButton = new ImageButton(pauseDraw);
        pauseButton.setPosition(15, Gdx.graphics.getHeight() - pauseTex.getHeight() - 10);

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = true;
                togglePause();
            }
        });

        // Play button setup
        playTex = new Texture(Gdx.files.internal("Buttons/playGame2.png")); // Ensure the correct texture is used
        TextureRegionDrawable playDraw = new TextureRegionDrawable(playTex);
        playButton = new ImageButton(playDraw);
        playButton.setPosition(15, Gdx.graphics.getHeight() - playTex.getHeight() - 10);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = false;
                togglePause();
            }
        });

        gameStage.addActor(pauseButton);
        menuStage.addActor(playButton);
        menuStage.addActor(overlay);

        inputMultiplexer.addProcessor(gameStage);
        inputMultiplexer.addProcessor(menuStage); // Ensure menuStage is also part of the input
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void togglePause() {
        overlay.setVisible(isPaused); // Show overlay if paused
        if (isPaused) {
            Gdx.input.setInputProcessor(menuStage); // Set input to menu stage when paused
        } else {
            Gdx.input.setInputProcessor(gameStage); // Return to game stage input when unpaused
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background,0,0);
        spriteBatch.end();

        if(!isPaused){
            gameStage.act(delta);
            gameStage.draw();

        }
        else{
            menuStage.act(delta);
            menuStage.draw();
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        pauseTex.dispose();
        playTex.dispose();
        spriteBatch.dispose();
        gameStage.dispose();
        menuStage.dispose();
    }
}
