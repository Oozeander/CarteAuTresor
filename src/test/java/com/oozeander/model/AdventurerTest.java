package com.oozeander.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class AdventurerTest {

    private static TreasureMap treasureMap;

    @BeforeEach
    void setUp() {
        treasureMap = TreasureMap.builder()
                .width((short) 4)
                .height((short) 3)
                .mountains(new HashSet<Coordinate>())
                .treasures(new HashMap<Coordinate, Short>())
                .build();
    }

    @Nested
    class MoveOnce {

        private final Adventurer player = Adventurer.builder()
                .name("Billel")
                .movementSequence("ADG")
                .treasureCount((short) 0)
                .build();

        private static Stream<Arguments> should_do_nothing_when_invalid_move() {
            return Stream.of(
                    // x < 0
                    Arguments.of(Coordinate.builder().x((short) 0).y((short) 0).build(), Direction.WEST, (short) 0, (short) 0, treasureMap),
                    // y < 0
                    Arguments.of(Coordinate.builder().x((short) 0).y((short) 0).build(), Direction.NORTH, (short) 0, (short) 0, treasureMap),
                    // x > width
                    Arguments.of(Coordinate.builder().x((short) 4).y((short) 0).build(), Direction.EAST, (short) 4, (short) 0, treasureMap),
                    // y > height
                    Arguments.of(Coordinate.builder().x((short) 0).y((short) 3).build(), Direction.SOUTH, (short) 0, (short) 3, treasureMap),
                    // (x, y) == mountain
                    Arguments.of(Coordinate.builder().x((short) 0).y((short) 0).build(),
                            Direction.NORTH,
                            (short) 0, (short) 0,
                            TreasureMap.builder()
                                    .mountains(new HashSet<Coordinate>(Collections.singleton(
                                            Coordinate.builder().x((short) 0).y((short) 1).build())))
                                    .build()
                    )
            );
        }

        @Test
        void should_move_forward() {
            player.position(Coordinate.builder().x((short) 0).y((short) 0).build());
            player.orientation(Direction.EAST);

            player.moveOnce(treasureMap, player.movementSequence().charAt(0));

            assertThat(player.position().x()).isEqualTo((short) 1);
            assertThat(player.position().y()).isEqualTo((short) 0);
        }

        @Test
        void should_move_forward_and_find_treasure() {
            var treasureCoordinate = Coordinate.builder().x((short) 1).y((short) 0).build();
            treasureMap.addTreasure(treasureCoordinate, (short) 3);
            player.position(Coordinate.builder().x((short) 0).y((short) 0).build());
            player.orientation(Direction.EAST);

            player.moveOnce(treasureMap, player.movementSequence().charAt(0));

            assertThat(player.position().x()).isEqualTo((short) 1);
            assertThat(player.position().y()).isEqualTo((short) 0);
            assertThat(player.treasureCount()).isEqualTo((short) 1);
            assertThat(treasureMap.treasureCount(treasureCoordinate)).isEqualTo((short) 2);
        }

        @Test
        void should_not_move_forward_when_mountain_is_blocking_the_way() {
            treasureMap.addMountain(Coordinate.builder().x((short) 1).y((short) 0).build());
            player.position(Coordinate.builder().x((short) 0).y((short) 0).build());
            player.orientation(Direction.EAST);

            player.moveOnce(treasureMap, player.movementSequence().charAt(0));

            assertThat(player.position().x()).isEqualTo((short) 0);
            assertThat(player.position().y()).isEqualTo((short) 0);
        }

        @ParameterizedTest
        @ValueSource(chars = {'D', 'G', 'Z'})
        void should_turn(char turn) {
            player.position(Coordinate.builder().x((short) 0).y((short) 0).build());

            Arrays.stream(Direction.values()).forEach(direction -> {
                player.orientation(direction);

                Direction newDirection = null;
                if (turn == 'D') {
                    newDirection = switch (direction) {
                        case NORTH -> Direction.EAST;
                        case SOUTH -> Direction.WEST;
                        case EAST -> Direction.SOUTH;
                        case WEST -> Direction.NORTH;
                    };
                } else if (turn == 'G') {
                    newDirection = switch (direction) {
                        case NORTH -> Direction.WEST;
                        case SOUTH -> Direction.EAST;
                        case EAST -> Direction.NORTH;
                        case WEST -> Direction.SOUTH;
                    };
                } else {
                    assertThatExceptionOfType(IllegalArgumentException.class)
                            .isThrownBy(() -> player.moveOnce(treasureMap, turn))
                            .withMessage("Invalid turn provided: %s".formatted(turn));
                    return;
                }

                player.moveOnce(treasureMap, turn);

                assertThat(player.orientation()).isEqualTo(newDirection);
            });
        }

        @ParameterizedTest
        @MethodSource
        void should_do_nothing_when_invalid_move(Coordinate coordinate, Direction direction, short expectedX, short expectedY, TreasureMap treasureMap) {
            player.position(coordinate);
            player.orientation(direction);

            player.moveOnce(treasureMap, player.movementSequence().charAt(0));

            assertThat(player.position().x()).isEqualTo(expectedX);
            assertThat(player.position().y()).isEqualTo(expectedY);
        }
    }
}
