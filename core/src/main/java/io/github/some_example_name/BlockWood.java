package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class BlockWood extends Block{

    BlockWood(World world, TextureRegion texture, Vector2 pos,String blockType, boolean rotate) {
        super(world,texture,pos,2,blockType,rotate,10);
    }
}
