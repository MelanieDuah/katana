package com.katana.infrastructure.exceptions;

/**
 * Created by Akwasi Owusu on 11/16/17.
 */

public class KatanaBusinessException extends Exception {

    public KatanaBusinessException(String message) {
        super(message);
    }

    public KatanaBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
