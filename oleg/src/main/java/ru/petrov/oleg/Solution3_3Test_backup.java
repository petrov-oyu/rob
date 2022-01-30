package ru.petrov.oleg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * https://stackoverflow.com/questions/62671372/google-foo-bar-challenge-level-3-doomsday-fuel
 */
public class Solution3_3Test_backup {
    public static int[] solution(int[][] m) {
        List<Integer> transientRows = getTransientRows(m);
        List<Integer> terminateRows = IntStream.range(0, m.length)
                .filter(v -> !transientRows.contains(v))
                .boxed()
                .collect(Collectors.toList());
        List<Integer> rowDenominators = getRowDenominators(m);
        if(m[0][0]==0 && m.length==1) {
            return new int[]{1, 1};
        }

        Matrix i = new Matrix(createIdentity(transientRows.size()), transientRows.size(), transientRows.size());
        Matrix q = new Matrix(convertToQ(m, transientRows, rowDenominators), transientRows.size(), transientRows.size());
        Matrix r = new Matrix(convertToR(m, transientRows, rowDenominators), transientRows.size(), terminateRows.size());

        // I - Q
        Matrix subtract = i.subtract(q);
        // (I - Q)^-1
        Matrix inverse = subtract.getInverseMatrix();
        Matrix multiply = inverse.multiply(r);

        List<Fraction> firstRow = multiply.getRow(0);
        return convertToResultFormat(m, firstRow);
    }

    private static int[] convertToResultFormat(int[][] m, List<Fraction> firstRow) {
        ArrayList<Fraction> numeratorList = new ArrayList<>(); // numeratorList
        int[] denomList = new int[firstRow.size()]; // denomList
        // Find the numerators and the common denominator, make it an array
        for (int index = 0; index < firstRow.size(); index++)
        {
            denomList[index] = firstRow.get(index).getDenominator();
            numeratorList.add(firstRow.get(index));
        }
        int lcm = getLcm(denomList);
        int[] result = new int[firstRow.size()+1];
        if(m[0][0]==0 && m.length==1) {
            result[0]=1;
            result[1]=1;
            return result;
        } else {
            for (int j = 0; j < result.length-1; j++) {
                numeratorList.set(j, numeratorList.get(j).multiply(new Fraction(lcm)));
                result[j] = numeratorList.get(j).getNumerator();
            }
            result[firstRow.size()] = lcm;
            return result;
        }
    }

    /**
     * decompose input matrix `p` on R (t-by-r) component. `t` is the number of transient states
     */
    private static List<List<Fraction>> convertToR(int[][] m, List<Integer> transientRows, List<Integer> denominators) {
        List<Integer> terminateRows = IntStream.range(0, m.length)
                .filter(v -> !transientRows.contains(v))
                .boxed()
                .collect(Collectors.toList());

        List<List<Fraction>> r = new ArrayList<>();
        for (int row = 0; row < transientRows.size(); row++) {
            List<Fraction> rRow = new ArrayList<>();
            for (int column = 0; column < terminateRows.size(); column++) {
                rRow.add(new Fraction(m[transientRows.get(row)][terminateRows.get(column)], denominators.get(row)));
            }
            r.add(rRow);
        }
        return r;
    }

    /**
     * decompose input matrix `p` on Q (t-by-t) component. `t` is the number of transient states
     */
    private static List<List<Fraction>> convertToQ(int[][] m, List<Integer> transientRows, List<Integer> denominators) {
        List<List<Fraction>> q = new ArrayList<>();
        for (int row = 0; row < transientRows.size(); row++) {
            List<Fraction> qRow = new ArrayList<>();
            for (int column = 0; column < transientRows.size(); column++) {
                qRow.add(new Fraction(m[transientRows.get(row)][transientRows.get(column)], denominators.get(row)));
            }
            q.add(qRow);
        }
        return q;
    }

    /**
     * create identity matrix of size `t`
     */
    private static List<List<Fraction>> createIdentity(int size) {
        List<List<Fraction>> i = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            List<Fraction> iRow = new ArrayList<>();
            for (int column = 0; column < size; column++) {
                Fraction value = row == column ? Fraction.one() : Fraction.zero();
                iRow.add(value);
            }
            i.add(iRow);
        }

        return i;
    }

    private static List<Integer> getRowDenominators(int[][] m) {
        return Arrays.stream(m)
                .map(row -> Arrays.stream(row).sum())
                .filter(sum -> sum != 0)
                .collect(Collectors.toList());
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

    public static int getLcm(int arr[]) {
        int max = 0;
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        int res = 1;
        int factor = 2;
        while (factor <= max) {
            ArrayList<Integer> arrIndex = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (arr[j] % factor == 0) {
                    arrIndex.add(arrIndex.size(), j);
                }
            }
            if (arrIndex.size() >= 2) {
                // Reduce all array elements divisible
                // by factor.
                for (int j = 0; j < arrIndex.size(); j++) {
                    arr[arrIndex.get(j)] /= factor;
                }

                res *= factor;
            } else {
                factor++;
            }
        }

        // Then multiply all reduced array elements
        for (int i = 0; i < n; i++) {
            res *= arr[i];
        }

        return res;
    }

    private static class Matrix {
        private final int M;
        private final int N;
        private final Fraction det;
        private List<List<Fraction>> matrix;
        private List<List<Fraction>> inverseMatrix;

        public Matrix(List<List<Fraction>> mat, int m, int n) {
            this.matrix = mat;
            this.M = m;
            this.N = n;
            this.det = this.determinant(mat, n);
            this.inverseMatrix = this.inverse();
        }

        private void getCofactor(List<List<Fraction>> mat, List<List<Fraction>> tempMat, int p, int q, int n) {
            int i = 0;
            int j = 0;
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    if (row != p && col != q) {
                        tempMat.get(i).set(j++, mat.get(row).get(col));
                        if (j == n - 1) {
                            j = 0;
                            i++;
                        }
                    }
                }
            }
        }

        private Fraction determinant(List<List<Fraction>> mat, int n) {
            Fraction ans = new Fraction(0, 1);
            if (this.M != this.N) {
                return ans;
            }
            if (n == 1) {
                return mat.get(0).get(0);
            }
            List<List<Fraction>> tempMat = new ArrayList<>();
            // Init 2d fraction arraylist
            for (int i = 0; i < this.M; i++) {
                List<Fraction> tempMatRow = new ArrayList<>();
                for (int j = 0; j < this.N; j++) {
                    tempMatRow.add(new Fraction(0, 1));
                }
                tempMat.add(tempMatRow);
            }

            int sign = 1;
            Fraction signFraction = new Fraction(sign, 1);
            for (int k = 0; k < n; k++) {
                this.getCofactor(mat, tempMat, 0, k, n);
                ans = ans.add(signFraction.multiply(mat.get(0).get(k).multiply(determinant(tempMat, n - 1))));
                sign = -sign;
                signFraction = new Fraction(sign, 1);
            }
            return ans;
        }

        private void adjoint(List<List<Fraction>> mat, List<List<Fraction>> adj) {
            if (this.N == 1) {
                adj.get(0).set(0, new Fraction(1, 1));
                return;
            }
            int sign = 1;

            List<List<Fraction>> tempMat = new ArrayList<>();
            // Init 2d fraction arraylist
            for (int i = 0; i < this.N; i++) {
                List<Fraction> tempMatRow = new ArrayList<>();
                for (int j = 0; j < this.N; j++) {
                    tempMatRow.add(new Fraction(0, 1));
                }
                tempMat.add(tempMatRow);
            }

            for (int p = 0; p < this.N; p++) {
                for (int q = 0; q < this.N; q++) {
                    this.getCofactor(mat, tempMat, p, q, this.N);
                    sign = ((p + q) % 2 == 0) ? 1 : -1;
                    Fraction signFraction = new Fraction(sign, 1);
                    adj.get(q).set(p, signFraction.multiply((this.determinant(tempMat, this.N - 1))));
                }
            }
        }

        private List<List<Fraction>> inverse() {
            List<List<Fraction>> inv = new ArrayList<>();
            // Init 2d fraction arraylist
            for (int i = 0; i < this.M; i++) {
                List<Fraction> invRow = new ArrayList<>();
                for (int j = 0; j < this.N; j++) {
                    invRow.add(new Fraction(0, 1));
                }
                inv.add(invRow);
            }

            if (this.det.equals(new Fraction(0))) {
                return inv;
            }

            List<List<Fraction>> adj = new ArrayList<>();
            // Init 2d fraction arraylist
            for (int i = 0; i < this.M; i++) {
                List<Fraction> adjRow = new ArrayList<>();
                for (int j = 0; j < this.N; j++) {
                    adjRow.add(new Fraction(0, 1));
                }
                adj.add(adjRow);
            }

            adjoint(this.matrix, adj);
            for (int p = 0; p < this.N; p++) {
                for (int q = 0; q < this.N; q++) {
                    Fraction temp = adj.get(p).get(q).divide(this.det);
                    inv.get(p).set(q, temp);
                }
            }
            return inv;
        }

        public Matrix getInverseMatrix() {
            return new Matrix(this.inverseMatrix, this.M, this.N);
        }

        public Fraction getElement(int m, int n) {
            return this.matrix.get(m).get(n);
        }

        public List<Fraction> getRow(int m) {
            if (m <= this.M) {
                return this.matrix.get(m);
            }
            return new ArrayList<Fraction>();
        }

        public Matrix plus(Matrix mat) {
            int M_m = mat.getDimension()[0];
            int N_m = mat.getDimension()[1];
            if (this.M != M_m || this.N != N_m) {
                //system.out.println("Error in plus: Dimensions of two matrices are not equal!"); // Debug
                return mat;
            } else {
                List<List<Fraction>> sum = new ArrayList<>();
                // Init 2d fraction arraylist
                for (int i = 0; i < this.M; i++) {
                    List<Fraction> sumRow = new ArrayList<>();
                    for (int j = 0; j < this.N; j++) {
                        sumRow.add(new Fraction(0, 1));
                    }
                    sum.add(sumRow);
                }
                for (int i = 0; i < this.M; i++) {
                    for (int j = 0; j < this.N; j++) {
                        sum.get(i).set(j, this.matrix.get(i).get(j).add(mat.getElement(i, j)));
                    }
                }
                return new Matrix(sum, this.M, this.N);
            }
        }

        public Matrix subtract(Matrix mat) {
            List<List<Fraction>> result = new ArrayList<>();
            for (int row = 0; row < this.M; row++) {
                List<Fraction> resultRow = new ArrayList<>();
                for (int column = 0; column < this.N; column++) {
                    resultRow.add(this.matrix.get(row).get(column).subtract(mat.getElement(row, column)));
                }
                result.add(resultRow);
            }

            return new Matrix(result, this.M, this.N);
        }

        public Matrix multiply(Matrix mat) {
            List<List<Fraction>> multiply = new ArrayList<>();
            for (int row = 0; row < this.M; row++) {
                List<Fraction> multiplyRow = new ArrayList<>();
                for (int column = 0; column < mat.getDimension()[1]; column++) {
                    Fraction sum = Fraction.zero();
                    for (int k = 0; k < this.N; k++) {
                        sum = sum.add(this.matrix.get(row).get(k).multiply(mat.getElement(k, column)));
                    }
                    multiplyRow.add(sum);
                }
                multiply.add(multiplyRow);
            }
            return new Matrix(multiply, this.M, mat.getDimension()[1]);
        }

        public int[] getDimension() {
            return new int[] { this.M, this.N };
        }
    }

    private static class Fraction {
        private static final Fraction ZERO = new Fraction(0);
        private static final Fraction ONE = new Fraction(1);

        private int numerator;
        private int denominator = 1;
        private boolean sign = false; // true = negative, false = positive

        public Fraction(int num, int denom) {
            this.numerator = num;
            if (denom != 0) {
                this.denominator = denom;
            }
            this.simplify();
        }

        public Fraction(int num) {
            this.numerator = num;
            this.simplify();
        }

        private int getGcm(int num1, int num2) {
            return num2 == 0 ? num1 : this.getGcm(num2, num1 % num2);
        }

        // Simplify fraction to simplest form, runs in constructor
        public void simplify() {
            this.sign = !(this.numerator <= 0 && this.denominator <= 0) && !(this.numerator >= 0 && this.denominator >= 0);

            this.numerator = Math.abs(this.numerator);
            this.denominator = Math.abs(this.denominator);

            int gcm = this.getGcm(this.numerator, this.denominator);
            this.numerator = this.numerator / gcm;
            this.denominator = this.denominator / gcm;
            // When fraction is zero, make sure denominator is one and no negative sign
            if (this.numerator == 0 && this.denominator != 0) {
                this.denominator = 1;
                this.sign = false;
            }
        }

        public Fraction add(Fraction f1) {
            int num = 0;
            if (this.sign) { // this fraction is negative
                if (f1.getSign()) { // f1 is negative
                    num = (-1) * this.numerator * f1.denominator + this.denominator * (-1) * f1.numerator;
                } else { // f1 is positive
                    num = (-1) * this.numerator * f1.denominator + this.denominator * f1.numerator;
                }
            } else { // this fraction is positive
                if (f1.getSign()) { // f1 is negative
                    num = this.numerator * f1.denominator + this.denominator * (-1) * f1.numerator;
                } else { // f1 is positive
                    num = this.numerator * f1.denominator + this.denominator * f1.numerator;
                }
            }
            int denom = this.denominator * f1.getDenominator();
            return new Fraction(num, denom);
        }

        public Fraction subtract(Fraction f1) {
            int num = 0;
            if (this.sign) { // this fraction is negative
                if (f1.getSign()) { // f1 is negative
                    num = (-1) * this.numerator * f1.denominator + this.denominator * f1.numerator;
                } else { // f1 is positive
                    num = (-1) * this.numerator * f1.denominator - this.denominator * f1.numerator;
                }
            } else { // this fraction is positive
                if (f1.getSign()) { // f1 is negative
                    num = this.numerator * f1.denominator + this.denominator * f1.numerator;
                } else { // f1 is positive
                    num = this.numerator * f1.denominator - this.denominator * f1.numerator;
                }
            }
            int denom = this.denominator * f1.getDenominator();
            return new Fraction(num, denom);
        }

        public Fraction multiply(Fraction f1) {
            int signInt = 1;
            // Either one fraction is negative will make the product fraction negative, but not for both fractions are negative.
            if (this.sign && !f1.getSign() || !this.sign && f1.getSign()) {
                signInt = -1;
            }
            return new Fraction(signInt * this.numerator * f1.getNumerator(), this.denominator * f1.getDenominator());
        }

        public Fraction divide(Fraction f1) {
            int signInt = 1;
            // Either one fraction is negative will make the product fraction negative, but not for both fractions are negative.
            if (this.sign && !f1.getSign() || !this.sign && f1.getSign()) {
                signInt = -1;
            }
            return new Fraction(signInt *this.numerator * f1.getDenominator(), this.denominator * f1.getNumerator());
        }

        public boolean equals(Fraction f1) {
            return this.numerator == f1.getNumerator() && this.denominator == f1.getDenominator() && this.sign == f1.getSign();
        }

        public int getNumerator()
        {
            return this.numerator;
        }

        public int getDenominator() {
            return this.denominator;
        }

        public boolean getSign()
        {
            return this.sign;
        }

        public static Fraction one() {
            return ONE;
        }

        public static Fraction zero() {
            return ZERO;
        }
    }
}