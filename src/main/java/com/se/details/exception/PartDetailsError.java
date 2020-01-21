package com.se.details.exception;

/**
 * PartDetailsError handle all custom errors returned by part details service
 */
public enum PartDetailsError {

    RESOURCE_NOT_FOUND("1001", "Resource not found"),
    INVALID_REQUEST_INPUTS("1002", "Invalid request inputs");

    private String errorNumber;
    private String errorMessage;

    PartDetailsError(String errorNumber, String errorMessage) {
        this.errorNumber = errorNumber;
        this.errorMessage = errorMessage;
    }

    public String getErrorNumber() {
        return errorNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


}
