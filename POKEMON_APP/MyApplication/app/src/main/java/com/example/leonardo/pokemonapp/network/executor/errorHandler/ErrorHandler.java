package com.example.leonardo.pokemonapp.network.executor.errorHandler;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.ToJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import okhttp3.Response;

/**
 * Created by leonardo on 17/08/17.
 */

public class ErrorHandler {

    private Error[] errors;

    public ErrorHandler() {
        this.errors = new Error[0];
    }

    public ErrorHandler(Error[] errors) {
        this.errors = errors;
    }

    public Error[] getErrors() {
        return errors;
    }

    public static ErrorHandler getErrorResponse(retrofit2.Response response) {

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ErrorHandler> jsonAdapter = moshi.adapter(ErrorHandler.class);

        ErrorHandler errorHandler = null;
        try {
            String errorBody = response.errorBody().string();
            errorHandler = jsonAdapter.fromJson(errorBody);
        } catch (IOException e) {
            errorHandler = new ErrorHandler();
        }

        return errorHandler;
    }

    @Override
    public String toString() {
        if(errors.length == 0) {
            return "";
        }


        String returnString = "";
        final Set<String> addedErrors = new TreeSet<>();
        for(int i = 0; i < errors.length; i++) {
            String compoundErrorString = errors[i].toString() + "\n";
            if (!addedErrors.contains(compoundErrorString)) {
                returnString += compoundErrorString;
                addedErrors.add(compoundErrorString);
            }
        }

        if(returnString.contains("invalid email or password")) {
            return "invalid email or password";
        }

        if(returnString.contains("types ")) {
            returnString = returnString.replaceFirst("types ", "");
        }

        if(returnString.contains("moves")) {
            returnString = returnString.replaceFirst("moves ", "");
        }

        return returnString.trim();
    }

    public static class Error {
        private String title;
        private ErrorSource source;
        private String detail;

        public Error() {}

        public Error(ErrorSource source, String detail, String title) {
            this.source = source;
            this.detail = detail;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public ErrorSource getSource() {
            return source;
        }

        public String getDetail() {
            return detail;
        }

        @Override
        public String toString() {
            return (source == null ? title : source.toString()) + " " + (detail == null ? "" : detail);
        }
    }

    public static class ErrorSource {
        private String pointer;

        public ErrorSource() {}

        public ErrorSource(String pointer) {
            this.pointer = pointer;
        }

        public String getPointer() {
            return pointer;
        }

        @Override
        public String toString() {
            if(pointer == null) {
                return "";
            }

            return pointer.substring(pointer.lastIndexOf('/') + 1);
        }
    }

}
