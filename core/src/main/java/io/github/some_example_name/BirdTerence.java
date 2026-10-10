package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;
import java.util.List;

public class BirdTerence extends Bird{

    static Texture darkKnight=new Texture(Gdx.files.internal("Birds/darknightTerence120.png"));
    static Texture dragon=new Texture(Gdx.files.internal("Birds/dragonRed100.png"));
    static Texture space=new Texture(Gdx.files.internal("Birds/Terence1.png"));

    static List<Texture> textures= Arrays.asList(darkKnight,dragon,space);

    public BirdTerence(World world, Vector2 pos, int bird){
        super(world,textures.get(bird),pos,0.5f,20);

    }
}
