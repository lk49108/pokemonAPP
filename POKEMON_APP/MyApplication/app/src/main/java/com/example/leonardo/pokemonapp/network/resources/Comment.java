package com.example.leonardo.pokemonapp.network.resources;

import android.support.annotation.NonNull;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonQualifier;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by leonardo on 09/08/17.
 */
@JsonApi(type = "comments")
public class Comment extends Resource implements Serializable, Comparable<Comment> {

    @Json(name = "content")
    String content;

    @Json(name = "created-at")
    Date date;

    private HasOne<User> author;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getAuthor() {
        return author.get(getContext());
    }

    public void setAuthor(HasOne<User> author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(@NonNull Comment o) {
        return o.getDate().compareTo(date);
    }

}
