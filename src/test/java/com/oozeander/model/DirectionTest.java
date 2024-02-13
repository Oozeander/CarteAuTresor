package com.oozeander.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DirectionTest {

    @ParameterizedTest
    @ValueSource(chars = {'N', 'W', 'E', 'S', 'Z'})
    void should_return_direction_by_char_value_or_throw_if_error(char value) {
        switch (value) {
            case 'N' -> assertThat(Direction.directionByValue(value)).isEqualTo(Direction.NORTH);
            case 'W' -> assertThat(Direction.directionByValue(value)).isEqualTo(Direction.WEST);
            case 'E' -> assertThat(Direction.directionByValue(value)).isEqualTo(Direction.EAST);
            case 'S' -> assertThat(Direction.directionByValue(value)).isEqualTo(Direction.SOUTH);
            default -> assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> Direction.directionByValue(value))
                    .withMessage("Invalid orientation provided: %s".formatted(value));
        }
    }
}
