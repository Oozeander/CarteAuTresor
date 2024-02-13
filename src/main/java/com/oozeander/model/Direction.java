package com.oozeander.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Direction {
    NORTH('N'), SOUTH('S'), EAST('E'), WEST('W');

    char value;

    public static Direction directionByValue(char value) {
        return switch (value) {
            case 'N' -> Direction.NORTH;
            case 'S' -> Direction.SOUTH;
            case 'E' -> Direction.EAST;
            case 'W' -> Direction.WEST;
            default -> throw new IllegalArgumentException("Invalid orientation provided: %s".formatted(value));
        };
    }
}
