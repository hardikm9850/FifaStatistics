package com.example.kevin.fifastatistics.User;

import java.util.ArrayList;

/**
 * <b>Class:</b> User <br><br>
 * <b>Description:</b> <br>
 * The User class defines a FIFA Statistics user. It contains all of the user's
 * properties and related items, such as name, level, experience, friends, stats,
 * matches, etc. <br>
 * There shall only ever be one instance of a user per googleId (two users
 * cannot be shared by a single Google account).
 *
 * @version 1.0
 * @author Kevin Grant
 *
 */
public class User {

    public String name;
    public String email;
    public String googleId;
    public String imageUrl;
    public ArrayList<Friend> friends;
    public ArrayList<Stats> records;
    public ArrayList<Stats> averages;
    public ArrayList<MatchStub> matches;
    public ArrayList<SeriesStub> series;
    public int matchWins;
    public int matchLosses;
    public int seriesWins;
    public int seriesLosses;
    public int level;
    public int experience;

    public User(String name, String email, String googleId, String imageUrl,
                ArrayList<Friend> friends, ArrayList<Stats> records,
                ArrayList<Stats> averages, ArrayList<MatchStub> matches,
                ArrayList<SeriesStub> series, int matchWins, int matchLosses,
                int seriesWins, int seriesLosses, int level, int experience)
    {
        this.name = name;
        this.email = email;
        this.googleId = googleId;
        this.imageUrl = imageUrl;
        this.friends = friends;
        this.records = records;
        this.averages = averages;
        this.matches = matches;
        this.series = series;
        this.matchWins = matchWins;
        this.matchLosses = matchLosses;
        this.seriesWins = seriesWins;
        this.seriesLosses = seriesLosses;
        this.level = level;
        this.experience = experience;
    }

    public User(String name, String email, String googleId, String imageUrl)
    {
        this.name = name;
        this.email = email;
        this.googleId = googleId;
        this.imageUrl = imageUrl;
    }

    private class Stats {

        public int goalsFor;
        public int goalsAgainst;
        public int shotsFor;
        public int shotsAgainst;
        public int shotsOnTarget;
        public int possession;
        public int tacklesFor;
        public int tacklesAgainst;
        public int fouls;
        public int redCards;
        public int offsides;
        public int shotAccuracy;
        public int passAccuracy;

        public Stats(
                int goalsFor, int goalsAgainst, int shotsFor,
                int shotsAgainst, int shotsOnTarget, int possession,
                int tacklesFor, int tacklesAgainst, int fouls,
                int redCards, int offsides, int shotAccuracy,
                int passAccuracy)
        {
            this.goalsFor = goalsFor;
            this.goalsAgainst = goalsAgainst;
            this.shotsFor = shotsFor;
            this.shotsAgainst = shotsAgainst;
            this.shotsOnTarget = shotsOnTarget;
            this.possession = possession;
            this.tacklesFor = tacklesFor;
            this.tacklesAgainst = tacklesAgainst;
            this.fouls = fouls;
            this.redCards = redCards;
            this.offsides = offsides;
            this.shotAccuracy = shotAccuracy;
            this.passAccuracy = passAccuracy;
        }
    }

    private class MatchStub {

        public String href;
        public String opponent;
        public String date;
        public int goalsFor;
        public int goalsAgainst;
        public boolean won;

        public MatchStub(String href, String opponent, String date,
                         int goalsFor, int goalsAgainst, boolean won) {
            this.href = href;
            this.opponent = opponent;
            this.date = date;
            this.goalsFor = goalsFor;
            this.goalsAgainst = goalsAgainst;
            this.won = won;
        }
    }

    private class SeriesStub {

        public String href;
        public String opponent;
        public String date;
        public ArrayList<MatchSummary> matches;
        public boolean win;

        public SeriesStub(String href, String opponent, String date,
                          ArrayList<MatchSummary> matches, boolean win) {
            this.href = href;
            this.opponent = opponent;
            this.date = date;
            this.matches = matches;
            this.win = win;
        }

        private class MatchSummary {

            public int goalsFor;
            public int goalsAgainst;
            public boolean win;

            public MatchSummary(int goalsFor, int goalsAgainst, boolean win) {
                this.goalsFor = goalsFor;
                this.goalsAgainst = goalsAgainst;
                this.win = win;
            }
        }
    }
}
