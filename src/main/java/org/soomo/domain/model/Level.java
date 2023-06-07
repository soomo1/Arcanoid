package org.soomo.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private int levelNumber;
    private List<Brick> bricks;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.bricks = createLevel();
    }

    private List<Brick> createLevel() {
        List<Brick> bricks = new ArrayList<>();
        // Add bricks to the list
        // This is where we'd have different arrangements of bricks for different levels
        for (int i = 0; i < levelNumber * 2; i++) {
            for (int j = 0; j < 10; j++) {
                Brick brick = new Brick(Brick.WIDTH * j, Brick.HEIGHT * i);
                bricks.add(brick);
            }
        }
        return bricks;
    }

    public List<Brick> getBricks() {
        return bricks;
    }
}
