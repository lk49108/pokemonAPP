package com.example.leonardo.pokemonapp.network.resources;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by leonardo on 12/07/17.
 */
@JsonApi(type = "pokemons")
public class Pokemon extends Resource implements Parcelable {

    @Json(name = "image-url")
    private Uri imageSource;

    @Json(name = "name")
    private String name;

    @Json(name = "description")
    private String description;

    @Json(name = "height")
    private String height;

    @Json(name = "weight")
    private String weight;

    @Json(name = "category")
    private String category;

    @Json(name = "gender")
    private String abilities;

    public Pokemon() {
    }

    public Uri getImageSource() {
        return imageSource;
    }

    public void setImageSource(Uri imageSource) {
        this.imageSource = imageSource;
    }

    public Pokemon(String name, String description, String height, String weight, String category, String abilities, Uri imageSource) {
        this.name = name;
        this.description = description;
        this.height = height;
        this.weight = weight;
        this.category = category;
        this.abilities = abilities;
        this.imageSource = imageSource;

    }

    public void setName(String name) {
        this.name = name;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                name,
                description,
                height,
                weight,
                category,
                abilities,
                imageSource != null ? imageSource.toString() : null
        });
    }

    private Pokemon(Parcel in) {
        String[] data = new String[7];

        in.readStringArray(data);
        name = data[0];
        description = data[1];
        height = data[2];
        weight = data[3];
        category = data[4];
        abilities = data[5];
        imageSource = (data[6] == null) ? null : Uri.parse(data[6]);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };
}
