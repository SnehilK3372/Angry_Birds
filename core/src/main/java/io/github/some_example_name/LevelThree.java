package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

final class LevelThree extends ScreenAdapter {
    Main game;//do not use stage/spritebatch from previous class
    private Texture background;
    private Texture pauseTex,playTex;
    private ImageButton pauseButton,playButton;
    private Texture overlayTex;
    private Stage menuStage;
    private Stage gameStage;
    private SpriteBatch spriteBatch;
    private InputMultiplexer inputMultiplexer;

    private BitmapFont abFont;
    private Skin skin;
    private Label pauseTitle;

    //private Table overlay;
    private Image overlay;
    private Image levelPaused;
    private Texture levelPause;

    private Texture slingshot;
    private Sprite slingshotSprite;

    private Texture restart;
    private ImageButton restartButton;

    private Texture exit;
    private ImageButton exitButton;

    private Texture saveGame;
    private ImageButton saveGameButton;

    private Image poster;

    private World world;
    private boolean isPaused=false;

    List<Bird> birdList=new ArrayList<>();
    List<Pig> pigList=new ArrayList<>();
    List<Block> blockList=new ArrayList<>();

    Bird Red1;
    Bird Terence1;

    Pig pig1;


    Block Block1;
    Block Block2;
    Block Block3;
    Block Block4;
    Box2DDebugRenderer debugRenderer;

    Sprite blk1;
    Sprite blk2;
    private OrthographicCamera camera;


    public void menuSetup(){

        //1. change in here + 2. Add actor to menuStage in show()
        poster=new Image(new Texture(Gdx.files.internal("Backgrounds/poster.png")));
        poster.setPosition(250,125);

        restart=new Texture(Gdx.files.internal("Buttons/restart.png"));
        TextureRegionDrawable restartDraw=new TextureRegionDrawable(restart);
        restartButton=new ImageButton(restartDraw);
        restartButton.setPosition(15,Gdx.graphics.getHeight()-restart.getHeight()-110);

        restartButton.addListener(event->{
            if(restartButton.isPressed()){
                return true;
            }
            return false;
        });


        saveGame=new Texture(Gdx.files.internal("Buttons/cloudSave.png"));
        TextureRegionDrawable saveGameDraw=new TextureRegionDrawable(saveGame);
        saveGameButton=new ImageButton(saveGameDraw);
        saveGameButton.setPosition(15,restartButton.getY()-110);

        exit=new Texture(Gdx.files.internal("Buttons/exit.png"));
        TextureRegionDrawable reloadDraw=new TextureRegionDrawable(exit);
        exitButton =new ImageButton(reloadDraw);
        exitButton.setPosition(15, saveGameButton.getY()-110);

        exitButton.addListener(event->{
            if(exitButton.isPressed()){
                game.setScreen(new GameScreen(game,0,0,0));
                return true;
            }
            return false;
        });




        levelPause=new Texture(Gdx.files.internal("Buttons/Level1Pause.png"));
        levelPaused=new Image(levelPause);
        levelPaused.setPosition(400,600);

        menuStage.addActor(levelPaused);


        //Overlay
        overlayTex=new Texture(Gdx.files.internal("Buttons/Overlay.png"));
        overlay = new Image(overlayTex);
        overlay.setVisible(false);

        //pause button
        pauseTex =new Texture(Gdx.files.internal("Buttons/pauseButton.png"));
        TextureRegionDrawable pauseDraw=new TextureRegionDrawable(pauseTex);
        pauseButton=new ImageButton(pauseDraw);
        pauseButton.setPosition(15,Gdx.graphics.getHeight()-pauseTex.getHeight()-10 );

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("meow");
                isPaused = true;
                togglePause();
            }
        });

        //play button
        playTex =new Texture(Gdx.files.internal("Buttons/playGame2.png"));
        TextureRegionDrawable playDraw=new TextureRegionDrawable(playTex);
        playButton=new ImageButton(playDraw);
        playButton.setPosition(15,Gdx.graphics.getHeight()-playTex.getHeight()-10 );
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = false;
                togglePause();
            }
        });
    }

    LevelThree(Main game){
        this.game=game;
        inputMultiplexer=new InputMultiplexer();

        background=new Texture(Gdx.files.internal("Levels/Level1background.jpg"));
        spriteBatch=new SpriteBatch();

        menuStage = new Stage(new ScreenViewport());
        gameStage = new Stage(new ScreenViewport());

        slingshot= new Texture(Gdx.files.internal("Birds/Mythic_Slingshot.png"));
        slingshotSprite= new Sprite(slingshot);

        world=new World(new Vector2(0,-9.8f),true);
        debugRenderer = new Box2DDebugRenderer();
        // camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        //gameStage.add(slingshotSprite);


        //overlay for the Pause, win and lose screens
//        overlay = new Table();
//        overlay.setFillParent(true);
//        overlay.setColor(0, 0, 0, 0.5f);
//        overlay.setVisible(false);//only true when menu is clicked
    }
    @Override
    public void show(){

        menuSetup();

        Red1=new BirdRed(world,new Vector2(103,245),1);
        Terence1=new BirdTerence(world,new Vector2(10,199),2);
        birdList.add(Red1);
        birdList.add(Terence1);

        //pig1=new SmallPig();

//        Block1= new BlockWood();
//        Block2= new BlockWood();
//        Block3= new BlockWood(new Texture(Gdx.files.internal("Blocks/Wood_hor.png")),500,50);
//        Block4= new BlockWood(new Texture(Gdx.files.internal("Blocks/Wood_hor.png")),500,50);


        gameStage.addActor(pauseButton);

        //Button setup for menu
        menuStage.addActor(overlay);
        menuStage.addActor(exitButton);
        menuStage.addActor(playButton);
        menuStage.addActor(restartButton);
        menuStage.addActor(saveGameButton);
        menuStage.addActor(poster);



        inputMultiplexer.addProcessor(gameStage);
        //  inputMultiplexer.addProcessor();

        Gdx.input.setInputProcessor(inputMultiplexer);

    }
    private void togglePause(){//overlay behaviour
        overlay.setVisible(isPaused);
        if (isPaused) {
            inputMultiplexer.addProcessor(menuStage);
            inputMultiplexer.removeProcessor(gameStage);
        } else {
            inputMultiplexer.addProcessor(gameStage);
            inputMultiplexer.removeProcessor(menuStage);
        }
    }

    @Override
    public void render(float delta){
        world.step(1/60f, 6,2);



        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background,0,0);
        spriteBatch.draw(slingshotSprite,95,180);
        spriteBatch.draw(Red1.getSprite(),103,245);
        spriteBatch.draw(Terence1.getSprite(),10,199);
        spriteBatch.draw(Block1.getSprite(),825,125);
        spriteBatch.draw(Block2.getSprite(),820+Block3.getSprite().getWidth(),125);
        spriteBatch.draw(Block3.getSprite(),825,125);
        spriteBatch.draw(Block4.getSprite(),825,125+Block1.getSprite().getHeight()-10);
        spriteBatch.draw(pig1.getSprite(),840,130);


        spriteBatch.end();

        if(!isPaused){
            gameStage.act(delta);
            gameStage.draw();

        }
        else{
            menuStage.act(delta);
            menuStage.draw();
        }


        //debugRenderer.render(world, camera.combined);
    }
    @Override
    public void dispose(){
        world.dispose();
        background.dispose();
        spriteBatch.dispose();
    }
}
