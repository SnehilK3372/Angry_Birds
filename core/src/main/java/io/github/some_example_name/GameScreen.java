package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.Serializable;

public class GameScreen extends ScreenAdapter implements Serializable {
    private static final long serialVersionUID = 3L;
    Main game;
    //2 stages created 1 for scrolling other of menu options
    Stage staticStage;
    Stage scrollStage;
    Stage menuStage;

    private Texture backgroundTex;
    private Texture settings;
    private ImageButton settingsButton;

    private Texture menuButton;
    private ImageButton menuImageButton;

    private Texture Level1;
    private ImageButton Level1Button;

    private Texture Level2;
    private ImageButton Level2Button;

    private Texture Level3;
    private ImageButton Level3Button;

    private Texture trophy1;
    private Image trophy11;
    private Texture trophy2;
    private Image trophy22;
    private Texture trophy3;
    private Image trophy33;

    private OrthographicCamera camera;
    private float levelWidth;
    private float velocityX = 0f; // Track drag velocity
    private final float damping = 0.9f;

    private InputMultiplexer inputMultiplexer;
    private static int score1=0;
    private static int score2=0;
    private static int score3=0;



    GameScreen(Main game,int score1,int score2, int score3){
        this.game=game;
        if(score1!=0){
            GameScreen.score1 =score1;
        }
        if(score2!=0){
            GameScreen.score1 =score1;
        }
        if(score3!=0){
            GameScreen.score1 =score1;
        }
        trophy1=new Texture(Gdx.files.internal("Birds/trophy1.png"));
        TextureRegionDrawable trophyDraw=new TextureRegionDrawable(trophy1);
        trophy11=new Image(trophyDraw);
        trophy11.setPosition(880,430);


        TextureRegionDrawable trophyDraw2=new TextureRegionDrawable(trophy1);
        trophy22=new Image(trophyDraw2);

        TextureRegionDrawable trophyDraw3=new TextureRegionDrawable(trophy1);
        trophy33=new Image(trophyDraw3);

        camera= new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth/ 2,camera.viewportHeight/2,0);// set cam position

        scrollStage=new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera));
        staticStage=new Stage(new ScreenViewport());


        settings=new Texture(Gdx.files.internal("Buttons/Settings.png"));
        TextureRegionDrawable settingsDraw=new TextureRegionDrawable(settings);
        settingsButton=new ImageButton(settingsDraw);
        settingsButton.setPosition(15,10);



        menuButton=new Texture(Gdx.files.internal("Buttons/menuButton.png"));
        TextureRegionDrawable menuDraw=new TextureRegionDrawable(menuButton);
        menuImageButton=new ImageButton(menuDraw);
        menuImageButton.setPosition(15,Gdx.graphics.getHeight()-menuButton.getHeight()-10);
        menuImageButton.addListener(event->{
           if(menuImageButton.isPressed()){
               //toggleMenu();
               return true;
           }
           return false;
        });


        backgroundTex = new Texture(Gdx.files.internal("Backgrounds/mount_wide1.jpg"));//image edited to fit
        levelWidth=backgroundTex.getWidth();



        Level1=new Texture(Gdx.files.internal("Levels/Level1_new_new.png"));
        TextureRegionDrawable Level1Draw=new TextureRegionDrawable(Level1);
        Level1Button=new ImageButton(Level1Draw);
        Level1Button.setPosition(Level1Button.getWidth()/2 -180,(Gdx.graphics.getHeight()-Level1Button.getHeight())/2f+1);

        Level1Button.addListener(event -> {
            if(Level1Button.isPressed()){
                this.game.setScreen(new LevelOne(game,0));
                return true;
            }
            return false;
        });

        Level2 = new Texture(Gdx.files.internal("Levels/Level_2.png"));
        TextureRegionDrawable Level2Draw=new TextureRegionDrawable(Level2);
        Level2Button=new ImageButton(Level2Draw);
        Level2Button.setPosition(1135,(Gdx.graphics.getHeight()-Level1Button.getHeight())/2f-10);

        Level2Button.addListener(event->{
            if(Level2Button.isPressed()){//not set yet
                game.setScreen(new LevelTwo(game,score2));
                return true;
            }
            return false;
        });

        Level3 =new Texture(Gdx.files.internal("Levels/Level_3_new_new.png"));
        TextureRegionDrawable Level3Draw=new TextureRegionDrawable(Level3);
        Level3Button=new ImageButton(Level3Draw);
        Level3Button.setPosition(2000,(Gdx.graphics.getHeight()-Level1Button.getHeight())/2f);

        Level3Button.addListener(event->{
            if(Level3Button.isPressed()){
                game.setScreen(new LevelThree(game));
                return true;
            }
            return false;
        });


    }
    @Override
    public void show(){

        inputMultiplexer=new InputMultiplexer();
        inputMultiplexer.addProcessor(scrollStage);
        inputMultiplexer.addProcessor(staticStage);
        inputMultiplexer.addProcessor(new InputAdapter(){
           //private int lastX=-1; // static scroll
           @Override

           public boolean touchDragged(int ScreenX, int ScreenY, int pointer){
               //static scroll
//               if(lastX!=-1){
//                   int deltaX=ScreenX-lastX;
//                   camera.position.x-=deltaX;
//               }
//               lastX=ScreenX;
               velocityX = -Gdx.input.getDeltaX() * 60f;

               return true;

           }
           @Override
            public boolean touchUp(int x, int y, int pointer,int button){//behaviour when mouse releases
               velocityX*=damping;
               //lastX=-1;
               return true;
           }
        });

        Gdx.input.setInputProcessor(inputMultiplexer);//input multiplexer has input from 3 sources: 2 stages and 1 for scroll

        scrollStage.addActor(Level1Button);
        scrollStage.addActor(Level2Button);
        scrollStage.addActor(Level3Button);
        scrollStage.addActor(trophy11);



        staticStage.addActor(settingsButton);
        staticStage.addActor(menuImageButton);

    }
    @Override
    public void render(float delta){

        if(score1!=0){

        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.x += velocityX * delta;
        velocityX *= damping;

        camera.position.x = Math.max(camera.viewportWidth / 2, Math.min(levelWidth - camera.viewportWidth / 2, camera.position.x));
        game.spriteBatch.setProjectionMatrix(camera.combined);
        camera.update();

        game.spriteBatch.begin();//sprite batch for scrollable element
        game.spriteBatch.draw(backgroundTex,0,0);
        game.spriteBatch.end();

        scrollStage.act(delta);
        scrollStage.draw();

        staticStage.act(delta);
        staticStage.draw();

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
        game.spriteBatch.dispose();
        backgroundTex.dispose();
        menuStage.dispose();
        staticStage.dispose();
    }

}
