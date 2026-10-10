package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.lwjgl.Sys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//Notes:
/* NOTES:

Coordinate Systems:
Box2D:
    1. Screen: Origin is bottomLeft
    2. Texture: Origin is centre of texture
LibGDX:
    1. Screen: Origin is bottom Left
    2. Texture: Origin is bottom Left
??Input Processor has origin at TopLeft
* here, stages use normal coordinates whereas, spritebatch uses world coordinates(scaled down by ppm)
* all sprites must have locations corrected for ppm

* viewport has scaled down the world by ppm
* spriteBatch is combined with the viewport
*
*
* everything drawn by spriteBatch here must be scaled down by ppm

* -> both menuStage and gameStage are unaffected from viewport here

* -> Box2D uses centre of sprite as origin
* -> libgdx uses bottom left corner as origin
* -> setAsBox as inputs as width/2 and height/2
*
* there is a need to convert from box2D positions to libgdx coordinates
*
* 2 things are being done here,
    a. conversion from box2D coords to screen coords
    b. scale down by ppm(done just for debug renderer)
*
* in this case, the resolution of the ** world ** is now approx. 71x40

birds won't collide with the launcher during launch/flight with the help of mask
* */

public class LevelTwo extends ScreenAdapter implements InputProcessor, Serializable {
    private static final long serialVersionUID = 5L;

    Main game;//do not use stage/spritebatch from previous class
    private final Texture background;
    private Texture pauseTex,playTex;
    private ImageButton pauseButton,playButton;
    private Texture overlayTex;
    private final Stage menuStage;
    private final Stage gameStage;
    private final SpriteBatch spriteBatch;
    private final InputMultiplexer inputMultiplexer;
    private float timeAfterFin=0f;
    private float timeAfterFinBird=0f;

    TextureAtlas blockAtlas=new TextureAtlas(Gdx.files.internal("Blocks/atlases/spritesheet.atlas"));

    private BitmapFont font;
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

    private final World world;
    private boolean isPaused=false;
    private boolean isFlying=false;
    private boolean isDragging;
    int activeBird;
    private Vector2 dragStart;
    private final Vector2 slingshotPosition=new Vector2(11.5f,8.7f);
    private final List<Vector2> trajectoryPoints = new ArrayList<>();
    private float timeAfterLaunch=0.0f;
    private float waitTime=3.0f;
    Body body4;
    private int score;
    private int targetScore;

    private final short groundMask=0x1;//: 0001
    private final short bodyMask=0x2;//: 0010
    private final short launcherMask=0x4;//: 0100

    private Texture scoreCard;
    private Sprite scoreSprite;
    private boolean isComplete=false;
    //box2D calculates "bitwise AND" for categoryBits and maskBits to decide if collision occurs

    Bird Red1;
    Bird Terence1;

    List<Bird>birds;
    List<Pig>pigs;
    List<Block>blocks;


    Box2DDebugRenderer debugRenderer;

    private final OrthographicCamera camera;
    private final float ppm=18f;

    //Sprite sp=new Sprite(new Texture(Gdx.files.internal("Buttons/cloudSave.png")));
    private final Viewport viewport;
    public void groundSetup(){
        Body body1;
        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(880/ppm,0);//scaled down from 1280, **sets centre of the box**

        body1= world.createBody(bodyDef);
        PolygonShape shape=new PolygonShape();
        shape.setAsBox(170/ppm,80/ppm);// **sets dimensions of the box**
        createFixture(body1, shape);
        body1.getFixtureList().get(0).setUserData("ground");


        Body body2;
        BodyDef bodyDef2 =new BodyDef();
        bodyDef2.type= BodyDef.BodyType.StaticBody;
        bodyDef2.position.set(0,0);//scaled down from 1280, **sets centre of the box**

        body2= world.createBody(bodyDef2);
        PolygonShape shape2 =new PolygonShape();
        shape2.setAsBox(11,2);// **sets dimensions of the box** in the scaled world
        createFixture(body2, shape2);
        body2.getFixtureList().get(0).setUserData("ground");



        Body body3;
        BodyDef bodyDef3 =new BodyDef();
        bodyDef3.type= BodyDef.BodyType.StaticBody;
        bodyDef3.position.set(180/ppm,0);//scaled down from 1280, **sets centre of the box**
        body3 = world.createBody(bodyDef3);

        PolygonShape shape3 =new PolygonShape();
        shape3.setAsBox(60/ppm,20/ppm);// **sets dimensions of the box** in the scaled world
        createFixture(body3, shape3);
        body3.getFixtureList().get(0).setUserData("ground");

        BodyDef bodyDef4 =new BodyDef();
        bodyDef4.type= BodyDef.BodyType.StaticBody;
        bodyDef4.position.set(11.5f,5);//scaled down from 1280, **sets centre of the box**

        body4 = world.createBody(bodyDef4);

        PolygonShape shape4 =new PolygonShape();
        shape4.setAsBox(1,2);// **sets dimensions of the box** in the scaled world
        FixtureDef fixtureDef4 =new FixtureDef();
        fixtureDef4.shape= shape4;
        fixtureDef4.density=1f;
        fixtureDef4.friction=100f;
        fixtureDef4.restitution=0f;
        fixtureDef4.filter.categoryBits=groundMask;
        fixtureDef4.filter.maskBits=groundMask|bodyMask;
        body4.createFixture(fixtureDef4);
        body1.getFixtureList().get(0).setUserData("ground");
        shape4.dispose();


    }

    private void createFixture(Body body4, PolygonShape shape4) {
        FixtureDef fixtureDef4 =new FixtureDef();
        fixtureDef4.shape= shape4;
        fixtureDef4.density=1f;
        fixtureDef4.friction=1f;
        fixtureDef4.restitution=0f;
        fixtureDef4.filter.categoryBits=groundMask;
        fixtureDef4.filter.maskBits=groundMask|bodyMask;
        body4.createFixture(fixtureDef4);
        shape4.dispose();
    }
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
                game.setScreen(new LevelTwo(game,0));
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
    public void unpackBlocks(){
        TextureRegion RectStone = blockAtlas.findRegion("RectStone");
        TextureRegion rectGlass = blockAtlas.findRegion("rectGlass");
        TextureRegion rectWood = blockAtlas.findRegion("rectWood");
        TextureRegion smallRectGlass = blockAtlas.findRegion("smallRectGlass");
        TextureRegion smallRectStone = blockAtlas.findRegion("smallRectStone");
        TextureRegion smallRectWood = blockAtlas.findRegion("smallRectWood");

        TextureRegion smallerRectGlass = blockAtlas.findRegion("smallerRectGlass");
        TextureRegion smallerRectWood = blockAtlas.findRegion("smallerRectWood");
        TextureRegion squareGlass = blockAtlas.findRegion("squareGlass");
        TextureRegion squareStone = blockAtlas.findRegion("squareStone");
        TextureRegion squareWood = blockAtlas.findRegion("squareWood");


        Block blockDown=new BlockStone(world,RectStone,new Vector2(42,6.8f),"RectStone",false);
        Block block2=new BlockStone(world,smallRectStone,new Vector2(51.5f,6.8f),"smallRectStone",true);
        Block block8=new BlockStone(world,squareStone,new Vector2(43,12.6f),"squareStone",false);
        Block block9=new BlockStone(world,squareStone,new Vector2(43,14.6f),"squareStone",false);

        Block block3=new BlockGlass(world,squareGlass,new Vector2(42,7),"squareGlass",false);
        Block block4=new BlockGlass(world,squareGlass,new Vector2(42,8),"squareGlass",false);
        //  Block block5=new BlockGlass(world,squareGlass,new Vector2(42,9),"squareGlass",false);


        Block block5=new BlockStone(world,RectStone,new Vector2(43,11),"RectStone",false);


        Block block6=new BlockWood(world,squareWood,new Vector2(51.25f,8),"squareWood",false);
        Block block7=new BlockWood(world,squareWood,new Vector2(51.25f,9),"squareWood",false);
        // Block block13=new BlockWood(world,squareWood,new Vector2(51.25f,10),"squareWood",false);
        blocks.add(blockDown);
        blocks.add(block2);
        blocks.add(block3);
        blocks.add(block4);
        blocks.add(block5);
        blocks.add(block6);
        blocks.add(block7);
        blocks.add(block8);
        blocks.add(block9);


    }

    LevelTwo(Main game, int score){
        this.game=game;
        this.score=score;
        scoreCard=new Texture(Gdx.files.internal("Font/score.png"));
        scoreSprite=new Sprite(scoreCard);
        inputMultiplexer=new InputMultiplexer();
        activeBird=0;
        dragStart=new Vector2();
        background=new Texture(Gdx.files.internal("Backgrounds/bg2.jpg"));
        spriteBatch=new SpriteBatch();

        debugRenderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera();
        viewport = new FitViewport(1280/ppm, 720/ppm, camera);
        viewport.apply();
        camera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);
        camera.update();

        menuStage = new Stage();
        gameStage = new Stage();




        world=new World(new Vector2(0,-20f),true);

        debugRenderer = new Box2DDebugRenderer();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/Fof/FeastOfFlesh.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size =256;
        parameter.color = Color.WHITE;

        font= generator.generateFont(parameter);
        generator.dispose();

        birds=new ArrayList<>();
        pigs=new ArrayList<>();
        blocks=new ArrayList<>();

    }
    @Override
    public void show(){
        groundSetup();
        menuSetup();
        unpackBlocks();

       // Red1=new BirdRed(world,new Vector2(10f,7.1f),1);

      //  Terence1=new BirdTerence(world,new Vector2(7,1.3f),1);

      //  birds.add(Red1);
        birds.add(new BirdBomb(world,new Vector2(10f,7.1f),1));
      // birds.add(Terence1);

        birds.add(new BirdBubbles(world,new Vector2(2,5),0));
        birds.add(new BirdChuck(world,new Vector2(4,5),0));

        pigs.add(new PigKing(world,new Vector2(45,6)));
        pigs.add(new PigChef(world,new Vector2(55,8)));
        pigs.add(new PigSpace(world,new Vector2(46,12.8f)));
        pigs.add(new PigSpace(world,new Vector2(49,12.8f)));

        isDragging=false;

        gameStage.addActor(pauseButton);

        menuStage.addActor(overlay);
        menuStage.addActor(exitButton);
        menuStage.addActor(playButton);
        menuStage.addActor(restartButton);
        menuStage.addActor(saveGameButton);
        menuStage.addActor(poster);

        inputMultiplexer.addProcessor(gameStage);
        inputMultiplexer.addProcessor(this);
        CollisionListener listener=new CollisionListener();
        world.setContactListener(listener);

        Gdx.input.setInputProcessor(inputMultiplexer);
    }
    private void togglePause(){//overlay behaviour
        overlay.setVisible(isPaused);
        if (isPaused) {
            inputMultiplexer.addProcessor(menuStage);
            inputMultiplexer.removeProcessor(gameStage);
            inputMultiplexer.removeProcessor(this);
        } else {
            inputMultiplexer.addProcessor(gameStage);
            inputMultiplexer.addProcessor(this);
            inputMultiplexer.removeProcessor(menuStage);
        }
    }
    private void animateNextBird(){//only called after bird is flying
        if (activeBird + 1 >= birds.size()) return;
        if(timeAfterLaunch>=1.0f){
            body4.getFixtureList().get(0).getFilterData().maskBits=groundMask;
        }
        if(waitTime<=timeAfterLaunch){
            Body nextBird = birds.get(activeBird + 1).getBody();
            Vector2 target = new Vector2(11.3f,8f);
            //Vector2 target = slingshotPosition;

            float speed = 0.1f;
            Vector2 current1 = nextBird.getPosition();
            Vector2 movementUp = new Vector2(0, speed);
            nextBird.setTransform(current1.add(movementUp), -30);

            nextBird.setTransform(current1.add(movementUp), 0);


            Vector2 current = nextBird.getPosition();
            Vector2 movement = target.sub(current).scl(speed);

            nextBird.setTransform(current.add(movement), 0);

            if (current.dst2(new Vector2(11.5f,8f)) <=2f) {
                nextBird.setLinearVelocity(0,0);
                nextBird.setAngularVelocity(0f);
                activeBird++;
                isFlying = false;
                timeAfterLaunch=0;
            }

        }
        else{
            timeAfterLaunch+=Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void render(float delta){

        if (!isPaused) {
            world.step(1/100f, 8,3);
            int birdCount=0;
            for(Bird bird:birds){
                if(!bird.isDestroyed()){
                    bird.update(world,delta);
                }
                if(!bird.isDestroyed()){
                    bird.getSprite().setPosition(bird.getBody().getPosition().x*ppm-bird.getSprite().getWidth()/2,bird.getBody().getPosition().y*ppm-bird.getSprite().getHeight()/2);
                    birdCount++;
                }
            }
            for(Block block: blocks){
                if(!block.isDestroyed()){
                    block.update(world);
                }
                if(block.isDestroyed()&&!block.isPointTaken()){
                    score+=1000;
                    block.takePoint();
                }
                if(!block.isDestroyed()){
                    block.getSprite().setPosition(block.getBody().getPosition().x*ppm-block.getSprite().getWidth()/2,block.getBody().getPosition().y*ppm-block.getSprite().getHeight()/2);
                }
            }
            int pigCount=0;
            for(Pig pig: pigs){
                if(!pig.isDestroyed()){
                    pig.update(world);
                }
                if(pig.isDestroyed()&&!pig.isPointTaken()){
                    score+=2000;
                    pig.takePoint();
                }
                if (!pig.isDestroyed()) {
                    pig.getSprite().setPosition(pig.getBody().getPosition().x*ppm-pig.getSprite().getWidth()/2,pig.getBody().getPosition().y*ppm-pig.getSprite().getHeight()/2);
                    pigCount++;
                }

            }

            if(birdCount==0){
                game.setScreen(new LoseScreen(game));
            }
            if(pigCount==0){
                if (timeAfterFin>=3) {
                    isComplete=true;
                    game.setScreen(new TitleScreen(game));
                }
                else{
                    timeAfterFin+=delta;
                }
            }
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        spriteBatch.draw(background,0,0,1280/ppm,720/ppm);
     //   spriteBatch.draw(slingshotSprite,90/ppm,180/ppm,slingshotSprite.getWidth()/ppm,slingshotSprite.getHeight()/ppm);

        font.getData().setScale(0.01f);
        // spriteBatch.draw(scoreSprite,45,35,scoreSprite.getWidth()/ppm,scoreSprite.getHeight()/ppm);
        font.draw(spriteBatch, "ScoRe : "+score, 56, 34);


        for(Block block: blocks){
            if(!block.isDestroyed()){
                spriteBatch.draw(block.getSprite(),block.getSprite().getX()/ppm,block.getSprite().getY()/ppm,block.getSprite().getWidth()/ppm,block.getSprite().getHeight()/ppm);
            }
        }
        for(Bird bird:birds){
            if(!bird.isDestroyed()){
                spriteBatch.draw(bird.getSprite(),bird.getSprite().getX()/ppm,bird.getSprite().getY()/ppm,bird.getSprite().getWidth()/ppm,bird.getSprite().getHeight()/ppm);
            }
        }
        for(Pig pig: pigs){
            if(!pig.isDestroyed()){
                spriteBatch.draw(pig.getSprite(),pig.getSprite().getX()/ppm,pig.getSprite().getY()/ppm,pig.getSprite().getWidth()/ppm,pig.getSprite().getHeight()/ppm);
            }
        }

        spriteBatch.end();
        debugRenderer.render(world, camera.combined);

        if(!isPaused){
            gameStage.act(delta);
            gameStage.draw();
        }
        else{
            menuStage.act(delta);
            menuStage.draw();
        }
        if (isFlying) {
            animateNextBird();
        }
    }
    @Override
    public void dispose(){
        world.dispose();
        background.dispose();
        spriteBatch.dispose();
        menuStage.dispose();
        gameStage.dispose();
    }
    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        camera.update();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        if (isFlying || (activeBird > birds.size())){
            return true;
        }
        //slingshot won't collide with bird
        body4.getFixtureList().get(0).getFilterData().maskBits=launcherMask;

        Vector2 touch = new Vector2(screenX/ppm, (Gdx.graphics.getHeight())/ppm - screenY/ppm);;
        Body activeBirdBody = birds.get(activeBird).getBody();
        Gdx.app.log("Debug", "Touch position: " + touch);

        if (activeBirdBody.getFixtureList().get(0).testPoint(touch)) {
            isDragging = true;
            dragStart.set(touch);
            //  Gdx.app.log("Debug", "Bird selected for dragging.");
        }
        return true;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging) {
            Vector2 touch = new Vector2(screenX/ppm, (Gdx.graphics.getHeight())/ppm - screenY/ppm);
            float maxDragDistance = 8f; // Example max distance
            Vector2 slingshotWorld = new Vector2(11.5f,6.8f+birds.get(activeBird).getSprite().getHeight()/ppm/2);// Slingshot position in world

            if (touch.dst(slingshotWorld) > maxDragDistance) {
                touch = slingshotWorld.cpy().add(touch.sub(slingshotWorld).nor().scl(maxDragDistance));
            }
            Gdx.app.log("Debug", "Dragging bird to position: " + touch);


            birds.get(activeBird).getBody().setTransform(touch, 0);
        }
        return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isDragging) {
            isDragging = false;
            isFlying = true;

            Vector2 dragEnd = new Vector2(screenX/ppm, (Gdx.graphics.getHeight())/ppm - screenY/ppm);
            Vector2 diff=dragStart.sub(dragEnd);
            Vector2 impulse = diff.scl(60f); // Scale for speed adjustment

            birds.get(activeBird).getBody().applyLinearImpulse(impulse, birds.get(activeBird).getBody().getWorldCenter(), true);
            birds.get(activeBird).launch();
        }
        return true;
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }


    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

}
