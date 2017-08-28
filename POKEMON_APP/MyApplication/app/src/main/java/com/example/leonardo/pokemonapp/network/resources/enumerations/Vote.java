package com.example.leonardo.pokemonapp.network.resources.enumerations;

import java.io.Serializable;

/**
 * Created by leonardo on 10/08/17.
 */

public enum Vote implements Serializable {

    DISLIKE(-1),
    LIKE(1),
    NEUTRAL(0);

    private int voteId;

    private Vote(int voteId) {
        this.voteId = voteId;
    }

    public int getVoteId() {
        return voteId;
    }

    public static Vote getFromId(int voteId) {
        switch(voteId) {
            case -1:
                return DISLIKE;
            case 1:
                return LIKE;
            default:
                return NEUTRAL;
        }
    }
}
