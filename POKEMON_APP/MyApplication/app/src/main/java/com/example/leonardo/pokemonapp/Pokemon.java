package com.example.leonardo.pokemonapp;

/**
 * Created by leonardo on 12/07/17.
 */

public class Pokemon {

    private String name;

    private String description;

    public Pokemon(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
