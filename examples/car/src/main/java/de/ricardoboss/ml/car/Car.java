package de.ricardoboss.ml.car;

import processing.core.PConstants;
import processing.core.PVector;

class Car {
    private static final int WIDTH = 15;
    private static final int HEIGHT = 35;
    private static final float MAX_SPEED = 10f;
    private static final float MIN_SPEED = 0.1f;

    PVector position = new PVector(0, 0);
    PVector velocity = PVector.random2D();

    void draw() {
        App.current.pushStyle();
        App.current.pushMatrix();

        // make (0;0) the center of the car
        App.current.translate(position.x, position.y);

        drawBody();
        drawText();

        App.current.popMatrix();
        App.current.popStyle();
    }

    private void drawBody()
    {
        App.current.pushStyle();
        App.current.pushMatrix();

        // correct orientation with respect to the direction we are travelling to
        App.current.rotate(velocity.heading() + PConstants.HALF_PI);

        App.current.strokeWeight(1); // thin border
        App.current.stroke(0); // black border
        App.current.fill(255, 180, 180); // red fill

        // draw a car shape
        App.current.rect(0, 0, WIDTH, HEIGHT);

        // draw a line which represents the velocity vector
        App.current.strokeWeight(2);
        App.current.line(0, -velocity.mag() * HEIGHT / 2f, 0, 0);

        App.current.popMatrix();
        App.current.popStyle();
    }

    private void drawText()
    {
        App.current.pushStyle();
        App.current.pushMatrix();

        App.current.fill(0);
        App.current.text("X: " + (int)position.x, 0, 12);
        App.current.text("Y: " + (int)position.y, 0, 24);
        App.current.text("Yaw: " + velocity.heading(), 0, 36);
        App.current.text("Spd: " + velocity.mag(), 0, 48);

        App.current.popMatrix();
        App.current.popStyle();
    }

    public void accelerate() {
        velocity.mult(1.1f);
    }

    public void brake() {
        velocity.mult(0.8f);
    }

    public void steer(float theta) {
        velocity.rotate(theta);
    }

    void update() {
        var spd = velocity.mag();
        if (spd > MAX_SPEED)
            velocity.normalize().mult(MAX_SPEED);
        else if (spd < MIN_SPEED)
            velocity.normalize().mult(MIN_SPEED);

        position.add(velocity);
    }
}
