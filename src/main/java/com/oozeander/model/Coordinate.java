package com.oozeander.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Accessors(fluent = true)
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coordinate {

    short x, y;
}
