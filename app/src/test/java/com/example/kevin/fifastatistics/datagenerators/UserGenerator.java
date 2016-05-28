package com.example.kevin.fifastatistics.datagenerators;

import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.models.user.MatchStub;
import com.example.kevin.fifastatistics.models.user.SeriesStub;
import com.example.kevin.fifastatistics.models.user.User;

import java.util.ArrayList;
import java.util.Random;

public class UserGenerator {

    public static User generateUser() {

        Random rand = new Random();
        String name = NameGenerator.generateRandomFullName();
        String id = IdGenerator.getRandomId();

        return User.builder()
                .id(id)
                .email(EmailGenerator.generateEmailFromNameWithNumbers(name))
                .googleId(IdGenerator.getRandomId())
                .name(name)
                .registrationToken(IdGenerator.getRandomIdWithDashes())
                .imageUrl(ImageUrlGenerator.generateValidImageUrl())
                .friends(generateFriendsList(rand.nextInt(10) + 1))
                .incomingRequests(generateFriendsList(rand.nextInt(3)))
                .outgoingRequests(generateFriendsList(rand.nextInt(2)))
                .records(StatsGenerator.generateStats())
                .averages(StatsGenerator.generateStats())
                .matches(generateMatchList(id, rand.nextInt(5)))
                .series(generateSeriesList(rand.nextInt(4) + 1))
                .matchWins(rand.nextInt(100))
                .matchLosses(rand.nextInt(100))
                .seriesWins(rand.nextInt(100))
                .seriesLosses(rand.nextInt(100))
                .level(rand.nextInt(20))
                .experience(rand.nextInt(100000))
                .build();
    }

    private static ArrayList<Friend> generateFriendsList(int count) {
        ArrayList<Friend> friends = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            friends.add(FriendGenerator.generateFriend());
        }

        return friends;
    }

    private static ArrayList<MatchStub> generateMatchList(String id, int count) {
        ArrayList<MatchStub> matches = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            matches.add(MatchStubGenerator.generateMatchStub(id));
        }

        return matches;
    }

    private static ArrayList<SeriesStub> generateSeriesList(int count) {
        ArrayList<SeriesStub> series = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            series.add(SeriesStubGenerator.generateSeriesStub());
        }

        return series;
    }
}
