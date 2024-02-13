package com.oozeander.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Builder
@Accessors(fluent = true)
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreasureMap {

    private static final String COORDINATE_OUT_OF_BOUNDS = "The given coordinates are not within the treasure map !";
    private static final String MOUNTAIN_COORDINATE_ALREADY_TAKEN = "The given coordinates are already taken by a mountain !";
    private static final String TREASURE_COORDINATE_ALREADY_TAKEN = "The given coordinates are already taken by a treasure !";

    final int width;
    final int height;
    @Builder.Default
    Set<Coordinate> mountains = new HashSet<>();
    @Builder.Default
    Map<Coordinate, Short> treasures = new HashMap<>();
    @Builder.Default
    Set<Adventurer> players = new HashSet<>();

    public void addMountain(Coordinate coordinate) {
        if (coordinate.x() > width || coordinate.y() > height)
            throw new IllegalArgumentException(COORDINATE_OUT_OF_BOUNDS);
        else if (mountains.contains(coordinate))
            throw new IllegalArgumentException(MOUNTAIN_COORDINATE_ALREADY_TAKEN);
        else if (treasures.containsKey(coordinate))
            throw new IllegalArgumentException(TREASURE_COORDINATE_ALREADY_TAKEN);
        mountains.add(coordinate);
    }

    public void addAdventurer(Adventurer player) {
        if (player.position().x() > width || player.position().y() > height)
            throw new IllegalArgumentException(COORDINATE_OUT_OF_BOUNDS);
        else if (mountains.contains(player.position()))
            throw new IllegalArgumentException(MOUNTAIN_COORDINATE_ALREADY_TAKEN);
        players.add(player);
    }

    public void addTreasure(Coordinate coordinate, short count) {
        if (coordinate.x() > width || coordinate.y() > height)
            throw new IllegalArgumentException(COORDINATE_OUT_OF_BOUNDS);
        else if (mountains.contains(coordinate))
            throw new IllegalArgumentException(MOUNTAIN_COORDINATE_ALREADY_TAKEN);
        treasures.put(coordinate, count);
    }

    public boolean isMountain(Coordinate coordinate) {
        if (coordinate.x() > width || coordinate.y() > height)
            throw new IllegalArgumentException(COORDINATE_OUT_OF_BOUNDS);
        return mountains.contains(coordinate);
    }

    public short treasureCount(Coordinate coordinate) {
        if (coordinate.x() > width || coordinate.y() > height)
            throw new IllegalArgumentException(COORDINATE_OUT_OF_BOUNDS);
        return treasures.getOrDefault(coordinate, (short) 0);
    }

    public String toString() {
        var stringBuilder = new StringBuilder("""
                C - %d - %d
                """.formatted(width, height));

        mountains.forEach(mountain -> stringBuilder.append("""
                M - %d - %d
                """.formatted(mountain.x(), mountain.y())));

        treasures.forEach((coordinate, count) -> {
            if (count != 0)
                stringBuilder.append("""
                        T - %d - %d - %d
                        """.formatted(coordinate.x(), coordinate.y(), count));
        });

        players.forEach(player -> stringBuilder.append("""
                A - %s - %d - %d - %s - %d
                """.formatted(
                player.name(), player.position().x(), player.position().y(), player.orientation().value(), player.treasureCount())));

        return stringBuilder.toString().trim();
    }
}
