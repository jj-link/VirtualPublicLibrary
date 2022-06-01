package com.revature.exceptions;

public class NullBookException extends Exception{
    public NullBookException(){
        super("Book not found");
    }
    public NullBookException(String message){
        super(message);
    }

}
