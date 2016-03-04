package com.example.kevin.fifastatistics.user;

/**
 * <b>Class:</b> Stats <br><br>
 * <b>Description:</b> <br>
 * The Stats class represents all of the User's main statistics, and should only exist
 * within instances of User objects. All of its properties are ints.
 * It defines:<ul>
 * <li>goalsFor
 * <li>goalsAgainst
 * <li>shotsFor
 * <li>shotsAgainst
 * <li>shotsOnTarget
 * <li>possession
 * <li>tacklesFor
 * <li>tacklesAgainst
 * <li>fouls
 * <li>redCards
 * <li>offsides
 * <li>shotAccuracy
 * <li>passAccuracy
 * </ul>
 * @version 1.0
 * @author Kevin Grant
 *
 */
public class Stats {

    private int goalsFor;
    private int goalsAgainst;
    private int shotsFor;
    private int shotsAgainst;
    private int shotsOnTarget;
    private int possession;
    private int tacklesFor;
    private int tacklesAgainst;
    private int fouls;
    private int redCards;
    private int offsides;
    private int shotAccuracy;
    private int passAccuracy;

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

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getShotsFor() {
        return shotsFor;
    }

    public void setShotsFor(int shotsFor) {
        this.shotsFor = shotsFor;
    }

    public int getShotsAgainst() {
        return shotsAgainst;
    }

    public void setShotsAgainst(int shotsAgainst) {
        this.shotsAgainst = shotsAgainst;
    }

    public int getShotsOnTarget() {
        return shotsOnTarget;
    }

    public void setShotsOnTarget(int shotsOnTarget) {
        this.shotsOnTarget = shotsOnTarget;
    }

    public int getPossession() {
        return possession;
    }

    public void setPossession(int possession) {
        this.possession = possession;
    }

    public int getTacklesFor() {
        return tacklesFor;
    }

    public void setTacklesFor(int tacklesFor) {
        this.tacklesFor = tacklesFor;
    }

    public int getTacklesAgainst() {
        return tacklesAgainst;
    }

    public void setTacklesAgainst(int tacklesAgainst) {
        this.tacklesAgainst = tacklesAgainst;
    }

    public int getFouls() {
        return fouls;
    }

    public void setFouls(int fouls) {
        this.fouls = fouls;
    }

    public int getRedCards() {
        return redCards;
    }

    public void setRedCards(int redCards) {
        this.redCards = redCards;
    }

    public int getOffsides() {
        return offsides;
    }

    public void setOffsides(int offsides) {
        this.offsides = offsides;
    }

    public int getShotAccuracy() {
        return shotAccuracy;
    }

    public void setShotAccuracy(int shotAccuracy) {
        this.shotAccuracy = shotAccuracy;
    }

    public int getPassAccuracy() {
        return passAccuracy;
    }

    public void setPassAccuracy(int passAccuracy) {
        this.passAccuracy = passAccuracy;
    }
}
