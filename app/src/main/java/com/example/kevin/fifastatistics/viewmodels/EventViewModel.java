package com.example.kevin.fifastatistics.viewmodels;

import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

public interface EventViewModel<T extends FifaEvent> {

    void setEvent(T event, Player user);
}
