package com.ssafy.kiriya.exception;

@SuppressWarnings("serial")
public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String msg) {
        super(msg);
    }
}
