package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;
import java.util.List;

public class BirdRed extends Bird{

    static Texture spaceBird=new Texture(Gdx.files.internal("Birds/spaceRed50.png"));
    static Texture phoenixBird=new Texture(Gdx.files.internal("Birds/phoenixRed50.png"));
    static Texture gokuBird=new Texture(Gdx.files.internal("Birds/gokuRed.png"));
    static Texture wastelanderBird=new Texture(Gdx.files.internal("Birds/wastelanderRed60.png"));
    static Texture tribalBird=new Texture(Gdx.files.internal("Birds/tribalRed50.png"));
    static List<Texture> textures= Arrays.asList(spaceBird,phoenixBird,gokuBird,wastelanderBird,tribalBird);


    public BirdRed(World world, Vector2 pos, int bird){
        super(world,textures.get(bird),pos,1,5);

    }
    public BirdRed(){
        super(23);
    }

}
