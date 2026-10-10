package io.github.some_example_name;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class WinScreen extends ScreenAdapter{

    Main game;
    Stage stage;
    private Texture logoPhoto;
    private Texture logoName;
    private ImageButton playGame;//play button
    private Texture playTexture;//texture for play button

    public WinScreen(Main game, int score){
        this.game=game;
        stage = new Stage(new ScreenViewport());
        logoPhoto =new Texture(Gdx.files.internal("Logo/logo-Photoroom2.png"));
        logoName = new Texture(Gdx.files.internal("Buttons/ABLogo1.png"));
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(stage);
        playTexture=new Texture(Gdx.files.internal("Buttons/playGame1.png"));
        TextureRegionDrawable playDraw = new TextureRegionDrawable(playTexture);
        playGame= new ImageButton(playDraw);

        playGame.setPosition(575, 25);
        //playGame.setPosition(600, 50+10);

        playGame.addListener(event->{
            if (playGame.isPressed()) {
                // game.setScreen(new GameScreen(game,0,0,0));
                return true;
            }
            return false;
        });

        stage.addActor(playGame);

    }
    //fillviewport
    @Override
    public void render(float delta){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float x1 = (Gdx.graphics.getWidth() - logoPhoto.getWidth())/ 2f;
        float y1 = (Gdx.graphics.getHeight() - logoPhoto.getHeight()) / 2f;
        float x2=(Gdx.graphics.getWidth()-logoName.getWidth())/2f;

        game.spriteBatch.begin();



        game.spriteBatch.draw(game.image,0,0,1280,720);//specific to this size only, will have to change size in lwjgl3Launcher too
        game.spriteBatch.draw(logoPhoto,x1+20,y1+40);//specific to this size only, will have to change size in lwjgl3Launcher too
        //  game.spriteBatch.draw(logoName,x2, y1+100);
        game.spriteBatch.draw(logoName,x2, y1+240);
        game.spriteBatch.end();


        //game.spriteBatch.begin();


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
