package com.example.kevin.fifastatistics.managers;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;

public class CrestUrlResizer {

    private static final int CREST_SIZE_SMALL;
    private static final String SVG = ".svg";
    private static final String SIZE_REGEX = "\\d{2,4}?px";

    static {
        CREST_SIZE_SMALL = FifaApplication.getContext().getResources().getDimensionPixelSize(R.dimen.match_team_logo_size);
    }

    public static String resizeSmall(String crestUrl) {
        return resize(crestUrl, CREST_SIZE_SMALL);
    }

    private static String resize(String crestUrl, int size) {
        if (crestUrl != null) {
            return crestUrl.endsWith(SVG) ? crestUrl.replaceAll(SIZE_REGEX, size + "px") : crestUrl;
        } else {
            return null;
        }
    }
}
