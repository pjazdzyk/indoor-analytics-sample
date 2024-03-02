package com.synerset.indooranalytics.infrastructure.exceptionhandling;

public class IndoorAnalyticsInvalidArgumentException extends RuntimeException {
    public IndoorAnalyticsInvalidArgumentException(String message) {
        super(message);
    }
}
