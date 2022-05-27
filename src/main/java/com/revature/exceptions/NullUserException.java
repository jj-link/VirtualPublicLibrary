package com.revature.exceptions;

public class NullUserException extends Exception{
    public NullUserException(){
        super("User not found");
    }
    public NullUserException(String message){
        super(message);
    }


}
