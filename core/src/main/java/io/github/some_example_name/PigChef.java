package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PigChef extends Pig{
    private static final TextureAtlas atlas=new TextureAtlas(Gdx.files.internal("Pigs/chef.atlas"));
    public PigChef(World world, Vector2 pos){
        super(world,atlas,pos,1.5f,12);
    }
    public PigChef(){
        super(23);
    }

}
