package com.example.kevin.fifastatistics.datagenerators;

import java.util.Random;

import com.example.kevin.fifastatistics.models.user.Friend;

public class FriendGenerator {

    public static Friend generateFriend() {
        Random level = new Random();
        return Friend.builder()
                .id(IdGenerator.getRandomId())
                .name(NameGenerator.generateRandomFullName())
                .imageUrl(ImageUrlGenerator.generateValidImageUrl())
                .registrationToken(IdGenerator.getRandomIdWithDashes())
                .level(level.nextInt(30))
                .build();
    }
}
