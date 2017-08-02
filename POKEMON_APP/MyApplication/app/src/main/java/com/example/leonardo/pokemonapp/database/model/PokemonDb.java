package com.example.leonardo.pokemonapp.database.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.leonardo.pokemonapp.database.AppDatabase;
import com.example.leonardo.pokemonapp.database.model.converter.UriConverter;
import com.example.leonardo.pokemonapp.network.resources.Pokemon;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by leonardo on 26/07/17.
 */
@Table(database = AppDatabase.class)
public class PokemonDb extends BaseModel implements Parcelable {

    @PrimaryKey(autoincrement = true)
    private long id;

    @Column(typeConverter = UriConverter.class)
    private Uri imageSource;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String height;

    @Column
    private String weight;

    @Column
    private String category;

    @Column
    private String abilities;

    public PokemonDb() {}

    public PokemonDb(Uri imageSource, String name, String description, String height, String weight, String category, String abilities) {
        this.imageSource = imageSource;
        this.name = name;
        this.description = description;
        this.height = height;
        this.weight = weight;
        this.category = category;
        this.abilities = abilities;
    }

    public static PokemonDb fromPokemon(Pokemon pokemon) {
        return new PokemonDb(pokemon.getImageSource(),
                pokemon.getName(),
                pokemon.getDescription(),
                pokemon.getHeight(),
                pokemon.getWeight(),
                pokemon.getCategory(),
                pokemon.getAbilities());
    }

    public static PokemonDb[] fromPokemonArray(Pokemon[] pokemon) {
        PokemonDb[] pokemonsDb = new PokemonDb[pokemon.length];
        for(int i = 0; i < pokemonsDb.length; i++) {
            pokemonsDb[i] = fromPokemon(pokemon[i]);
        }

        return pokemonsDb;
    }

    protected PokemonDb(Parcel in) {
        imageSource = (Uri) in.readValue(Uri.class.getClassLoader());
        name = in.readString();
        description = in.readString();
        height = in.readString();
        weight = in.readString();
        category = in.readString();
        abilities = in.readString();
    }

    public static PokemonDb[] fromPokemonList(List<Pokemon> pokemons) {
        PokemonDb[] pokemonsDb = new PokemonDb[pokemons.size()];
        for(int i = 0; i < pokemonsDb.length; i++) {
            pokemonsDb[i] = fromPokemon(pokemons.get(i));
        }

        return pokemonsDb;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(imageSource);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeString(category);
        dest.writeString(abilities);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {

        return id;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PokemonDb> CREATOR = new Parcelable.Creator<PokemonDb>() {
        @Override
        public PokemonDb createFromParcel(Parcel in) {
            return new PokemonDb(in);
        }

        @Override
        public PokemonDb[] newArray(int size) {
            return new PokemonDb[size];
        }
    };

    public Uri getImageSource() {
        return imageSource;
    }

    public void setImageSource(Uri imageSource) {
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAbilities() {
        return abilities;
    }

    public void setAbilities(String abilities) {
        this.abilities = abilities;
    }

}
