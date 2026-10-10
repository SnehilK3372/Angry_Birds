package io.github.some_example_name;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoseScreen extends ScreenAdapter{

    Main game;
    Stage stage;

    private Texture bg;
    private Image bgImage;

    private Texture retry;
    private ImageButton retryButton;//play button

    private Texture back;
    private ImageButton backButton;

    public LoseScreen(Main game){
        this.game=game;
        stage = new Stage(new ScreenViewport());


        bg =new Texture(Gdx.files.internal("Backgrounds/LoseScreen.jpg"));
        TextureRegionDrawable bgDraw = new TextureRegionDrawable(bg);;
        bgImage=new Image(bgDraw);
        bgImage.setPosition(0,0);

        retry =new Texture(Gdx.files.internal("Birds/retryButton.png"));
        TextureRegionDrawable retryDraw = new TextureRegionDrawable(retry);;
        retryButton=new ImageButton(retryDraw);
        retryButton.setPosition(500,12);
        retryButton.addListener(event->{
            if (retryButton.isPressed()) {
                game.setScreen(new LevelOne(game,0));
                return true;
            }
            return false;
        });

        back =new Texture(Gdx.files.internal("Buttons/menuButton.png"));
        TextureRegionDrawable backDraw = new TextureRegionDrawable(back);;
        backButton=new ImageButton(backDraw);
        backButton.setPosition(30,30);

        backButton.addListener(event->{
            if (backButton.isPressed()) {
                game.setScreen(new GameScreen(game,0,0,0));
                return true;
            }
            return false;
        });


    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(stage);
        stage.addActor(bgImage);
        stage.addActor(retryButton);
        stage.addActor(backButton);
    }
    //fillviewport
    @Override
    public void render(float delta){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }
    @Override
    public void resize(int width, int height){

    }
    @Override
    public void pause(){

    }
    @Override
    public void resume(){

    }
    @Override
    public void hide(){

    }
    @Override
    public void dispose(){
        game.image.dispose();
        game.spriteBatch.dispose();
        stage.dispose();
    }



}
