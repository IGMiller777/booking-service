package com.igmiller.booking.cli;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class InputReaderTest {

    private InputReader readerOf(String text) {
        return new InputReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void readLine_shouldThrowInputClosed_whenInputIsEmpty() {
        assertThrows(InputClosedException.class, () -> readerOf("").readLine("> "));
    }

    @Test
    void readInt_shouldThrowInputClosed_whenInputEndsBeforeValidNumber() {
        // "abc" — невалидно, дальше ввода нет: раньше тут был NoSuchElementException
        assertThrows(InputClosedException.class, () -> readerOf("abc\n").readInt("> ", 0, 9));
    }

    @Test
    void readYesOrNo_shouldThrowInputClosed_whenInputEndsWithoutAnswer() {
        assertThrows(InputClosedException.class, () -> readerOf("maybe\n").readYesOrNo("> "));
    }

    @Test
    void readInt_shouldReturnFirstValidNumber() {
        assertEquals(7, readerOf("x\n42\n7\n").readInt("> ", 0, 9));
    }
}
