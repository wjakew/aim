/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.maintanance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Object for creating random strings of password
 */
public class RandomWordGeneratorEngine {

    public ArrayList<String> history;

    /**
     * Constructor
     */
    public RandomWordGeneratorEngine(){
        history = new ArrayList<>();
    }

    /**
     * Object for generating random string based on object data
     * @return String
     */
    public  String generateRandomString(int numberOfSigns, boolean useDigits, boolean usePunctuation) {
        // Utwórz zbiory znaków, które zostaną wykorzystane do wygenerowania ciągu.
        Set<Character> characters = new HashSet<>();
        for (char c = 'a'; c <= 'z'; c++) {
            characters.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            characters.add(c);
        }
        if (useDigits) {
            for (char c = '0'; c <= '9'; c++) {
                characters.add(c);
            }
        }
        if (usePunctuation) {
            characters.add(',');
            characters.add('.');
            characters.add('!');
            characters.add('?');
            characters.add('#');
            characters.add('%');
            characters.add('@');
            characters.add('(');
            characters.add('/');
            characters.add('\\');
            characters.add('{');
            characters.add('}');
        }

        // Wygeneruj ciąg znaków.
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numberOfSigns; i++) {
            stringBuilder.append(characters.toArray()[new Random().nextInt(characters.size())]);
        }
        history.add(LocalDateTime.now().toString()+"#"+stringBuilder.toString());
        return stringBuilder.toString();
    }

}