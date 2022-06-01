package com.revature.exceptions;

public class ExistingUserException extends Exception {

    public ExistingUserException() { super("This user already exists"); }

    public ExistingUserException(String message) { super(message); }

}
