package com.kdt.board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TitlePolicyTest {

    @Test
    void closedTitleOk() {
        TitlePolicy policy = new TitlePolicy();
        assertEquals("ok", policy.check("closed"));
    }

    @Test
    void blankTitleIsRejected() {
        TitlePolicy policy = new TitlePolicy();
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class, () -> policy.check(""));
        assertEquals("title is blank", error.getMessage());
    }

    @Test
    void spaceOnlyIsBlank() {
        TitlePolicy policy = new TitlePolicy();
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class, () -> policy.check(" ")
        );
        assertEquals("title is blank", error.getMessage());
    }

    @Test
    void nullTitleIsBlank() {
        TitlePolicy policy = new TitlePolicy();
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> policy.check(null));
        assertEquals("title is blank", error.getMessage());
    }

    @Test
    void longTitleIsReject() {
        String title = "132565464564754574657";
        assertEquals(21, title.length());

        TitlePolicy policy = new TitlePolicy();
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> policy.check(title));

        assertEquals("title is long", error.getMessage());
    }

    @Test
    void nineteenLetterIsOk() {
        String title = "abcdefghijklmnopqrs";
        assertEquals(19, title.length());

        TitlePolicy policy = new TitlePolicy();
        assertEquals("ok", policy.check(title));
    }

    @Test
    void twentyLetterIsOk() {
        String title = "abcdefghijklmnopqrst";
        assertEquals(20, title.length());

        TitlePolicy policy = new TitlePolicy();
        assertEquals("ok", policy.check(title));
    }

    @Test
    void twentyOneSpacesPreferBlankError() {
        String title = "          " + "          " + " ";
        assertEquals(21, title.length());

        TitlePolicy policy = new TitlePolicy();
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> policy.check(title));

        assertEquals("title is blank", error.getMessage());

    }

    @Test
    void nineteenLetterAndSpaceIsOk() {
        String title = "abcdefghijklmnopqrs" + " ";
        assertEquals(20, title.length());

        TitlePolicy policy = new TitlePolicy();
        assertEquals("ok", policy.check(title));
    }

    @Test
    void twentyLetterAndSpaceIsLong() {
        String title = "abcdefghijklmnopqrst" + " ";
        assertEquals(21, title.length());

        TitlePolicy policy = new TitlePolicy();
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> policy.check(title));

        assertEquals("title is long", error.getMessage());
    }

}