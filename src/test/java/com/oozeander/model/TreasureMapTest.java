package com.oozeander.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TreasureMapTest {

    private static final Coordinate COORDINATE = Coordinate.builder().x((short) 3).y((short) 2).build();
    private static final Coordinate COORDINATE_OUT_OF_BOUNDS = Coordinate.builder().x((short) 5).y((short) 2).build();
    private static final String COORDINATE_OUT_OF_BOUNDS_MESSAGE = "The given coordinates are not within the treasure map !";
    private static final String MOUNTAIN_COORDINATE_ALREADY_TAKEN = "The given coordinates are already taken by a mountain !";
    private static final String TREASURE_COORDINATE_ALREADY_TAKEN = "The given coordinates are already taken by a treasure !";

    private TreasureMap treasureMap;

    @BeforeEach
    void setUp() {
        treasureMap = TreasureMap.builder()
                .width(4)
                .height(3)
                .mountains(new HashSet<Coordinate>())
                .treasures(new HashMap<Coordinate, Short>())
                .build();
    }

    @Test
    void should_return_string_representation() {
        var map = TreasureMap.builder()
                .width(3)
                .height(4)
                .mountains(Collections.singleton(
                        Coordinate.builder().x((short) 1).y((short) 0).build()
                ))
                .treasures(Collections.singletonMap(
                        Coordinate.builder().x((short) 0).y((short) 3).build(), (short) 5
                ))
                .players(Collections.singleton(
                        Adventurer.builder()
                                .position(
                                        Coordinate.builder().x((short) 2).y((short) 1).build()
                                )
                                .treasureCount((short) 2)
                                .orientation(Direction.EAST)
                                .movementSequence("ADDAAGA")
                                .name("Billel")
                                .build()
                ))
                .build();

        assertThat(map.toString()).isEqualTo("""
                C - 3 - 4
                M - 1 - 0
                T - 0 - 3 - 5
                A - Billel - 2 - 1 - E - 2""");
    }

    @Nested
    class IsMountain {

        @Test
        void should_return_if_mountain() {
            boolean isMountain = treasureMap.isMountain(COORDINATE);
            assertThat(isMountain).isFalse();
        }

        @Test
        void should_throw_when_coordinate_out_of_bounds() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> treasureMap.isMountain(COORDINATE_OUT_OF_BOUNDS))
                    .withMessage(COORDINATE_OUT_OF_BOUNDS_MESSAGE);
        }
    }

    @Nested
    class TreasureCount {

        @Test
        void should_return_treasure_count() {
            short treasureFound = 19;
            treasureMap.addTreasure(COORDINATE, treasureFound);
            short treasureCount = treasureMap.treasureCount(COORDINATE);
            assertThat(treasureCount).isEqualTo(treasureFound);
        }

        @Test
        void should_throw_when_coordinate_out_of_bounds() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> treasureMap.treasureCount(COORDINATE_OUT_OF_BOUNDS))
                    .withMessage(COORDINATE_OUT_OF_BOUNDS_MESSAGE);
        }
    }

    @Nested
    class AddMountain {

        @Test
        void should_add_mountain() {
            treasureMap.addMountain(COORDINATE);
            assertThat(treasureMap.isMountain(COORDINATE)).isTrue();
        }

        @Test
        void should_throw_when_coordinate_out_of_bounds() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> treasureMap.addMountain(COORDINATE_OUT_OF_BOUNDS))
                    .withMessage(COORDINATE_OUT_OF_BOUNDS_MESSAGE);
        }

        @Test
        void should_throw_when_coordinate_matches_mountain() {
            treasureMap.addMountain(COORDINATE);
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> treasureMap.addMountain(COORDINATE))
                    .withMessage(MOUNTAIN_COORDINATE_ALREADY_TAKEN);
        }

        @Test
        void should_throw_when_coordinate_matches_treasure() {
            treasureMap.addTreasure(COORDINATE, (short) 3);
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> treasureMap.addMountain(COORDINATE))
                    .withMessage(TREASURE_COORDINATE_ALREADY_TAKEN);
        }
    }

    @Nested
    class AddAdventurer {

        private final Adventurer player = Adventurer.builder()
                .position(COORDINATE)
                .build();

        @Test
        void should_add_player() {
            treasureMap.addAdventurer(player);
            assertThat(treasureMap.players())
                    .hasSize(1)
                    .containsExactly(player);
        }

        @Test
        void should_throw_when_coordinate_out_of_bounds() {
            player.position(COORDINATE_OUT_OF_BOUNDS);
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> treasureMap.addAdventurer(player))
                    .withMessage(COORDINATE_OUT_OF_BOUNDS_MESSAGE);
        }

        @Test
        void should_throw_when_coordinate_matches_mountain() {
            treasureMap.addMountain(COORDINATE);
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> treasureMap.addAdventurer(player))
                    .withMessage(MOUNTAIN_COORDINATE_ALREADY_TAKEN);
        }
    }

    @Nested
    class AddTreasure {

        @Test
        void should_add_treasures() {
            short treasureCount = 3;
            treasureMap.addTreasure(COORDINATE, treasureCount);
            assertThat(treasureMap.treasures())
                    .hasSize(1)
                    .containsKey(COORDINATE)
                    .containsValue(treasureCount);
        }

        @Test
        void should_throw_when_coordinate_out_of_bounds() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> treasureMap.addTreasure(COORDINATE_OUT_OF_BOUNDS, (short) 3))
                    .withMessage(COORDINATE_OUT_OF_BOUNDS_MESSAGE);
        }

        @Test
        void should_throw_when_coordinate_matches_mountain() {
            treasureMap.addMountain(COORDINATE);
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> treasureMap.addTreasure(COORDINATE, (short) 3))
                    .withMessage(MOUNTAIN_COORDINATE_ALREADY_TAKEN);
        }
    }
}
