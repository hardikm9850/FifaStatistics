package com.example.kevin.fifastatistics.models.databasemodels.user;

import java.io.Serializable;

public interface Player extends Serializable {

    String getId();
    String getName();
    String getImageUrl();
    String getRegistrationToken();
    String getFavoriteTeamId();
}
