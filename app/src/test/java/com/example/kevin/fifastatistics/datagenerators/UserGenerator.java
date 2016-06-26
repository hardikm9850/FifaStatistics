package com.example.kevin.fifastatistics.datagenerators;

import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.MatchStub;
import com.example.kevin.fifastatistics.models.databasemodels.user.SeriesStub;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class UserGenerator {

    public static final int NULL_REQUESTS_LIST = 1;

    private static int[] requestIndexes = new int[5];
    private static Random rand;

    public static User generateUser() {
        return generateUser(null);
    }

    public static User generateUser(int... options) {
        setArgsArray(options);
        rand = new Random();
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
                .incomingRequests(getRequestsList())
                .outgoingRequests(getRequestsList())
                .recordStats(StatsGenerator.generateStatsPair())
                .averageStats(StatsGenerator.generateStatsPair())
                .matches(generateMatchList(id, rand.nextInt(5) + 1))
                .series(generateSeriesList(rand.nextInt(4) + 1))
                .matchRecords(RecordsGenerator.generateRecords(85))
                .seriesRecords(RecordsGenerator.generateRecords(22))
                .level(rand.nextInt(20))
                .experience(rand.nextInt(100000))
                .build();
    }

    private static void setArgsArray(int[] options) {
        if (options == null) {
            Arrays.fill(requestIndexes, 0);
            return;
        }
        for (int i : options) {
            requestIndexes[i] = 1;
        }
    }

    private static ArrayList<Friend> getRequestsList() {
        return (requestIndexes[NULL_REQUESTS_LIST] == 1) ? null : generateFriendsList(rand.nextInt(2));

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
