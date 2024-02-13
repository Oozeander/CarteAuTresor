package com.oozeander;

import com.oozeander.model.Adventurer;
import com.oozeander.model.Coordinate;
import com.oozeander.model.Direction;
import com.oozeander.model.TreasureMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class TreasureHunt {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java TreasureHunt <input_file> <output_file>");
            System.exit(1);
        }

        String inputFile = args[0], outputFile = args[1];

        TreasureMap map = parseInput(inputFile);
        simulateMovement(map);

        writeOutput(map, outputFile);
    }

    private static TreasureMap parseInput(String inputFile) throws IOException {
        AtomicReference<TreasureMap> treasureMap = new AtomicReference<>();
        Files.readAllLines(Path.of(inputFile))
                .stream().dropWhile(line -> line.startsWith("#"))
                .forEach(line -> {
                    String[] parts = line.split(" - ");
                    String type = parts[0];

                    switch (type) {
                        case "C" -> {
                            treasureMap.set(TreasureMap.builder()
                                    .width(Integer.parseInt(parts[1]))
                                    .height(Integer.parseInt(parts[2]))
                                    .build()
                            );
                        }
                        case "M" -> {
                            treasureMap.get().addMountain(Coordinate.builder()
                                    .x((short) Integer.parseInt(parts[1]))
                                    .y((short) Integer.parseInt(parts[2]))
                                    .build());
                        }
                        case "T" -> {
                            treasureMap.get().addTreasure(
                                    Coordinate.builder()
                                            .x((short) Integer.parseInt(parts[1]))
                                            .y((short) Integer.parseInt(parts[2]))
                                            .build(),
                                    (short) Integer.parseInt(parts[3]));
                        }
                        case "A" -> {
                            treasureMap.get().addAdventurer(Adventurer.builder()
                                    .name(parts[1])
                                    .movementSequence(parts[5])
                                    .position(
                                            Coordinate.builder()
                                                    .x((short) Integer.parseInt(parts[2]))
                                                    .y((short) Integer.parseInt(parts[3]))
                                                    .build())
                                    .orientation(Direction.directionByValue(parts[4].charAt(0)))
                                    .treasureCount((short) 0)
                                    .build()
                            );
                        }
                        default -> throw new IllegalArgumentException("Unknown type: %s".formatted(type));
                    }
                });
        return treasureMap.get();
    }

    private static void simulateMovement(TreasureMap treasureMap) {
        for (Adventurer player : treasureMap.players())
            player.movementSequence().chars().forEach(move -> player.moveOnce(treasureMap, (char) move));
    }

    private static void writeOutput(TreasureMap treasureMap, String outputFile) throws IOException {
        Files.writeString(Path.of(outputFile), treasureMap.toString());
    }
}
