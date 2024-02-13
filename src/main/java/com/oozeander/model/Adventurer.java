package com.oozeander.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Adventurer {

    final String name;
    final String movementSequence;
    Coordinate position;
    Direction orientation;
    short treasureCount;

    public void moveOnce(TreasureMap treasureMap, char move) {
        if (move == 'A') {
            Coordinate newPosition = getNextPosition();
            if (isValidMove(newPosition, treasureMap)) {
                position = newPosition;
                int treasure = treasureMap.treasureCount(position);
                if (treasure > 0) {
                    treasureCount++;
                    treasureMap.addTreasure(position, (short) --treasure);
                }
            }
        } else {
            updateOrientation(move);
        }
    }

    private Coordinate getNextPosition() {
        return switch (orientation) {
            case NORTH -> Coordinate.builder().x(position.x()).y((short) (position.y() - 1)).build();
            case EAST -> Coordinate.builder().x((short) (position.x() + 1)).y(position.y()).build();
            case SOUTH -> Coordinate.builder().x(position.x()).y((short) (position.y() + 1)).build();
            case WEST -> Coordinate.builder().x((short) (position.x() - 1)).y(position.y()).build();
        };
    }

    private boolean isValidMove(Coordinate newPosition, TreasureMap treasureMap) {
        return newPosition.x() >= 0 && newPosition.x() < treasureMap.width() &&
                newPosition.y() >= 0 && newPosition.y() < treasureMap.height() &&
                !treasureMap.isMountain(newPosition);
    }

    private void updateOrientation(char turn) {
        switch (turn) {
            case 'G' -> orientation = Direction.directionByValue(turnLeft());
            case 'D' -> orientation = Direction.directionByValue(turnRight());
            default -> throw new IllegalArgumentException("Invalid turn provided: %s".formatted(turn));
        }
    }

    private char turnLeft() {
        return switch (orientation) {
            case NORTH -> Direction.WEST.value();
            case WEST -> Direction.SOUTH.value();
            case SOUTH -> Direction.EAST.value();
            case EAST -> Direction.NORTH.value();
        };
    }

    private char turnRight() {
        return switch (orientation) {
            case NORTH -> Direction.EAST.value();
            case EAST -> Direction.SOUTH.value();
            case SOUTH -> Direction.WEST.value();
            case WEST -> Direction.NORTH.value();
        };
    }
}
