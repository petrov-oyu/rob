package ru.petrov.oleg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Solution1_2 {

    public static int solution1(String x) {
        if (x.length() >= 200) {
            return 0;
        }

        char[] mmsCake = x.toCharArray();
        int mmsPieceLength = 1;
        int pieces = 1;
        int mmsPieceLookUpPosition = 0;

        while (mmsPieceLength <= mmsCake.length/2 && pieces == 1) {

            for (int i = mmsPieceLength; i < mmsCake.length; i++) {
                if (mmsCake[i] != mmsCake[mmsPieceLookUpPosition]) {
                    break;
                }

                if (mmsPieceLength == mmsPieceLookUpPosition + 1) {
                    pieces++;
                    mmsPieceLookUpPosition = 0;
                    continue;
                }
                mmsPieceLookUpPosition++;
            }

            if (pieces != 1  && (mmsCake.length == mmsPieceLength * pieces)) {
                return pieces;
            }

            mmsPieceLength++;
            mmsPieceLookUpPosition = 0;
            pieces = 1;
        }

        return pieces;
    }

    public static int solution2(int[] l) {
        Arrays.sort(l);

        int sum = Arrays.stream(l).sum();
        int remainFromSum = sum % 3;
        boolean isDivisibleByThree = remainFromSum == 0;
        if (!isDivisibleByThree) {
            int[] digitsWithOneRemain = Arrays.stream(l).filter(value -> value % 3 == 1).limit(2).toArray();
            int[] digitsWithTwoRemain = Arrays.stream(l).filter(value -> value % 3 == 2).limit(2).toArray();

            if (remainFromSum == 1) {
                if (digitsWithOneRemain.length > 0) {
                    l = filterDigitsFromArray(l, digitsWithOneRemain[0]);
                    isDivisibleByThree = true;
                } else if (digitsWithTwoRemain.length > 1) {
                    l = filterDigitsFromArray(l, digitsWithTwoRemain[0], digitsWithTwoRemain[1]);
                    isDivisibleByThree = true;
                }
            } else if (remainFromSum == 2) {
                if (digitsWithTwoRemain.length > 0) {
                    l = filterDigitsFromArray(l, digitsWithTwoRemain[0]);
                    isDivisibleByThree = true;
                } else if (digitsWithOneRemain.length > 1) {
                    l = filterDigitsFromArray(l, digitsWithOneRemain[0], digitsWithOneRemain[1]);
                    isDivisibleByThree = true;
                }
            }
        }
        if (!isDivisibleByThree) {
            return 0;
        }

        //build number from digits
        int result = 0;
        for (int i = l.length - 1; i >= 0; i--) {
            result = result * 10 + l[i];
        }

        return result;
    }

    private static int[] filterDigitsFromArray(int[] array, int... digits) {
        final int flagForFilteredValues = -1;
        for (int i = 0; i < array.length; i++) {
            if (digits[0] == array[i]) {
                array[i] = flagForFilteredValues;
                digits[0] = flagForFilteredValues;
            } else if (digits.length > 1
                    && digits[1] == array[i]) {
                array[i] = flagForFilteredValues;
                digits[1] = flagForFilteredValues;
            }
        }

        return Arrays.stream(array)
                .filter(value -> value != -1)
                .toArray();
    }
}
