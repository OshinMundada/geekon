package com.example.jayti.geekon;

/**
 * Created by Jayti on 5/7/2017.
 */

public class User {
    String nickname, state, country, email;
    int totalPoints;


    public User() {
    }

    public User(String nickname, String email, String country, String state) {
        this.nickname = nickname;
        this.state = state;
        this.country = country;
        this.email = email;
        totalPoints=0;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                ", totalPoints=" + totalPoints +
                '}';
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

