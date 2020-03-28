package com.lcbo.home;

public class MicroStrategyException extends Exception {

    public MicroStrategyException(String message) {super(message);}

    public MicroStrategyException (Exception e) {super(e);}
}
