package com.example.kevin.fifastatistics.managers;

import android.content.res.Resources;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;

public class CrestUrlResizer {

    private static final int CREST_SIZE_SMALL;
    private static final int CREST_SIZE_LARGE;
    private static final String SVG_PNG = ".svg.png";
    private static final String SIZE_REGEX = "\\d{2,4}?px";

    static {
        Resources resources = FifaApplication.getContext().getResources();
        CREST_SIZE_SMALL = resources.getDimensionPixelSize(R.dimen.match_team_logo_size);
        CREST_SIZE_LARGE = resources.getDimensionPixelSize(R.dimen.match_team_logo_size_large);
    }

    public static String resizeSmall(String crestUrl) {
        return resize(crestUrl, CREST_SIZE_SMALL);
    }

    public static String resizeLarge(String crestUrl) {
        return resize(crestUrl, CREST_SIZE_LARGE);
    }

    private static String resize(String crestUrl, int size) {
        if (crestUrl != null) {
            return crestUrl.endsWith(SVG_PNG) ? crestUrl.replaceAll(SIZE_REGEX, size + "px") : crestUrl;
        } else {
            return null;
        }
    }
}
