package de.ricardoboss.ml.car;

import java.util.ArrayList;
import java.util.List;

class Playground {
    Track track;
    float width;
    float height;
    final List<Car> cars = new ArrayList<>();

    public Playground(float width, float height) {
        this.width = width;
        this.height = height;
    }

    void draw() {
        App.current.pushMatrix();
        App.current.pushStyle();

        // draw a border around the playground
        App.current.stroke(80);
        App.current.strokeWeight(5);

        // fill the border with a light grey
        App.current.fill(230);

        // draw the playground
        App.current.rect(0, 0, width, height);

        // draw the track
        track.draw();

        // draw all the cars
        cars.forEach(Car::draw);

        App.current.popMatrix();
        App.current.popStyle();
    }

    void update() {
        var iter = cars.listIterator();
        Car c;

        while (iter.hasNext()) {
            c = iter.next();
            c.update();

            // check if out of window bounds
            if (Math.abs(c.position.x) > width / 2f || Math.abs(c.position.y) > height / 2f ||
                    !track.isOn(c.position))
                iter.remove();
        }
    }
}
