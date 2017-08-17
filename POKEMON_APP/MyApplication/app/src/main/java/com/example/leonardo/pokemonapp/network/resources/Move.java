package com.example.leonardo.pokemonapp.network.resources;


import com.example.leonardo.pokemonapp.PermissionApp;
import com.squareup.moshi.Json;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by leonardo on 08/08/17.
 */
@JsonApi(type = "moves")
public class Move extends Resource implements Serializable {

    private static List<Move> moves;

    static {
        moves = new ArrayList<>();

        try {
            InputStream inputStream = PermissionApp.getPermissionApp().getAssets().open("pokemon_moves.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            Set<String> ids = properties.stringPropertyNames();

            for(String id : ids) {
                Move move = new Move(properties.getProperty(id));
                move.setId(id);
                moves.add(move);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Json(name = "name")
    private String name;

    private Move() {}

    private Move(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<Move> getMoves() {
        return moves;
    }

    public static Move getFromId(int id) {
        for(Move move : moves) {
            if(Integer.parseInt(move.getId()) == id) {
                return move;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
