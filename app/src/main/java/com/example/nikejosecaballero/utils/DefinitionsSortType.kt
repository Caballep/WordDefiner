package com.example.nikejosecaballero.utils

enum class DefinitionsSortType(val sortTypeValue: String) {

    // TODO: Find a better way
    NONE("Nothing"),
    HIGHEST_RATE("Highest rated"),
    LOWEST_RATE("Lowest rated");

    companion object {
        fun fromString(value: String) = values().first { it.sortTypeValue == value }
    }
}