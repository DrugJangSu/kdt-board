package com.kdt.board;

public class TitlePolicy {

    private static final int MAX_TITLE_LENGTH = 20;

    public String check(String title) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title is blank");
        }

        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("title is long");
        }
        return "ok";
    }


}