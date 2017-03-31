package com.example.kevin.fifastatistics.models.patches;

public class UserTeamPatch implements Patch {

    public final String favoriteTeamId;

    public UserTeamPatch(String favoriteTeamId) {
        this.favoriteTeamId = favoriteTeamId;
    }
}
