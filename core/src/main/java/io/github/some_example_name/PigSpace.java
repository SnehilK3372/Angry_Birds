package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PigSpace extends Pig{
    private static final TextureAtlas atlas=new TextureAtlas(Gdx.files.internal("Pigs/space.atlas"));
    public PigSpace(World world, Vector2 pos){
        super(world,atlas,pos,0.75f,9);
    }
}
