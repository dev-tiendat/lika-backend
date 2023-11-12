package com.app.lika.utils;

public class TimeUtils {

    public static long convertMinutesToSeconds(int minutes) {
        return minutes * 60L;
    }

    // Chuyển đổi giây thành phút
    public static int convertSecondsToMinutes(long seconds) {
        return (int) (seconds / 60L); // 60 giây trong một phút
    }

    public static long convertMinutesToMilliseconds(int minutes) {
        return minutes * 60L * 1000L; // 60 giây * 1000 ms trong một giây
    }
}