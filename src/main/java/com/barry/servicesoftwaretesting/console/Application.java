package com.barry.servicesoftwaretesting.console;

public class Application {

    public static void main(String[] args) {

        String name = "BARRY";
        String nameTwo = "BARRY";

        earlyCheck(nameTwo, name);
    }

    private static void earlyCheck(String nameTwo, String name) {
        if (nameTwo.equals(name)) {
            return;  // method exit
        } else {
            System.out.println("It OK !!!");
        }
        throw new IllegalArgumentException("VAR !!");
    }
}
