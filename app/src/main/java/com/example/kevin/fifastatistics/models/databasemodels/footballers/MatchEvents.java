package com.example.kevin.fifastatistics.models.databasemodels.footballers;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchEvents implements Serializable {

    private List<GoalItem> goals;
    private List<InjuryItem> injuries;
    private List<CardItem> cards;

    @JsonCreator
    public MatchEvents() {
        goals = new ArrayList<>();
        injuries = new ArrayList<>();
        cards = new ArrayList<>();
    }

    public void swapForWinner() {
        swapForWinnerFor(goals);
        swapForWinnerFor(cards);
        swapForWinnerFor(injuries);
    }

    private void swapForWinnerFor(List<? extends MatchEventItem> items) {
        if (items != null) {
            for (MatchEventItem item : items) {
                if (item != null) {
                    item.setForWinner(!item.isForWinner());
                }
            }
        }
    }

    @Getter
    @Setter
    public abstract static class MatchEventItem implements Serializable {
        private DummyPlayer player;
        private int minute;
        private boolean forWinner;

        @JsonIgnore
        public Integer getPlayerBaseId() {
            return player != null ? player.getBaseId() : null;
        }

        @JsonIgnore
        boolean isGoal() {
            return !(this instanceof CardItem) && !(this instanceof InjuryItem);
        }

        @JsonIgnore
        public boolean isGoldenGoal() {
            return isGoal() && minute > Match.MAX_SECOND_HALF_MINUTE;
        }

        @JsonIgnore
        public abstract int getIcon();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MatchEventItem that = (MatchEventItem) o;

            if (minute != that.minute) return false;
            if (forWinner != that.forWinner) return false;
            return player != null ? player.equals(that.player) : that.player == null;
        }

        @Override
        public int hashCode() {
            int result = player != null ? player.hashCode() : 0;
            result = 31 * result + minute;
            result = 31 * result + (forWinner ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            return SerializationUtils.toJson(this);
        }
    }

    @Getter
    @Setter
    public static class GoalItem extends MatchEventItem {
        private boolean ownGoal;

        @Override
        public int getIcon() {
            return 0;
        }
    }

    @Getter
    @Setter
    public static class InjuryItem extends MatchEventItem {
        private InjuryType type;

        @JsonIgnore
        @Override
        public int getIcon() {
            if (type == InjuryType.MINOR) {
                return R.drawable.icons8_bandage;
            } else if (type == InjuryType.MAJOR) {
                return R.drawable.icons8_hospital;
            } else {
                return R.drawable.icons8_ambulance;
            }
        }
    }

    @Getter
    @Setter
    public static class CardItem extends MatchEventItem {
        private CardType type;

        @JsonIgnore
        @Override
        public int getIcon() {
            if (type == CardType.YELLOW) {
                return R.drawable.ic_soccer_yellow_card;
            } else if (type == CardType.DIRECT_RED) {
                return R.drawable.ic_soccer_red_card;
            } else {
                return R.drawable.ic_soccer_yellow_red_card;
            }
        }
    }

    @Setter
    public static class DummyPlayer implements Serializable {
        private String headshotImgUrl;
        private String name;
        private int baseId;

        @JsonCreator
        public DummyPlayer() {}

        public DummyPlayer(Footballer player) {
            headshotImgUrl = player.getHeadshotImgUrl();
            name = player.getName();
            baseId = player.getBaseId();
        }

        public String getHeadshotImgUrl() {
            return headshotImgUrl;
        }

        public String getName() {
            return name;
        }

        public int getBaseId() {
            return baseId;
        }


        @JsonIgnore
        public String getInitials() {
            if (name != null) {
                StringTokenizer st = new StringTokenizer(name);
                if (st.countTokens() == 1) {
                    return st.nextToken().substring(0, 1);
                } else if (st.countTokens() == 2) {
                    return st.nextToken().substring(0, 1) + st.nextToken().substring(0, 1);
                }
            }
            return "-";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DummyPlayer that = (DummyPlayer) o;

            return baseId == that.baseId;
        }

        @Override
        public int hashCode() {
            return baseId;
        }
    }

    public enum InjuryType {
        MINOR, MAJOR, SEVERE
    }

    public enum CardType {
        YELLOW, YELLOW_RED, DIRECT_RED;
    }
}
