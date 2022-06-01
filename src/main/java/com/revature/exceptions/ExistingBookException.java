package com.revature.exceptions;

public class ExistingBookException extends Exception {

    public ExistingBookException() { super("This book already exists"); }

    public ExistingBookException(String message) { super(message); }
}
