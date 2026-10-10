package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.lwjgl.Sys;

import java.io.Serializable;

public abstract class Pig implements Serializable {
    private static final long serialVersionUID = 9L;
    private float health;
    private int maxHealth;



    transient private TextureRegion t1;
    transient private TextureRegion t2;
    transient private TextureRegion t3;
    transient private Sprite sprite;
    transient private Body body;
    private boolean isDestroyed=false;
    private final float ppm=18f;
    private boolean pointTaken=false;

    public void parseRegion(TextureAtlas atlas){
        t1=atlas.findRegion("1");
        t2=atlas.findRegion("2");
        t3=atlas.findRegion("3");
    }
    public Pig(int health){
        this.health=health;
    }

    public Pig(World world, TextureAtlas atlas, Vector2 position, float weight,int health){
        health*=6;
        this.health=health*4;
        this.maxHealth=health*4;

        parseRegion(atlas);
        Sprite pig=new Sprite(t1);
        pig.setPosition(position.x, position.y);

        this.sprite=pig;

        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((position.x+ sprite.getWidth()/2/ppm),(position.y+ sprite.getHeight()/2/ppm));



        body= world.createBody(bodyDef);
        float avg=(pig.getHeight()+pig.getWidth())/2f;
        CircleShape shape=new CircleShape();
        shape.setRadius(avg/2/ppm);


        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=shape;
        fixtureDef.density= weight;
        fixtureDef.friction=100f;
        fixtureDef.restitution=0f;
        fixtureDef.filter.categoryBits=0x1;
        fixtureDef.filter.maskBits=0x1 | 0x2;
        body.createFixture(fixtureDef);
        body.setAngularDamping(5f);
        body.getFixtureList().get(0).setUserData(this);


        shape.dispose();

    }
    public boolean isPointTaken() {
        return pointTaken;
    }
    public void takePoint(){
        pointTaken=true;
    }


    public Sprite getSprite(){
        return sprite;
    }
    public Body getBody(){
        return body;
    }
    public void getDamage(float damage){
        health-=damage;
        if(health<maxHealth*0.66f){
            this.sprite= new Sprite(t2);
        }
        else if(health<maxHealth*0.33f){
            this.sprite= new Sprite(t3);
        }
    }
    public void update(World world){
        boolean pos=(sprite.getX()<=1280)&&(0<=sprite.getX())&&(sprite.getY()<=720)&&(0<=sprite.getY());

        if(health<=0 || !pos){
            world.destroyBody(body);
            isDestroyed=true;
            System.out.println("Pig is destroyed");
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public float getHealth() {
        return health;
    }
}
