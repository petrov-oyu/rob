package ru.petrov.oleg;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * https://blog.ishandeveloper.com/foobar-2020
 * https://github.com/TesfayKidane/Algorithm/blob/master/Algorithm/src/google/foo/bar/FuelInjectionPerfection.java
 */
public class Solution3_2 {
    private static final BigInteger THREE = BigInteger.valueOf(3);

    public static int   solution(String x) {
        BigInteger givenNumber = new BigInteger(x);
        int count = 0;
        while (!givenNumber.equals(BigInteger.ONE)) {
            BigInteger num = givenNumber.shiftRight(1);
            if (givenNumber.and(BigInteger.ONE).equals(BigInteger.ZERO)) {
                count++;
                givenNumber = num;
            } else {
                if (givenNumber.equals(THREE)) {
                    count += 2;
                    break;
                }

                if (num.and(BigInteger.ONE).equals(BigInteger.ZERO)) {
                    count += 2;
                    givenNumber = num;
                } else {
                    count += 2;
                    givenNumber = num.add(BigInteger.ONE);
                }
            }
        }
        return count;
    }
}

/**
 * Verifying solution...
 * All test cases passed. Use submit Solution.java to submit your solution
 */
//
//private static final BigInteger THREE = BigInteger.valueOf(3);
//
//    public static int   solution(String x) {
//        BigInteger givenNumber = new BigInteger(x);
//        int count = 0;
//        while (!givenNumber.equals(BigInteger.ONE)) {
//            BigInteger num = givenNumber.shiftRight(1);
//            if (givenNumber.and(BigInteger.ONE).equals(BigInteger.ZERO)) {
//                givenNumber = num;
//            } else {
//                if (givenNumber.equals(THREE)) {
//                    count += 2;
//                    break;
//                }
//
//                if (num.and(BigInteger.ONE).equals(BigInteger.ZERO)) {
//                    givenNumber = givenNumber.subtract(BigInteger.ONE);
//                } else {
//                    givenNumber = givenNumber.add(BigInteger.ONE);
//                }
//            }
//            count++;
//        }
//        return count;
//    }


/**
 * Verifying solution...
 * Test 1 passed!
 * Test 2 passed!
 * Test 3 passed! [Hidden]
 * Test 4 failed  [Hidden]
 * Test 5 passed! [Hidden]
 * Test 6 passed! [Hidden]
 * Test 7 passed! [Hidden]
 * Test 8 failed  [Hidden]
 * Test 9 passed! [Hidden]
 * Test 10 passed! [Hidden]
 */
//    public static int   solution(String x) {
//        if ("1".equals(x)) {
//            return 0;
//        }
//
//        BigInteger givenNumber = new BigInteger(x);
//        return count(givenNumber);
//
//    }
//
//    private static int count(BigInteger givenNumber) {
//        if (givenNumber.equals(BigInteger.ONE)) {
//            return 0;
//        }
//
//        BigInteger num = givenNumber.shiftRight(1);
//        if (givenNumber.and(BigInteger.ONE).equals(BigInteger.ZERO)) {
//            return 1 + count(num);
//        }
//
//        if (num.and(BigInteger.ONE).equals(BigInteger.ZERO)) {
//            return 2 + count(num);
//        }
//
//        return 2 + count(num.add(BigInteger.ONE));
//    }


/**
 *
 */
//    public static int   solution(String x) {
//        if ("1".equals(x)) {
//            return 0;
//        }
//        LinkedList<Integer> binary = convertToBinary(x);
//
//        return count(binary);
//    }
//
//    private static int count(LinkedList<Integer> binary) {
//        if (binary.size() == 1) {
//            return 0;
//        }
//
//        int counter = 0;
//        while (binary.getFirst() == 0) {
//            counter++;
//            binary.removeFirst(); // binary/2
//        }
//
//        if (binary.size() == 1) {
//            return counter;
//        }
//
//        binary.removeFirst();
//        if (binary.size() == 1) {
//            return 1 + counter;
//        }
//
//        if (binary.getFirst() == 0) {
//            // binary-1 and binary/2
//            return counter + 2 + count(binary);
//        } else {
//            addOne(binary);// binary+1 and binary/2
//            return counter + 2 + count(binary);
//        }
//    }
//
//    private static void addOne(LinkedList<Integer> binary) {
//        int size = binary.size();
//        for (int i = 0; i < size; i++) {
//            if (binary.get(i) == 1) {
//                binary.set(i, 0);
//                if (i == 0) {
//                    binary.addLast(1);
//                }
//            } else {
//                binary.set(i, 1);
//                break;
//            }
//        }
//    }
//
//    private static LinkedList<Integer> convertToBinary(String x) {
//        char[] chars = x.toCharArray();
//        LinkedList<Integer> givenNum = new LinkedList<>();
//        for (int i = chars.length - 1; i >= 0; i--) {
//            givenNum.add(Character.digit(chars[i], 10));
//        }
//        return convertToBinary(givenNum, new LinkedList<>());
//    }
//
//    private static LinkedList<Integer> convertToBinary(LinkedList<Integer> givenNum, LinkedList<Integer> binary) {
//        if (givenNum.size() == 1 && givenNum.get(0) == 1) {
//            binary.add(1);
//            return binary;
//        }
//        binary.add(givenNum.get(0) % 2);
//        LinkedList<Integer> numDividedByTwo = new LinkedList<>();
//        numDividedByTwo.add(givenNum.get(0) / 2);
//        for (int position = 1; position < givenNum.size(); position++) {
//            int digit = givenNum.get(position);
//            if (digit % 2 != 0) {
//                Integer last = numDividedByTwo.removeLast();
//                numDividedByTwo.addLast(last + 5);
//            }
//            numDividedByTwo.add(digit / 2);
//        }
//
//        if (numDividedByTwo.getLast() == 0) {
//            numDividedByTwo.removeLast();
//        }
//        return convertToBinary(numDividedByTwo, binary);
//    }

/**
 * Verifying solution...
 * Test 1 passed!
 * Test 2 passed!
 * Test 3 failed  [Hidden]
 * Test 4 failed  [Hidden]
 * Test 5 passed! [Hidden]
 * Test 6 failed  [Hidden]
 * Test 7 failed  [Hidden]
 * Test 8 failed  [Hidden]
 * Test 9 failed  [Hidden]
 * Test 10 failed  [Hidden]
 */
//    public static int   solution(String x) {
//        if ("1".equals(x)) {
//            return 0;
//        }
//        LinkedList<Integer> binary = convertToBinary(x);
//
//        return count(binary);
//    }
//
//    private static int count(LinkedList<Integer> binary) {
//        if (binary.size() == 1) {
//            return 0;
//        }
//
//        if (binary.getFirst() == 0) {
//            binary.removeFirst(); // binary/2
//            return 1 + count(binary);
//        } else {
//            binary.removeFirst();
//            if (binary.getFirst() == 0) {
//                // binary-1 and binary/2
//                return 2 + count(binary);
//            } else {
//                addOne(binary);// binary+1 and binary/2
//                return 2 + count(binary);
//            }
//        }
//    }
//
//    private static void addOne(LinkedList<Integer> binary) {
//        int size = binary.size();
//        for (int i = 0; i < size; i++) {
//            if (binary.get(i) == 1) {
//                binary.set(i, 0);
//                if (i == 0) {
//                    binary.addLast(1);
//                }
//            } else {
//                binary.set(i, 1);
//                break;
//            }
//        }
//    }
//
//    private static LinkedList<Integer> convertToBinary(String x) {
//        char[] chars = x.toCharArray();
//        LinkedList<Integer> givenNum = new LinkedList<>();
//        for (int i = chars.length - 1; i >= 0; i--) {
//            givenNum.add(Character.digit(chars[i], 10));
//        }
//        return convertToBinary(givenNum, new LinkedList<>());
//    }
//
//    private static LinkedList<Integer> convertToBinary(LinkedList<Integer> givenNum, LinkedList<Integer> binary) {
//        if (givenNum.size() == 1 && givenNum.get(0) == 1) {
//            binary.add(1);
//            return binary;
//        }
//        binary.add(givenNum.get(0) % 2);
//        LinkedList<Integer> numDividedByTwo = new LinkedList<>();
//        numDividedByTwo.add(givenNum.get(0) / 2);
//        for (int position = 1; position < givenNum.size(); position++) {
//            int digit = givenNum.get(position);
//            if (digit % 2 != 0) {
//                Integer last = numDividedByTwo.removeLast();
//                numDividedByTwo.addLast(last + 5);
//            }
//            numDividedByTwo.add(digit / 2);
//        }
//
//        if (numDividedByTwo.getLast() == 0) {
//            numDividedByTwo.removeLast();
//        }
//        return convertToBinary(numDividedByTwo, binary);
//    }


/**
 * Verifying solution...
 * Test 1 passed!
 * Test 2 passed!
 * Test 3 failed  [Hidden]
 * Test 4 failed  [Hidden]
 * Test 5 passed! [Hidden]
 * Test 6 failed  [Hidden]
 * Test 7 failed  [Hidden]
 * Test 8 failed  [Hidden]
 * Test 9 failed  [Hidden]
 * Test 10 failed  [Hidden]
 */
//    public static int solution(String x) {
//        if ("1".equals(x)) {
//            return 0;
//        }
//        LinkedList<Integer> binary = convertToBinary(x);
//
//        return count(binary);
//    }
//
//    private static int count(LinkedList<Integer> binary) {
//        if (binary.size() == 1) {
//            return 1;
//        }
//
//        if (binary.getLast() == 0) {
//            binary.removeLast(); // binary/2
//            return 1 + count(binary);
//        } else {
//            binary.removeLast();
//            if (binary.getLast() == 0) {
//                // binary-1 and binary/2
//                return 2 + count(binary);
//            } else {
//                addOne(binary);// binary+1 and binary/2
//                return 2 + count(binary);
//            }
//        }
//    }
//
//    private static void addOne(LinkedList<Integer> binary) {
//        int size = binary.size() - 1;
//        for (int i = size; i >= 0; i--) {
//            if (binary.get(i) == 1) {
//                binary.set(i, 0);
//                if (i == 0) {
//                    binary.addFirst(1);
//                }
//            } else {
//                binary.set(i, 1);
//                break;
//            }
//        }
//    }
//
//    private static LinkedList<Integer> convertToBinary(String x) {
//        char[] chars = x.toCharArray();
//        LinkedList<Integer> givenNum = new LinkedList<>();
//        for (int i = chars.length - 1; i >= 0; i--) {
//            givenNum.add(Character.digit(chars[i], 10));
//        }
//        return convertToBinary(givenNum, new LinkedList<>());
//    }
//
//    private static LinkedList<Integer> convertToBinary(LinkedList<Integer> givenNum, LinkedList<Integer> binary) {
//        if (givenNum.size() == 1 && givenNum.get(0) == 1) {
//            return binary;
//        }
//        binary.add(givenNum.get(0) % 2);
//        LinkedList<Integer> numDividedByTwo = new LinkedList<>();
//        numDividedByTwo.add(givenNum.get(0) / 2);
//        for (int position = 1; position < givenNum.size(); position++) {
//            int digit = givenNum.get(position);
//            if (digit % 2 != 0) {
//                Integer last = numDividedByTwo.removeLast();
//                numDividedByTwo.addLast(last + 5);
//            }
//            numDividedByTwo.add(Character.digit(digit, 10) / 2);
//        }
//
//        if (numDividedByTwo.getLast() == 0) {
//            numDividedByTwo.removeLast();
//        }
//        return convertToBinary(numDividedByTwo, binary);
//    }
