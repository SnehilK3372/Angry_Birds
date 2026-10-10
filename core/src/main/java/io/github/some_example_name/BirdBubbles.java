package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;
import java.util.List;

public class BirdBubbles extends Bird{
    static Texture atlantean=new Texture(Gdx.files.internal("Birds/atlanteanBubbles50.png"));
    static Texture mythic=new Texture(Gdx.files.internal("Birds/mythicBubbles50.png"));

    static List<Texture> textures= Arrays.asList(atlantean,mythic);

    public BirdBubbles(World world, Vector2 vec, int bird){
        super(world, textures.get(bird),vec,1,6);

    }
}
