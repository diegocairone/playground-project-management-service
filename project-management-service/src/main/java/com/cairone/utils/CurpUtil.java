package com.cairone.utils;

import com.cairone.vo.enums.GenderEnum;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class CurpUtil {

    private CurpUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static final String CURP_REGEX = "([A-Z][AEIOUX][A-Z]{2})(\\d{6})([HMX])([A-Z]{2})([^AEIOU]{3})(\\d{2})";

    public static final Pattern CURP_PATTERN = Pattern.compile(CURP_REGEX);

    public static boolean validateCurp(String curp) {
        return CURP_PATTERN.matcher(curp).matches();
    }

    public static GenderEnum getGenderFromCurp(String curp) {

        if (!validateCurp(curp)) {
            throw new IllegalArgumentException("Invalid CURP");
        }

        char genderChar = curp.charAt(10);
        return GenderEnum.of(genderChar);
    }

    public static LocalDate getBirthDateFromCurp(String curp) {

        if (!validateCurp(curp)) {
            throw new IllegalArgumentException("Invalid birth date from CURP");
        }

        String currentYear = String.valueOf(LocalDate.now().getYear()).substring(2);
        String year = curp.substring(4, 6);

        if (Integer.parseInt(year) < Integer.parseInt(currentYear) + 1) {
            year = "20" + year;
        } else {
            year = "19" + year;
        }

        String month = curp.substring(6, 8);
        String day = curp.substring(8, 10);
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }
}
