package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.Sys;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {


    @Test
    void testCollisionBetweenBirdAndPig() {

        BirdRed bird=new BirdRed();
        PigChef pig =new PigChef();

        CollisionListener listener = new CollisionListener();

        listener.collider(bird,pig);

        assertEquals(35, bird.getHealth(), "Bird's health should reduce by 15");
        System.out.println(bird.getHealth());
        assertEquals(25, pig.getHealth(), "Pig's health should reduce by 15");
        System.out.println(pig.getHealth());
    }

}
