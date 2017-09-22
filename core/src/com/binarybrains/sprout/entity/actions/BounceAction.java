package com.binarybrains.sprout.entity.actions;


import java.util.Random;

/** Moves an actor from its current position to a specific position.*/
public class BounceAction extends TemporalAction {

    private double xa, ya, za;
    private double xx, yy, zz;

    protected void begin () {
        Random random = new Random();
        xx = target.getX();
        yy = target.getY();
        zz = 2;
        // accelerate values
        xa = random.nextGaussian() * 0.3;
        ya = random.nextGaussian() * 0.2;
        za = random.nextFloat() * 0.7 + 2;
    }

    @Override
    protected void end() {
        System.out.println("has ended");
    }

    protected void update (float percent) {

        xx += xa;
        yy += ya;
        zz += za;
        if (zz < 0) {
            zz = 0;
            za *= -0.5;
            xa *= 0.6;
            ya *= 0.6;
        }
        za -= 0.15;

        System.out.println("Updating BounceAction!!");

        // todo setZ(float zz)
        target.setPosition((float)xx, (float)yy + (float)zz);
        // target.setPosition(startX + (endX - startX) * percent, startY + (endY - startY) * percent);
    }

    public void reset () {
        super.reset();
    }

}
