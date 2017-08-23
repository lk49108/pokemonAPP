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

@JsonApi(type = "types")
public class Type extends Resource implements Serializable {

    private static List<Type> types;

    static {
        types = new ArrayList<>();

        try {
            InputStream inputStream = PermissionApp.getPermissionApp().getAssets().open("pokemon_types.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            Set<String> ids = properties.stringPropertyNames();

            for(String id : ids) {
                Type type = new Type(properties.getProperty(id));
                type.setId(id);
                types.add(type);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Json(name = "name")
    private String name;

    private Type() {}

    private Type(String name)  {
        this.name = name;
    }

    public String getName() {
      return name;
    }

    public static List<Type> getTypes() {
        return types;
    }

    public static Type getFromId(int i) {
        for(Type type : types) {
            if (Integer.parseInt(type.getId()) == i) {
                return type;
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
