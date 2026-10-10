package io.github.some_example_name;

import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.World;

public class BasicContactImpulse extends ContactImpulse {
    private final float[] normalImpulses;

    public BasicContactImpulse(World world,int force) {
        super(world,0);
        // Array of normal impulses, just mock it with a single value for simplicity
        this.normalImpulses = new float[]{force};
    }

    @Override
    public float[] getNormalImpulses() {
        return normalImpulses;
    }

    @Override
    public float[] getTangentImpulses() {
        return new float[0]; // This can be left empty if you're not using it.
    }
}
