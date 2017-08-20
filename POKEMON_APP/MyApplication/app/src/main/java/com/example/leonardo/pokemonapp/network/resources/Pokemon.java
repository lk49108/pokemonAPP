package com.example.leonardo.pokemonapp.network.resources;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.leonardo.pokemonapp.base.BaseMVP;
import com.example.leonardo.pokemonapp.database.model.PokemonDb;
import com.example.leonardo.pokemonapp.network.executor.ServiceCreator;
import com.example.leonardo.pokemonapp.network.resources.enumerations.Gender;
import com.example.leonardo.pokemonapp.network.resources.enumerations.Vote;
import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;
import moe.banana.jsonapi2.ResourceIdentifier;

/**
 * Created by leonardo on 12/07/17.
 */
@JsonApi(type = "pokemons")
public class Pokemon extends Resource implements Serializable, Comparable<Pokemon> {

    @Json(name = "image-url")
    private String imageSource;

    @Json(name = "name")
    private String name;

    @Json(name = "description")
    private String description;

    @Json(name = "height")
    private double height;

    @Json(name = "weight")
    private double weight;

    @Json(name = "gender")
    private Gender gender;

    @Json(name = "voted-on")
    Vote votedOn;

    private HasMany<Type> types;

    private HasMany<Comment> comments;

    private HasMany<Move> moves;

    private Comment[] pokemonComments;

    public Comment[] getPokemonComments() {
        return pokemonComments;
    }

    public void setPokemonComments(Comment[] pokemonComments) {
        this.pokemonComments = pokemonComments;
    }

    public Vote getVotedOn() {
        return votedOn;
    }

    public void setVotedOn(Vote votedOn) {
        this.votedOn = votedOn;
    }

    public Pokemon() {}

    public String getImageSource() {
        if(imageSource != null && imageSource.startsWith("/")) {
            return ServiceCreator.API_ENDPOINT + imageSource;
        }
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public HasMany<Type> getTypes() {
        return types;
    }

    public void setTypes(HasMany<Type> types) {
        this.types = types;
    }

    public HasMany<Comment> getComments() {
        return comments;
    }

    public void setComments(HasMany<Comment> comments) {
        this.comments = comments;
    }

    public HasMany<Move> getMoves() {
        return moves;
    }

    public void setMoves(HasMany<Move> moves) {
        this.moves = moves;
    }

    public int[] getTypeIds() {
        List<ResourceIdentifier> types = getTypes().get();
        if(types == null || types.size() == 0) {
            return null;
        }

        int[] ids = new int[types.size()];
        for(int i = 0; i < types.size(); i++) {
            ids[i] = Integer.parseInt(types.get(i).getId());
        }

        return ids;
    }

    public int[] getMoveIds() {
        List<ResourceIdentifier> moves = getMoves().get();
        if(moves == null || moves.size() == 0) {
            return null;
        }

        int[] ids = new int[moves.size()];
        for(int i = 0; i < ids.length; i++) {
            ids[i] = Integer.parseInt(moves.get(i).getId());
        }

        return ids;
    }


    public Pokemon(String name, String description, double height, double weight, String imageSource, Gender gender, Type[] types, Move[] moves, Vote votedOn) {
        this.name = name;
        this.description = description;
        this.height = height;
        this.weight = weight;
        this.imageSource = imageSource;
        this.gender = gender;
        setTypes(new HasMany<>(types));
        setMoves(new HasMany<>(moves));
        this.votedOn = votedOn;
    }

    public static Pokemon fromPokemonDb(PokemonDb pokemon) {

        Pokemon newPokemon = new Pokemon(
                pokemon.getName(),
                pokemon.getDescription(),
                pokemon.getHeight(),
                pokemon.getWeight(),
                pokemon.getImageUri(),
                pokemon.getGender(),
                pokemon.getTypes(),
                pokemon.getMoves(),
                pokemon.getVotedOn()
        );

        newPokemon.setId(String.valueOf(pokemon.getId()));
        return newPokemon;
    }

    public static Pokemon[] fromPokemonDbList(List<PokemonDb> pokemon) {
        Pokemon[] pokemons = new Pokemon[pokemon.size()];
        for(int i = 0; i < pokemons.length; i++) {
            pokemons[i] = fromPokemonDb(pokemon.get(i));
        }

        return pokemons;
    }

    public static List<Pokemon> fromPokemonDbListlToList(List<PokemonDb> pokemon) {
        List<Pokemon> pokemons = new ArrayList<>();
        for(int i = 0; i < pokemon.size(); i++) {
            pokemons.add(fromPokemonDb(pokemon.get(i)));
        }

        return pokemons;
    }

    @Override
    public int compareTo(@NonNull Pokemon o) {
        return Integer.parseInt(o.getId()) - Integer.parseInt(getId());
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
