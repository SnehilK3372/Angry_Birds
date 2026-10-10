package io.github.some_example_name;

import com.badlogic.gdx.physics.box2d.*;
import org.lwjgl.Sys;

public class CollisionListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        //collide(objectA,objectB,contact);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        Fixture fixA= contact.getFixtureA();
        Fixture fixB= contact.getFixtureB();

        if(fixA.getUserData()==null||fixB.getUserData()==null) return;

        Object objectA=fixA.getUserData();
        Object objectB=fixB.getUserData();
        collide(objectA,objectB,contactImpulse);
      //  updateScore(objectA,objectB,contactImpulse);

    }
    public void collide(Object objectA,Object objectB,ContactImpulse contactImpulse){
        float force = contactImpulse.getNormalImpulses()[0];
        handleDamage(objectA,objectB,force);
    }
    public void collider(Object object1,Object object2){

    }
    public void handleDamage(Object objectA,Object objectB, float force){
        if (force>1f) {
            float damage=force/30f;

            if(objectA instanceof Bird && objectB instanceof Pig){
                ((Bird) objectA).getDamage(damage);
                ((Pig) objectB).getDamage(damage);
            }
            else if(objectB instanceof Bird && objectA instanceof Pig){
                ((Pig) objectA).getDamage(damage);
                ((Bird) objectB).getDamage(damage);
            }
            else if("ground".equals(objectA)){
                if(objectB instanceof Pig){
                    ((Pig) objectB).getDamage(damage);
                }
                else if(objectB instanceof Bird){
                    ((Bird) objectB).getDamage(damage);
                }
            }
            else if("ground".equals(objectB)){
                if(objectA instanceof Pig){
                    ((Pig) objectA).getDamage(damage);
                }
                else if(objectA instanceof Bird){
                    ((Bird) objectA).getDamage(damage);
                }
            }
            else if(objectA instanceof Block){
                ((Block) objectA).getDamage(damage);
                if(objectB instanceof Pig){
                    ((Pig) objectB).getDamage(damage);
                }
                else if(objectB instanceof Bird){
                    ((Bird) objectB).getDamage(damage);
                }
            }
            else if(objectB instanceof Block){
                ((Block) objectB).getDamage(damage);
                if(objectA instanceof Pig){
                    ((Pig) objectA).getDamage(damage);
                }
                else if(objectA instanceof Bird){
                    ((Bird) objectA).getDamage(damage);
                }
            }
        }
    }

}
