package com.example.FlightService.Exceptions;


public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException() { super(); }
    public FlightNotFoundException(String message) { super(message); }
    public FlightNotFoundException(String message, Throwable cause) { super(message, cause); }
}

