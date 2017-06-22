package com.example.jayti.geekon;

/**
 * Created by oshinmundada on 11/05/17.
 */

public class FinalScore {

    String user, opponent, score_user, score_opp;

    public FinalScore() {
    }

    public FinalScore(String user, String opponent, String score1, String score2) {
        this.user = user;
        this.opponent = opponent;
        this.score_user = score1;
        this.score_opp = score2;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getScore_user() {
        return score_user;
    }

    public void setScore_user(String score_user) {
        this.score_user = score_user;
    }

    public String getScore_opp() {
        return score_opp;
    }

    public void setScore_opp(String score2) {
        this.score_opp = score2;
    }

    @Override
    public String toString() {
        return "FinalScore{" +
                "user='" + user + '\'' +
                ", opponent='" + opponent + '\'' +
                ", score_user='" + score_user + '\'' +
                ", score_opp='" + score_opp + '\'' +
                '}';
    }
}
