package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;

public abstract class Block implements Serializable {
    private static final long serialVersionUID = 2L;
    private float health;
    private int maxHealth;

    private Vector2 location;
    private TextureRegion texture;
    private Sprite sprite;
    private Body body;
    private final float ppm=18f;
    private TextureRegion t1;
    private TextureRegion t2;
    private TextureRegion t3;
    private TextureRegion t4;
    private boolean isDestroyed=false;
    private boolean pointTaken =false;

    public void assignNormalTextures(TextureRegion[][] subRegions){//for texture regions with stacking
        t1=subRegions[0][0];
        t2=subRegions[1][0];
        t3=subRegions[2][0];
        t4=subRegions[3][0];
    }

    public void parseRegions(TextureRegion textureRegion,String region){
        switch (region) {
            case "RectStone": {
                TextureRegion[][] subRegions = textureRegion.split(176, 19);
                assignNormalTextures(subRegions);
                break;
            }
            case "smallRectStone": {
                TextureRegion[][] subRegions = textureRegion.split(146, 19);
                assignNormalTextures(subRegions);
                break;
            }
            case "squareStone":
            case "squareWood": {
                TextureRegion[][] subRegions = textureRegion.split(36, 35);
                assignNormalTextures(subRegions);
                break;
            }
            case "rectGlass": {
                TextureRegion[][] subRegions = textureRegion.split(180, 19);
                t1 = subRegions[2][0];
                t2 = subRegions[1][0];
                t3 = subRegions[0][0];
                t4 = subRegions[3][0];
                break;
            }
            case "smallRectGlass":{
                TextureRegion[][] subRegions = textureRegion.split(151, 19);
                assignNormalTextures(subRegions);
                break;
            }
            case "smallerRectGlass":{
                TextureRegion[][] subRegions = textureRegion.split(46, 25);
                assignNormalTextures(subRegions);
                break;
            }
            case "squareGlass":{
                TextureRegion[][] subRegions = textureRegion.split(35, 35);
                assignNormalTextures(subRegions);
                break;
            }
            case "rectWood":{
                TextureRegion[][] subRegions = textureRegion.split(177, 19);
                assignNormalTextures(subRegions);
                break;
            }
            case "smallRectWood":{
                TextureRegion[][] subRegions = textureRegion.split(49, 25);
                assignNormalTextures(subRegions);
                break;
            }
            case "smallerRectWood":{
                TextureRegion[][] subRegions = textureRegion.split(72, 19);
                t1 = subRegions[0][0];
                t2 = subRegions[1][0];
                t3 = subRegions[0][1];
                t4 = subRegions[1][1];
                break;
            }


        }


    }
    public Block(World world, TextureRegion texture, Vector2 position,int weight,String region,boolean rotate,int health){//position is already set for ppm
        health*=6;
        this.location=position;
        this.health=health*4;
        this.maxHealth=health*4;
        parseRegions(texture,region);
        this.texture=t1;
        Sprite block=new Sprite(t1);


        block.setPosition(position.x, position.y);
        this.sprite=block;

        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((position.x+ sprite.getWidth()/2/ppm),(position.y+ sprite.getHeight()/2/ppm));



        body= world.createBody(bodyDef);

        PolygonShape shape=new PolygonShape();
        shape.setAsBox(block.getWidth()/ppm/2,block.getHeight()/ppm/2);


        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=shape;
        fixtureDef.density= weight;
        fixtureDef.friction=1f;
        fixtureDef.restitution=0f;
        body.createFixture(fixtureDef);
        body.setAngularDamping(5f);
        body.getFixtureList().get(0).setUserData(this);

        shape.dispose();

    }
    public Sprite getSprite(){
        return sprite;
    }
    public void getDamage(float damage){
        health-=damage;
        if(health<maxHealth*0.75f){
            this.sprite= new Sprite(t2);
        }
        else if(health<maxHealth*0.5f){
            this.sprite= new Sprite(t3);
        }
        else if(health<maxHealth*0.25f){
            this.sprite= new Sprite(t4);
        }
    }

    public boolean isPointTaken() {
        return pointTaken;
    }
    public void takePoint(){
        pointTaken=true;
    }


    public void update(World world){
        boolean pos=(sprite.getX()<=1280)&&(0<=sprite.getX())&&(sprite.getY()<=720)&&(0<=sprite.getY());
        if(health<=0 || !pos){
            world.destroyBody(body);
            isDestroyed=true;
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public Body getBody(){
        return body;
    }
}
