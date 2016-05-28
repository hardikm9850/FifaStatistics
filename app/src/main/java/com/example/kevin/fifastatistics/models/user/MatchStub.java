package com.example.kevin.fifastatistics.models.user;

import com.example.kevin.fifastatistics.models.match.Penalties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;

/**
 * <b>Class:</b> MatchStub <br><br>
 * <b>Description:</b> <br>
 * The MatchStub class is meant to act as a 'stub' of a Match, and should exist
 * only within instances of a User or Series object. It is used as the matches
 * associated with a user or Series object, and the related full Match object can be
 * accessed through the 'id' property. It defines only a few traits: <ul>
 * <li> <b>id</b>, a reference to the full Match object represented by this MatchStub
 * <li> <b>winnerId</b>, the ID of the winner of the match
 * <li> <b>date</b>, the date the match was on
 * <li> <b>goalsWinner</b>, the goals scored by the winner
 * <li> <b>goalsLoser</b>, the goals scored by the loser
 * <li> <b>won</b>, true if the user won, false otherwise
 * </ul>
 * @version 1.0
 * @author Kevin
 *
 */
@JsonDeserialize(builder = MatchStub.MatchStubBuilder.class)
@Builder
@Getter
public class MatchStub {

    private String matchId;
    private String winnerId;
    private String date;
    private int goalsWinner;
    private int goalsLoser;
    private Penalties penalties;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class MatchStubBuilder {
    }
}
