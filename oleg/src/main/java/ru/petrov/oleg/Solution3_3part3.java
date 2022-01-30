package ru.petrov.oleg;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * Verifying solution...
 * Test 1 passed!
 * Test 2 passed!
 * Test 3 failed  [Hidden]
 * Test 4 failed  [Hidden]
 * Test 5 failed  [Hidden]
 * Test 6 failed  [Hidden]
 * Test 7 failed  [Hidden]
 * Test 8 failed  [Hidden]
 * Test 9 failed  [Hidden]
 * Test 10 failed  [Hidden]
 *
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
public class Solution3_3part3 {

    public static int[] solution(int[][] m) {
        Fraction[][] probabilitiesMatrix = convertToProbabilitiesMatrix(m);
        System.err.println("probabilitiesMatrix");
        print(probabilitiesMatrix);
        //sort
        int t = getTransientsStateQuantity(m);
        System.err.println("transient quantity = " + t);
        Fraction[][] q = convertToQ(probabilitiesMatrix, t);
        System.err.println("Q");
        print(q);
        Fraction[][] r = convertToR(probabilitiesMatrix, t);
        System.err.println("R");
        print(r);
        Fraction[][] i = createIdentity(q.length);
        System.err.println("I");
        print(i);
        Fraction[][] subtract = subtract(i, q);
        System.err.println("subtract");
        print(subtract);
        Fraction[][] inverse = inverse(subtract);
        System.err.println("inverse");
        print(inverse);
        Fraction[][] mulitply = mulitply(inverse, r);
        System.err.println("mulitply");
        print(mulitply);

        List<Fraction> resultFractions = Fraction.convertToCommonDenominator(mulitply[0]);
        int denominator = resultFractions.stream()
                .filter(f -> !f.isZero())
                .map(f -> f.denominator)
                .findAny()
                    .orElse(0);
        List<Integer> result = resultFractions.stream()
                .map(f -> f.numerator)
                .collect(Collectors.toList());
        result.add(denominator);
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    private static Fraction[][] mulitply(Fraction[][] a, Fraction[][] b) {
        int rows = a.length;
        int columns = b[0].length;
        int iters = a[0].length;

        Fraction[][] mulitply = new Fraction[rows][columns];

        for (int row = 0; row < rows; row++) {
            Fraction[] mulitplyRow = new Fraction[columns];
            for (int column = 0; column < columns; column++) {
                Fraction sum = Fraction.zero();
                for (int i = 0; i < iters; i++) {
                    sum = sum.add(a[row][i].multiply(b[i][column]));
                }
                mulitplyRow[column] = sum;
            }
            mulitply[row] = mulitplyRow;
        }

        return mulitply;
    }

    private static Fraction[][] inverse(Fraction[][] matrix) {
        Fraction determinant = getMatrixDeterminant(matrix);

        //base case 2x2
        if (matrix.length == 2) {
            Fraction[] firstRow = new Fraction[]{matrix[1][1].divide(determinant), matrix[0][1].divide(determinant).negate()};
            Fraction[] secondRow = new Fraction[]{matrix[1][0].divide(determinant).negate(), matrix[0][0].divide(determinant)};
            return new Fraction[][]{firstRow, secondRow};
        }

        Fraction[][] cofactors = new Fraction[matrix.length][matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            Fraction[] cofactorRow = new Fraction[matrix.length];
            for (int column = 0; column < matrix.length; column++) {
                Fraction[][] minor = getMatrixMinor(matrix, row, column);
                Fraction minorDeterminant = getMatrixDeterminant(minor);
                cofactorRow[column] = (row + column) % 2 == 0 ? minorDeterminant : minorDeterminant.negate();
            }
            cofactors[row] = cofactorRow;
        }

        cofactors = transposeMatrix(cofactors);
        for (int row = 0; row < cofactors.length; row++) {
            for (int column = 0; column < cofactors.length; column++) {
                cofactors[row][column] = cofactors[row][column].divide(determinant);
            }
        }

        return cofactors;
    }

    private static Fraction[][] transposeMatrix(Fraction[][] matrix) {
        Fraction[][] transpose = new Fraction[matrix.length][matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            Fraction[] transposeRow = new Fraction[matrix.length];
            for (int column = 0; column < matrix.length; column++) {
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
        for (int col = 0; col < matrix.length; col++) {
            Fraction fractionFirst = (col + 1) % 2 == 0 ? matrix[0][col] : matrix[0][col].negate();
            Fraction fractionSecond = getMatrixDeterminant(getMatrixMinor(matrix, 0, col));
            result = result.add(fractionFirst
                            .multiply(fractionSecond));
        }
        return result;
    }

    private static Fraction[][] getMatrixMinor(Fraction[][] matrix, int row, int column) {
        Fraction[][] minor = new Fraction[matrix.length - 1][matrix.length - 1];
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

        Fraction[][] result = new Fraction[i.length][i.length];
        for (int row = 0; row < i.length; row++) {
            for (int column = 0; column < i.length; column++) {
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
                Fraction value = row == column ? Fraction.one() : Fraction.zero();
                i[row][column] = value;
            }
        }

        return i;
    }

    /**
     * decompose input matrix `p` on Q (t-by-t) component. `t` is the number of transient states
     */
    private static Fraction[][] convertToQ(Fraction[][] p, int transientsStateQuantity) {
        Fraction[][] q = new Fraction[transientsStateQuantity][transientsStateQuantity];
        for (int row = 0; row < transientsStateQuantity; row++) {
            q[row] = Arrays.copyOf(p[row], transientsStateQuantity);
        }
        return q;
    }

    /**
     * decompose input matrix `p` on R (t-by-r) component. `t` is the number of transient states
     */
    private static Fraction[][] convertToR(Fraction[][] p, int transientsStateQuantity) {
        Fraction[][] r = new Fraction[transientsStateQuantity][p.length - transientsStateQuantity];
        for (int row = 0; row < transientsStateQuantity; row++) {
            r[row] = Arrays.copyOfRange(p[row], transientsStateQuantity, p.length);
        }
        return r;
    }

    /**
     * get number of transients states assume absorbing states follow transient states w/o interlieveing
     */
    private static int getTransientsStateQuantity(int[][] m) {
        int count = 0;
        for (int row = 0; row < m.length; row++) {
            int sum = 0;
            for (int column = 0; column < m.length; column++) {
                sum += m[row][column];
            }
            if (sum != 0) {
                count++;
            }
        }

        return count;
    }

    private static Fraction[][] convertToProbabilitiesMatrix(int[][] m) {
        Fraction[][] probabilities = new Fraction[m.length][m.length];
        for (int i = 0; i < m.length; i++) {
            int sum = 0;
            Fraction[] row = Stream.generate(Fraction::zero)
                    .limit(m.length)
                    .toArray(Fraction[]::new);
            for (int j = 0; j < m.length; j++) {
                sum += m[i][j];
            }
            if (sum == 0) {
                for (int j = 0; j < m.length; j++) {
                    if (j == i) {
                        row[j] = Fraction.one();
                    }
                }
            } else {
                for (int j = 0; j < m.length; j++) {
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
        private static final Fraction ZERO = new Fraction(0, 0);
        private final int numerator;
        private final int denominator;

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

        public Fraction negate() {
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
            return numerator +
                    "/" + denominator;
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

            List<Fraction> fractions = new ArrayList<>();
            fractions.add(this);
            fractions.add(fraction);
            fractions = Fraction.convertToCommonDenominator(fractions);
            return new Fraction(fractions.get(0).numerator - fractions.get(1).numerator, fractions.get(0).denominator);
        }

        private static List<Fraction> convertToCommonDenominator(Fraction[] fractionsArray) {
            return convertToCommonDenominator(Arrays.stream(fractionsArray).collect(Collectors.toList()));
        }

        private static List<Fraction> convertToCommonDenominator(List<Fraction> fractions) {
            fractions = fractions.stream()
                    .map(f -> {
                        if (f.isZero()) {
                            return f;
                        }
                        int divisor = greatestCommonDivisor(f.numerator, f.denominator);
                        return new Fraction(f.numerator/divisor, f.denominator/divisor);
                    })
                    .collect(Collectors.toList());

            List<Integer> denominators = fractions.stream()
                    .filter(f -> !f.isZero())
                    .map(f -> f.denominator)
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

                    int min = denominators.get(0);
                    int max = denominators.get(denominators.size() - 1);
                    denominators.remove(0);
                    denominators.remove(denominators.size() - 1);
                    denominators.add(min * max);
                }
            }

            return fractions;
        }

        public static int greatestCommonDivisor(int a, int b) {
            return b==0 ? a : greatestCommonDivisor(b, a%b);
        }

        public Fraction divide(Fraction fraction) {
            return new Fraction(this.numerator * fraction.denominator, this.denominator * fraction.numerator);
        }
    }

    private static void print(Fraction[][] matrix) {
        for (Fraction[] fractions : matrix) {
            for (Fraction fraction : fractions) {
                System.err.print(fraction + " | ");
            }
            System.err.println(" ");
        }
    }
}