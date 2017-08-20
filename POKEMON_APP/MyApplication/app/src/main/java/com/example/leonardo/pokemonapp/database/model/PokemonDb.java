package com.example.leonardo.pokemonapp.database.model;

import com.example.leonardo.pokemonapp.database.AppDatabase;
import com.example.leonardo.pokemonapp.database.model.converter.PokemonGenderConverter;
import com.example.leonardo.pokemonapp.database.model.converter.PokemonVoteConverter;
import com.example.leonardo.pokemonapp.network.resources.Move;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.example.leonardo.pokemonapp.network.resources.Type;
import com.example.leonardo.pokemonapp.network.resources.enumerations.Gender;
import com.example.leonardo.pokemonapp.network.resources.enumerations.Vote;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 26/07/17.
 */
@Table(database = AppDatabase.class)
public class PokemonDb extends BaseModel implements Serializable {

    @PrimaryKey
    private int id;

    @Column
    private String imageUri;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private double height;

    @Column
    private double weight;

    @Column
    String typeIds;

    @Column
    String moveIds;

    @Column(typeConverter = PokemonVoteConverter.class)
    Vote votedOn;

    @Column(typeConverter = PokemonGenderConverter.class)
    private Gender gender;

    public PokemonDb() {}

    public PokemonDb(int id, String name, String description, double height, double weight, String typeIds, String moveIds, Vote votedOn, Gender gender, String imageUri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.height = height;
        this.weight = weight;
        this.typeIds = typeIds;
        this.moveIds = moveIds;
        this.votedOn = votedOn;
        this.gender = gender;
        this.imageUri = imageUri;
    }

    public static PokemonDb fromPokemon(Pokemon pokemon) {
        return new PokemonDb(
                Integer.parseInt(pokemon.getId()),
                pokemon.getName(),
                pokemon.getDescription(),
                pokemon.getHeight(),
                pokemon.getWeight(),
                getMovesString(pokemon.getMoveIds()),
                getTypesString(pokemon.getTypeIds()),
                pokemon.getVotedOn(),
                pokemon.getGender(),
                pokemon.getImageSource()
        );
    }

    private static String getTypesString(int[] typeIds) {
        if(typeIds == null) {
            return "";
        }

        String typesString = "";
        for(int i = 0; i < typeIds.length; i++) {
            typesString += String.valueOf(typeIds[i]) + ",";
        }

        return typesString.substring(0, typesString.length() - 1);
    }

    private static String getMovesString(int[] moveIds) {
        if(moveIds == null) {
            return "";
        }

        String movesString = "";
        for(int i = 0; i < moveIds.length; i++) {
            movesString += String.valueOf(moveIds[i]) + ",";
        }

        return movesString.substring(0, movesString.length() - 1);
    }

    public static PokemonDb[] fromPokemonArray(Pokemon[] pokemon) {
        PokemonDb[] pokemonsDb = new PokemonDb[pokemon.length];
        for(int i = 0; i < pokemonsDb.length; i++) {
            pokemonsDb[i] = fromPokemon(pokemon[i]);
        }

        return pokemonsDb;
    }

    public static PokemonDb[] fromPokemonList(List<Pokemon> pokemons) {
        PokemonDb[] pokemonsDb = new PokemonDb[pokemons.size()];
        for(int i = 0; i < pokemonsDb.length; i++) {
            pokemonsDb[i] = fromPokemon(pokemons.get(i));
        }

        return pokemonsDb;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
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

    public void setHeight(double height) {
        this.height = height;
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

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public Vote getVotedOn() {
        return votedOn;
    }

    public void setVotedOn(Vote votedOn) {
        this.votedOn = votedOn;
    }

    public Type[] getTypes() {
        if(typeIds.isEmpty()) {
            return new Type[0];
        }

        List<Type> typeList = new ArrayList<>();
        for(String id : typeIds.split(",")) {
            typeList.add(Type.getFromId(Integer.parseInt(id)));
        }

        return typeList.toArray(new Type[typeList.size()]);
    }

    public void setTypes(Type...types) {
        if(types == null) {
            typeIds = "";
            return;
        } else {
            int[] typeIdInt = new int[types.length];
            for(int i = 0; i < typeIdInt.length; i++) {
                typeIdInt[i] = Integer.parseInt(types[i].getId());
            }

            typeIds = getTypesString(typeIdInt);
        }
    }

    public Move[] getMoves() {
        if(moveIds.isEmpty()) {
            return new Move[0];
        }

        List<Move> moveList = new ArrayList<>();
        for(String id : moveIds.split(",")) {
            moveList.add(Move.getFromId(Integer.parseInt(id)));
        }

        return moveList.toArray(new Move[moveList.size()]);
    }

    public void setMoveIds(Move...moves) {
        if(moves == null) {
            moveIds = "";
            return;
        } else {
            int[] moveIdInt = new int[moves.length];
            for(int i = 0; i < moveIdInt.length; i++) {
                moveIdInt[i] = Integer.parseInt(moves[i].getId());
            }

            moveIds = getMovesString(moveIdInt);
        }
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }


}
