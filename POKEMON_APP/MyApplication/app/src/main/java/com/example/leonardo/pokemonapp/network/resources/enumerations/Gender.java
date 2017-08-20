package com.example.leonardo.pokemonapp.network.resources.enumerations;

/**
 * Created by leonardo on 09/08/17.
 */

public enum Gender {

    MALE(1, "male"),
    FEMALE(2, "female"),
    UNKNOWN(0, "unknown");

    private final String name;

    private final int id;

    Gender(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static Gender getFromId(int id) {
        switch (id) {
            case 1:
                return MALE;
            case 2:
                return FEMALE;
            default:
                return UNKNOWN;
        }
    }
}
