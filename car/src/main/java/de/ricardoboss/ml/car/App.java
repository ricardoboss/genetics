package de.ricardoboss.ml.car;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.util.Random;

public class App extends PApplet {
    public static void main(String[] args) {
        PApplet.main(App.class, args);
    }

    private static final int MILLIS_PER_UPDATE = 40;

    static App current;

    private Playground playground;
    private int lastUpdate;

    public App() {
        current = this;
    }

    @Override
    public void settings() {
        size(1400, 900);
    }

    @Override
    public void setup() {
        // specify target frame rate
        frameRate(80);

        // draw rectangles around a center point
        rectMode(CENTER);

        // create the playground which contains the track and the cars
        playground = new Playground(1350, 850);

        // create the track
        playground.track = new Track();

        for (int i = 0; i < 100; i++){
            playground.cars.add(new Car());
        }
    }

    @Override
    public void draw() {
        // main loop

        // apply physics
        update();

        // update image
        paint();
    }

    private void paint() {
        // move (0;0) to center of window
        translate(width / 2f, height / 2f);

        // black background
        background(255);

        // draw the playground
        playground.draw();

        // draw FPS in top left corner
        fill(0);
        text((int)Math.ceil(frameRate), -width / 2f, -height / 2f + 12);
    }

    private void update() {
        // check for update
        var currentMillis = millis();
        if (currentMillis - lastUpdate <= MILLIS_PER_UPDATE)
            return;

        playground.update();

        if (playground.cars.size() > 0) {
            var r = new Random();
            var c = playground.cars.get(r.nextInt(playground.cars.size()));

            var s = r.nextFloat();
            if (s < 0.3f)
                c.brake();
            else if (s < 0.5f)
                c.accelerate();
            else if (s < 0.9f)
                c.steer(r.nextFloat() * 1.8f - 0.9f);
        }

        lastUpdate = currentMillis;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        // convert coordinates to translated image
        var mx = e.getX() - width / 2f;
        var my = e.getY() - height / 2f;

        var c = new Car();
        c.position.set(mx, my);
        c.velocity = PVector.random2D();
        playground.cars.add(c);
    }
}
