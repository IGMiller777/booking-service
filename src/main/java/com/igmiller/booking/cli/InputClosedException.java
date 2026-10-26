package com.igmiller.booking.cli;

/**
 * Ввод закончился (EOF): stdin закрыт, перенаправлен из пустого файла или контейнер запущен без TTY.
 * Это не ошибка домена, а сигнал консоли завершить работу.
 */
final class InputClosedException extends RuntimeException {
    InputClosedException() {
        super("Console input is closed");
    }
}
