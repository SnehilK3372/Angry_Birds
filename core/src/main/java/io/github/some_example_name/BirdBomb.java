package io.github.some_example_name;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;
import java.util.List;

public class BirdBomb extends Bird{

    private final static Texture hunter=new Texture(Gdx.files.internal("Birds/hunterBomb.png"));
    private final static Texture phoenix=new Texture(Gdx.files.internal("Birds/phoenixBomb55.png"));
    private final static Texture space=new Texture(Gdx.files.internal("Birds/spaceBomb58.png"));

    static List<Texture> textures= Arrays.asList(hunter,phoenix,space);

    public BirdBomb(World world, Vector2 position, int bird){
        super(world,textures.get(bird),position,1,8);

        for(int i=0;i<textures.size();i++){//cleanup texture from the GPU
            if(i!=bird){
                textures.get(i).dispose();
            }
        }
    }

}
