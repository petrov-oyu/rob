package ru.petrov.oleg;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Verifying solution...
 * Test 1 passed!
 * Test 2 failed
 * Test 3 failed  [Hidden]
 * Test 4 failed  [Hidden]
 * Test 5 failed  [Hidden]
 * Test 6 failed  [Hidden]
 * Test 7 failed  [Hidden]
 * Test 8 failed  [Hidden]
 * Test 9 failed  [Hidden]
 * Test 10 failed  [Hidden]
 *
 * чушь - https://rajat19.github.io/foobar/doomsday-fuel
 *
 * http://neerc.ifmo.ru/wiki/index.php?title=%D0%A0%D0%B0%D1%81%D1%87%D1%91%D1%82_%D0%B2%D0%B5%D1%80%D0%BE%D1%8F%D1%82%D0%BD%D0%BE%D1%81%D1%82%D0%B8_%D0%BF%D0%BE%D0%B3%D0%BB%D0%BE%D1%89%D0%B5%D0%BD%D0%B8%D1%8F_%D0%B2_%D1%81%D0%BE%D1%81%D1%82%D0%BE%D1%8F%D0%BD%D0%B8%D0%B8
 * https://neerc.ifmo.ru/wiki/index.php?title=%D0%A4%D1%83%D0%BD%D0%B4%D0%B0%D0%BC%D0%B5%D0%BD%D1%82%D0%B0%D0%BB%D1%8C%D0%BD%D0%B0%D1%8F_%D0%BC%D0%B0%D1%82%D1%80%D0%B8%D1%86%D0%B0
 */
public class Solution3_3_part1 {

    public static int[] solution(int[][] m) {
        int[] denominators = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            int sum = 0;
            for (int j = 0; j < m.length; j++) {
                sum += m[i][j];
            }
            denominators[i] = sum;
        }

        List<Fraction> fractions = new ArrayList<>();
        for (int row = m.length - 1; row > 1; row--) {
            fractions.add(rev(m, denominators, row));
        }

        int max = fractions.stream()
                .filter(f -> f.denominator != 0)
                .mapToInt(f -> f.denominator)
                .max().getAsInt();
        int min = fractions.stream()
                .filter(f -> f.denominator != 0)
                .mapToInt(f -> f.denominator)
                .min().getAsInt();
        int resultDenominator = max;
        List<Integer> result;
        if (max % min == 0) {
            int multiplication = max / min;
            result = fractions.stream()
                    .map(f -> {
                        if (f.isZero()) {
                            return 0;
                        }
                        if (f.denominator == min) {
                            return f.numerator * multiplication;
                        } else {
                            return f.numerator;
                        }
                    }).collect(Collectors.toList());
        } else {
            result = fractions.stream()
                    .map(f -> {
                        if (f.isZero()) {
                            return 0;
                        }
                        return f.numerator;
                    })
                    .collect(Collectors.toList());
        }
        Collections.reverse(result);
        result.add(resultDenominator);
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    public static Fraction rev(int[][] matrix, int[] denominator, int column) {
        Fraction fraction = Fraction.zero();
        for (int row = matrix.length - 1; row >= 0; row--) {
            if (row  == column) {
                continue;
            }
            if (matrix[row][column] != 0) {
                Fraction startFraction = new Fraction(matrix[row][column], denominator[row]);
                if (row != 0) {
                    Fraction innerFraction = rev(matrix, denominator, row);
                    startFraction = startFraction.multiply(innerFraction);
                }
                fraction = fraction.add(startFraction);
            }
        }

        return fraction;
    }

    static class Fraction {
        private static final Fraction ONE = new Fraction(1, 1);
        private static final Fraction ZERO = new Fraction(0, 0);
        int numerator;
        int denominator;

        Fraction(int numerator, int denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public static Fraction one() {
            return ONE;
        }

        public static Fraction zero() {
            return ZERO;
        }

        public Fraction add(Fraction fraction) {
            if (fraction.isZero()) {
                return this;
            }
            if (this.isZero()) {
                return fraction;
            }
            return new Fraction(numerator * fraction.denominator  + denominator * fraction.numerator,
                    denominator * fraction.denominator);
        }

        private boolean isZero() {
            return this.denominator == 0 || this.numerator == 0;
        }

        public Fraction multiply(Fraction fraction) {
            if (fraction.isZero()) {
                return zero();
            }
            if (this.isZero()) {
                return zero();
            }
            return new Fraction(numerator * fraction.numerator, denominator * fraction.denominator);
        }

        @Override
        public String toString() {
            return "Fraction{" +
                    "numerator=" + numerator +
                    ", denominator=" + denominator +
                    '}';
        }
    }
}
