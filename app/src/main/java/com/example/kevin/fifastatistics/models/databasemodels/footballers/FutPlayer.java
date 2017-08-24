package com.example.kevin.fifastatistics.models.databasemodels.footballers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class FutPlayer implements Serializable {

    private String id;
    private String commonName;
    private String firstName;
    private String headshotImgUrl;
    private String lastName;
    private String name;
    private Position position;
    private String birthdate;
    private String foot;
    private Nation nation;
    private Club club;
    private List<Attribute> attributes;
    private Quality quality;
    private int baseId;
    private int rating;
    private int height;
    private int weight;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class ImageUrls implements Serializable {
        private String small;
        private String medium;
        private String large;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Nation implements Serializable {
        private ImageUrls imageUrls;
        private String abbrName;
        private String name;
        private int id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Club implements Serializable {
        private ClubImageUrls imageUrls;
        private String abbrName;
        private String name;
        private int id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class ClubImageUrls implements Serializable {
        private ImageUrls dark;
        private ImageUrls normal;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Attribute implements Serializable {
        private String name;
        private int value;
        private List<Integer> chemistryBonus;
    }

    public enum Position {
        LB, RB, CB, LWB, RWB, CM, CDM, CAM, LM, RM, LW, RW, ST, GK, LF, CF, RF
    }

    public enum Quality {
        gold, silver, bronze
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FutPlayer futPlayer = (FutPlayer) o;

        return baseId == futPlayer.baseId;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + baseId;
        result = 31 * result + (commonName != null ? commonName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (headshotImgUrl != null ? headshotImgUrl.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (birthdate != null ? birthdate.hashCode() : 0);
        result = 31 * result + (foot != null ? foot.hashCode() : 0);
        result = 31 * result + (quality != null ? quality.hashCode() : 0);
        result = 31 * result + rating;
        result = 31 * result + height;
        result = 31 * result + weight;
        return Math.abs(result);
    }
}