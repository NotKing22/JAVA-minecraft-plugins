package me.zsnow.stone.endergun.api;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberFormatAPI {
    private static final Pattern PATTERN = Pattern.compile("^(\\d+\\.?\\d*)(\\D+)");

    private final List<String> suffixes;

    public NumberFormatAPI() {
        this.suffixes = Arrays.asList("", "K", "M", "B", "T", "Q", "L");
    }

    public NumberFormatAPI(List<String> suffixes) {
        this.suffixes = suffixes;
    }

    public String formatNumber(double value) {
        int index = 0;

        double tmp;
        while ((tmp = value / 1000) >= 1) {
            value = tmp;
            ++index;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value) + this.suffixes.get(index);
    }

    public double parseString(String value) throws Exception {
        try {
            return Double.parseDouble(value);
        } catch (Exception ignored) {}

        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.find()) {
            throw new Exception("Invalid format");
        }

        double amount = Double.parseDouble(matcher.group(1));
        String suffix = matcher.group(2);

        int index = this.suffixes.indexOf(suffix.toUpperCase());

        return amount * Math.pow(1000, index);
    }

    // Test
    public static void main(String[] args) throws Exception {
        final NumberFormatAPI formatter = new NumberFormatAPI();

        System.out.println("Number -> String");
        System.out.println(formatter.formatNumber(100)); // 100
        System.out.println(formatter.formatNumber(1000)); // 1K
        System.out.println(formatter.formatNumber(10642)); // 10.64K
        System.out.println(formatter.formatNumber(10068)); // 10.07K
        System.out.println(formatter.formatNumber(493743)); // 493.74K
        System.out.println(formatter.formatNumber(6534817)); // 6.53M
        System.out.println(formatter.formatNumber(10000000)); // 10M
        System.out.println(formatter.formatNumber(Long.MAX_VALUE)); // 9.22L

        System.out.println("\n");

        System.out.println("String -> Number");
        System.out.println(formatter.parseString("100") + ""); // 100.0
        System.out.println(formatter.parseString("1k") + ""); // 1000.0
        System.out.println(formatter.parseString("10.64K") + ""); // 10640.0
        System.out.println(formatter.parseString("493.74K") + ""); // 493740.0
        System.out.println(formatter.parseString("6.53M") + ""); // 6530000.0
        System.out.println(formatter.parseString("10M") + ""); // 1.0E7

    }
}
