
package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class BlockStone extends Block{

    BlockStone(World world, TextureRegion texture, Vector2 pos,String blockType,boolean rotate) {
        super(world,texture,pos,3,blockType,rotate,12);
    }
}
