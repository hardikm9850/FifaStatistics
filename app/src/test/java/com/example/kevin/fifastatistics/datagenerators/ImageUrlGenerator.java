package com.example.kevin.fifastatistics.datagenerators;

import java.util.Random;

/**
 * Generates random Image URLs.
 */
public class ImageUrlGenerator {

    private static final String[] urls = {
            "http://a3.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTIwNjA4NjMzNzAzNzI4NjUy.jpg",
            "http://a1.files.biography.com/image/upload/c_fit,cs_srgb,dpr_1.0,h_1200,q_80,w_1200/MTE4MDAzNDEwNzQzMTY2NDc4.jpg",
            "https://pbs.twimg.com/profile_images/643807586396061696/FlHjhKtu.jpg",
            "https://i.ytimg.com/vi/rDxAd5pBNT8/hqdefault.jpg",
            "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRmTHpOImWS2wGUjSCn4mIeeTeG7UCj3PenbbhhLoKRCPsGGnQ7"
    };

    public static String generateValidImageUrl() {
        Random rand = new Random();
        return urls[rand.nextInt(urls.length)];
    }
}
