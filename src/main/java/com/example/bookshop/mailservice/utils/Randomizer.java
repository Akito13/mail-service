package com.example.bookshop.mailservice.utils;

import java.util.Random;

public class Randomizer {
    public static String random() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
