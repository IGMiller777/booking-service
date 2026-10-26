package com.igmiller.booking.cli;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class ConsoleMenuTest {

    @Test
    void run_shouldExitQuietly_whenInputIsClosed() {
        // Пустой stdin — как в Docker без TTY или при запуске "< /dev/null".
        // Сервисы не нужны: до них дело не доходит.
        InputReader closedInput = new InputReader(new ByteArrayInputStream(new byte[0]));
        ConsoleMenu menu = new ConsoleMenu(closedInput, null, null, null, null, null);

        assertTimeoutPreemptively(Duration.ofSeconds(2), menu::run);
    }
}
