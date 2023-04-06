package com.mct.base.demo.utils;

import android.graphics.Color;

import java.util.Random;

public class Utils {
    public static int generateBrightColor() {
        Random random = new Random();
        // Generate a random bright color by setting the red, green, and blue components to values between 128 and 255
        int red = random.nextInt(128) + 128;
        int green = random.nextInt(128) + 128;
        int blue = random.nextInt(128) + 128;
        return Color.rgb(red, green, blue);
    }
}
