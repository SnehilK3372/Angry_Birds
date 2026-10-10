package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;

public abstract class Bird implements Serializable{
    private static final long serialVersionUID = 1L; // For Java serialization
    private float health;
    private Texture texture;
    transient private TextureAtlas atlas;
    transient private Sprite sprite;
    transient Body body;
    final float ppm =18f;
    private boolean isLaunched=false;
    private float deltaAfterLaunch=0f;
    private boolean isDestroyed=false;

    public Bird(int health){
        this.health=health;
    }
    public Bird(World world, Texture texture,Vector2 position,float weight,float health){//position is already set for ppm

        this.texture=texture;
        this.health=health;
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Sprite birb=new Sprite(texture);
        birb.setPosition(position.x, position.y);
        birb.setOrigin(birb.getWidth()/2,birb.getHeight()/2 );


        this.sprite=birb;

        BodyDef bodyDef=new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((position.x+ sprite.getWidth()/2/ppm),(position.y+ sprite.getHeight()/2/ppm));



        body= world.createBody(bodyDef);
        float avg=(birb.getHeight()+birb.getWidth())/2f;
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
    public void update(World world, float delta){
        if(isLaunched){
            deltaAfterLaunch+=delta;
        }
        boolean pos=(sprite.getX()<=1280)&&(sprite.getY()<=720)&&(0<=sprite.getY());
        if((health<=0 && deltaAfterLaunch>=7) || !pos){
            world.destroyBody(body);
            isDestroyed=true;
            System.out.println("This bird is destroyed");
        }
    }
    public Body getBody() {
        return body;
    }
    public void launch(){
        isLaunched=true;
    }
    public boolean isDestroyed(){
        return isDestroyed;
    }

    public Texture getTexture(){
        return this.texture;
    }

    public Sprite getSprite(){
        return sprite;
    }
    public void getDamage(float damage){
        health-=damage;
    }

    public float getHealth() {
        return health;
    }
}
