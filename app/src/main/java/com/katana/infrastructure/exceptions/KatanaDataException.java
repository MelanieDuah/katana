package com.katana.infrastructure.exceptions;

/**
 * Created by Akwasi Owusu on 11/16/17.
 */

public class KatanaDataException extends Exception {

    public KatanaDataException(String message) {
        super(message);
    }

    public KatanaDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
