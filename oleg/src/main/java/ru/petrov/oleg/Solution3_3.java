package ru.petrov.oleg;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * https://en.wikipedia.org/wiki/Absorbing_Markov_chain
 * https://dev.to/legolord208/absorbing-markov-chains-how-do-they-work-46
 * https://github.com/rchen8/algorithms/blob/master/Matrix.java
 *
 * https://www.geeksforgeeks.org/adjoint-inverse-matrix/
 *
 *
 * https://github.com/rchen8/algorithms/blob/master/Matrix.java
 * https://github.com/mkutny/absorbing-markov-chains/blob/f257a4034dc959b50984cacb059801e9b285e5e6/amc.py#L20
 */
public class Solution3_3 {

    public static int[] solution(int[][] m) {
        if(m[0][0]==0 && m.length==1) {
            return new int[]{1, 1};
        }

        Fraction[][] probabilitiesMatrix = convertToProbabilitiesMatrix(m);
        List<Integer> t = getTransientRows(m);
        Fraction[][] q = convertToQ(probabilitiesMatrix, t);
        Fraction[][] r = convertToR(probabilitiesMatrix, t);
        Fraction[][] i = createIdentity(q.length);
        Fraction[][] subtract = subtract(i, q);
        Fraction[][] inverse = inverse(subtract);
        Fraction[][] multiply = multiply(inverse, r);
        List<Fraction> resultFractions = Fraction.convertToCommonDenominator(multiply[0]);
        long denominator = resultFractions.stream()
                .filter(f -> !f.isZero())
                .map(f -> f.denominator)
                .findAny()
                    .orElse(0L);
        List<Long> result = resultFractions.stream()
                .map(f -> f.numerator)
                .collect(Collectors.toList());
        result.add(denominator);
        return result.stream().mapToInt(Long::intValue).toArray();
    }

    private static Fraction[][] multiply(Fraction[][] a, Fraction[][] b) {
        int rows = a.length;
        int columns = b[0].length;
        int iters = a[0].length;

        Fraction[][] multiply = new Fraction[rows][columns];

        for (int row = 0; row < rows; row++) {
            Fraction[] multiplyRow = new Fraction[columns];
            for (int column = 0; column < columns; column++) {
                Fraction sum = Fraction.zero();
                for (int i = 0; i < iters; i++) {
                    sum = sum.add(a[row][i].multiply(b[i][column]));
                }
                multiplyRow[column] = sum;
            }
            multiply[row] = multiplyRow;
        }

        return multiply;
    }

    private static Fraction[][] inverse(Fraction[][] matrix) {
        Fraction determinant = getMatrixDeterminant(matrix);
        if (determinant.isZero()) {
            Fraction[][] inverse = new Fraction[matrix.length][matrix[0].length];
            for (int row = 0; row < matrix.length; row++) {
                for (int column = 0; column < matrix[0].length; column++) {
                    inverse[row][column] = Fraction.zero();
                }
            }
            return inverse;
        }

        //base case 2x2
        if (matrix.length == 2) {
            Fraction[] firstRow = new Fraction[]{matrix[1][1].divide(determinant), matrix[0][1].divide(determinant).negate()};
            Fraction[] secondRow = new Fraction[]{matrix[1][0].divide(determinant).negate(), matrix[0][0].divide(determinant)};
            return new Fraction[][]{firstRow, secondRow};
        }

        Fraction[][] cofactors = new Fraction[matrix.length][matrix[0].length];
        for (int row = 0; row < matrix.length; row++) {
            Fraction[] cofactorRow = new Fraction[matrix[0].length];
            for (int column = 0; column < matrix[0].length; column++) {
                Fraction[][] minor = getMatrixMinor(matrix, row, column);
                Fraction minorDeterminant = getMatrixDeterminant(minor);
                cofactorRow[column] = (row + column) % 2 == 0 ? minorDeterminant : minorDeterminant.negate();
            }
            cofactors[row] = cofactorRow;
        }

        cofactors = transposeMatrix(cofactors);
        for (int row = 0; row < cofactors.length; row++) {
            for (int column = 0; column < cofactors[0].length; column++) {
                cofactors[row][column] = cofactors[row][column].divide(determinant);
            }
        }

        return cofactors;
    }

    private static Fraction[][] transposeMatrix(Fraction[][] matrix) {
        Fraction[][] transpose = new Fraction[matrix.length][matrix[0].length];
        for (int row = 0; row < matrix.length; row++) {
            Fraction[] transposeRow = new Fraction[matrix[0].length];
            for (int column = 0; column < matrix[0].length; column++) {
                if (column == row) {
                    transposeRow[column] = matrix[row][column];
                } else {
                    transposeRow[column] = matrix[column][row];
                }
            }
            transpose[row] = transposeRow;
        }

        return transpose;
    }

    private static Fraction getMatrixDeterminant(Fraction[][] matrix) {
        //base case 2x2
        if (matrix.length == 2) {
            return matrix[0][0].multiply(matrix[1][1])
                    .subtract(matrix[0][1]
                            .multiply(matrix[1][0]));
        }

        Fraction result = Fraction.zero();
        for (int col = 0; col < matrix[0].length; col++) {
            Fraction fractionFirst = (col + 1) % 2 == 0 ? matrix[0][col] : matrix[0][col].negate();
            Fraction fractionSecond = getMatrixDeterminant(getMatrixMinor(matrix, 0, col));
            result = result.add(fractionFirst
                            .multiply(fractionSecond));
        }
        return result;
    }

    private static Fraction[][] getMatrixMinor(Fraction[][] matrix, int row, int column) {
        Fraction[][] minor = new Fraction[matrix.length - 1][matrix[0].length - 1];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; i != row && j < matrix[i].length; j++)
                if (j != column)
                    minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
        return minor;
    }

    /**
     * subtract two matrix
     */
    private static Fraction[][] subtract(Fraction[][] i, Fraction[][] q) {
        if (i.length != i[0].length || q.length != q[0].length) {
            throw new RuntimeException("non-square matrices");
        }
        if (i.length != q.length) {
            throw new RuntimeException("Cannot subtract matrices of different sizes");
        }

        Fraction[][] result = new Fraction[i.length][i[0].length];
        for (int row = 0; row < i.length; row++) {
            for (int column = 0; column < i[0].length; column++) {
                result[row][column] = i[row][column].subtract(q[row][column]);
            }
        }

        return result;
    }

    /**
     * create identity matrix of size `t`
     */
    private static Fraction[][] createIdentity(int size) {
        Fraction[][] i = new Fraction[size][size];
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                i[row][column] = row == column ? Fraction.one() : Fraction.zero();
            }
        }

        return i;
    }

    /**
     * decompose input matrix `p` on Q (t-by-t) component. `t` is the number of transient states
     */
    private static Fraction[][] convertToQ(Fraction[][] p, List<Integer> transientRows) {
        Fraction[][] q = new Fraction[transientRows.size()][transientRows.size()];
        for (int row = 0; row < transientRows.size(); row++) {
            for (int column = 0; column < transientRows.size(); column++) {
                q[row][column] = p[transientRows.get(row)][transientRows.get(column)];
            }
        }
        return q;
    }

    /**
     * decompose input matrix `p` on R (t-by-r) component. `t` is the number of transient states
     */
    private static Fraction[][] convertToR(Fraction[][] p, List<Integer> transientRows) {
        List<Integer> terminalRows = IntStream.range(0, p.length)
                .filter(v -> !transientRows.contains(v))
                .boxed()
                .collect(Collectors.toList());

        Fraction[][] r = new Fraction[transientRows.size()][terminalRows.size()];
        for (int row = 0; row < transientRows.size(); row++) {
            for (int column = 0; column < terminalRows.size(); column++) {
                r[row][column] = p[transientRows.get(row)][terminalRows.get(column)];
            }

        }
        return r;
    }

    /**
     * get transient states rows
     */
    private static List<Integer> getTransientRows(int[][] m) {
        List<Integer> transientRows = new ArrayList<>();
        for (int row = 0; row < m.length; row++) {
            if (Arrays.stream(m[row]).anyMatch(v -> v != 0)) {
                transientRows.add(row);
            }
        }

        return transientRows;
    }

    private static Fraction[][] convertToProbabilitiesMatrix(int[][] m) {
        Fraction[][] probabilities = new Fraction[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            int sum = 0;
            Fraction[] row = Stream.generate(Fraction::zero)
                    .limit(m.length)
                    .toArray(Fraction[]::new);
            for (int j = 0; j < m[0].length; j++) {
                sum += m[i][j];
            }
            if (sum == 0) {
                for (int j = 0; j < m[0].length; j++) {
                    if (j == i) {
                        row[j] = Fraction.one();
                    }
                }
            } else {
                for (int j = 0; j < m[0].length; j++) {
                    if (m[i][j] != 0) {
                        row[j] = new Fraction(m[i][j], sum);
                    }
                }
            }
            probabilities[i] = row;
        }
        return probabilities;
    }


    static class Fraction {
        private static final Fraction ONE = new Fraction(1, 1);
        private static final Fraction ZERO = new Fraction(0, 1);
        private final long numerator;
        private final long denominator;

        Fraction(long numerator, long denominator) {
            if (denominator == 0) {
                throw new RuntimeException("denominator cannot be zero");
            }
            if (numerator == 0) {
                denominator = 1;
            }
            if (numerator < 0 && denominator < 0) {
                numerator = Math.abs(numerator);
                denominator = Math.abs(denominator);
            } else if (denominator < 0) {
                numerator = -1 * numerator;
                denominator = Math.abs(denominator);
            }

            this.numerator = numerator;
            this.denominator = Math.abs(denominator);
        }

        public static Fraction one() {
            return ONE;
        }

        public static Fraction zero() {
            return ZERO;
        }

        private boolean isZero() {
            return this.numerator == 0;
        }

        public Fraction negate() {
            if (this.isZero()) {
                return Fraction.zero();
            }
            return new Fraction(-1 * this.numerator, this.denominator);
        }

        public Fraction add(Fraction fraction) {
            if (fraction.isZero()) {
                return this;
            }
            if (this.isZero()) {
                return fraction;
            }
            if (this.denominator == fraction.denominator) {
                return new Fraction(this.numerator + fraction.numerator, this.denominator);
            }
            List<Fraction> fractions = Fraction.convertToCommonDenominator(this, fraction);
            return new Fraction(fractions.get(0).numerator + fractions.get(1).numerator,
                    fractions.get(0).denominator);
        }

        public Fraction divide(Fraction fraction) {
            if (fraction.isZero()) {
                throw new RuntimeException("cannot divide by zero");
            }
            if (this.isZero()) {
                return this;
            }
            long n = this.numerator * fraction.denominator;
            long d = this.denominator * fraction.numerator;
            long commonDivisor = greatestCommonDivisor(n, d);
            return new Fraction(n/commonDivisor, d/commonDivisor);
        }

        public Fraction multiply(Fraction fraction) {
            if (this.isZero() || fraction.isZero()) {
                return zero();
            }
            long n = this.numerator * fraction.numerator;
            long d = this.denominator * fraction.denominator;
            long commonDivisor = greatestCommonDivisor(n, d);
            return new Fraction(n/commonDivisor, d/commonDivisor);
        }

        public Fraction subtract(Fraction fraction) {
            if (this.isZero() && fraction.isZero()) {
                return Fraction.zero();
            }
            if (this.isZero()) {
                return new Fraction(-1 * fraction.numerator, fraction.denominator);
            }
            if (fraction.isZero()) {
                return this;
            }
            if (this.denominator == fraction.denominator) {
                return new Fraction(this.numerator - fraction.numerator, this.denominator);
            }

            List<Fraction> fractions = Fraction.convertToCommonDenominator(this, fraction);
            return new Fraction(fractions.get(0).numerator - fractions.get(1).numerator, fractions.get(0).denominator);
        }

        private static List<Fraction> convertToCommonDenominator(Fraction[] fractionsArray) {
            return convertToCommonDenominator(Arrays.stream(fractionsArray).collect(Collectors.toList()));
        }

        private static List<Fraction> convertToCommonDenominator(Fraction fractionFirst, Fraction fractionSecond) {
            List<Fraction> fractions = new ArrayList<>();
            fractions.add(fractionFirst);
            fractions.add(fractionSecond);
            return convertToCommonDenominator(fractions);
        }

        private static List<Fraction> convertToCommonDenominator(List<Fraction> fractions) {
            fractions = fractions.stream()
                    .map(f -> {
                        if (f.isZero()) {
                            return f;
                        }
                        long divisor = greatestCommonDivisor(f.numerator, f.denominator);
                        return new Fraction(f.numerator/divisor, f.denominator/divisor);
                    })
                    .collect(Collectors.toList());

            List<Long> denominators = fractions.stream()
                    .filter(f -> !f.isZero())
                    .map(f -> Math.abs(f.denominator))
                    .sorted()
                    .distinct()
                    .collect(Collectors.toList());
            while (denominators.size() > 1) {
                if (denominators.get(denominators.size() - 1) % denominators.get(0) == 0) {
                    fractions.replaceAll(f -> {
                        if (f.denominator == denominators.get(0)) {
                            return new Fraction(f.numerator * denominators.get(denominators.size() - 1) / denominators.get(0),
                                    denominators.get(denominators.size() - 1));
                        }
                        return f;
                    });
                    denominators.remove(0);
                } else {
                    fractions.replaceAll(f -> {
                        if (f.denominator == denominators.get(0)) {
                            return new Fraction(f.numerator * denominators.get(denominators.size() - 1),
                                    denominators.get(0) * denominators.get(denominators.size() - 1));
                        } else if (f.denominator == denominators.get(denominators.size() - 1)) {
                            return new Fraction(f.numerator * denominators.get(0),
                                    denominators.get(0) * denominators.get(denominators.size() - 1));

                        }
                        return f;
                    });

                    long min = denominators.get(0);
                    long max = denominators.get(denominators.size() - 1);
                    denominators.remove(0);
                    denominators.remove(denominators.size() - 1);
                    denominators.add(min * max);
                }
            }

            return fractions;
        }

        public static long greatestCommonDivisor(long a, long b) {
            return b==0 ? a : greatestCommonDivisor(b, a%b);
        }

        @Override
        public String toString() {
            return numerator +
                    "/" + denominator;
        }
    }
}