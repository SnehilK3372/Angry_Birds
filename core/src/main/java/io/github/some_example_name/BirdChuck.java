package io.github.some_example_name;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;
import java.util.List;

public class BirdChuck extends Bird{

    private final static Texture vampBird=new Texture(Gdx.files.internal("Birds/vampChuck50.png"));
    private final static Texture phoenix=new Texture(Gdx.files.internal("Birds/phoenixChuck50.png"));
    private final static Texture star=new Texture(Gdx.files.internal("Birds/starChuck50.png"));
    private final static Texture ninja=new Texture(Gdx.files.internal("Birds/ninjaChuck50.png"));

    static List<Texture> textures= Arrays.asList(vampBird,phoenix,star,ninja);

    public BirdChuck(World world, Vector2 pos, int bird){

        super(world,textures.get(bird),pos,1,6);


    }

}
