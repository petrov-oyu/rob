package ru.petrov.oleg;

import java.math.BigInteger;
import java.util.Optional;

/**
 * 		System.err.println(Solution3_1.solution("3", "7")); // 4
 * 		System.err.println(Solution3_1.solution("4", "7")); // 4
 * 		System.err.println(Solution3_1.solution("1", "2")); // 1
 * 		System.err.println(Solution3_1.solution("1", "1")); // 0
 * 		System.err.println(Solution3_1.solution("100000000000000000000000000000000000000000000000000000000000", "2")); // impossible
 *
 * https://github.com/ivanseed/google-foobar-help/blob/master/challenges/bomb_baby/bomb_baby.md
 */
public class Solution3_1 {
    public static String solution(String x, String y) {
        return count(new BigInteger(x), new BigInteger(y))
                .map(BigInteger::toString)
                .orElse("impossible");
    }

    private static Optional<BigInteger> count(BigInteger first, BigInteger second) {
        if (first.equals(BigInteger.ONE) && second.equals(BigInteger.ONE)) {
            return Optional.of(BigInteger.ZERO);
        } else if (first.compareTo(BigInteger.ZERO) < 0
                || second.compareTo(BigInteger.ZERO) < 0
                || second.equals(first)) {
            return Optional.empty();
        }

        final BigInteger count;
        if (first.compareTo(second) > 0) {
            count = getMultiply(first, second);
            first = first.subtract(second.multiply(count));
        } else {
            count = getMultiply(second, first);
            second = second.subtract(first.multiply(count));
        }

        return count(first, second)
                .map(innerCount -> innerCount.add(count));
    }

    private static BigInteger getMultiply(BigInteger more, BigInteger less) {
        BigInteger delta = more.subtract(less);
        BigInteger multiply = delta.divide(less);
        if (!multiply.equals(BigInteger.ZERO) && delta.mod(less).compareTo(BigInteger.ZERO) > 0) {
            multiply = multiply.add(BigInteger.ONE);
        }
        if (multiply.equals(BigInteger.ZERO)) {
            multiply = BigInteger.ONE;
        }
        return multiply;
    }
}

/**
 * Stack overflow
 */
//    public static String solution(String x, String y) {
//        long count = count(new BigInteger(x), new BigInteger(y));
//        if (count < 0) {
//            return "impossible";
//        } else {
//            return String.valueOf(count);
//        }
//    }
//
//    private static long count(BigInteger first, BigInteger second) {
//        if (first.equals(BigInteger.ONE) && second.equals(BigInteger.ONE)) {
//            return 0;
//        } else if (first.compareTo(BigInteger.ZERO) < 0
//                || second.compareTo(BigInteger.ZERO) < 0
//                || second.equals(first)) {
//            return Long.MIN_VALUE;
//        }
//
//        if (first.compareTo(second) > 0) {
//            BigInteger delta = first.subtract(second);
//            long count = delta.divide(second).longValue();
//            if (count != 0 && delta.mod(second).compareTo(BigInteger.ZERO) > 0) {
//                count++;
//            }
//            count = count == 0 ? 1 : count;
//            return count + count(first.subtract(second.multiply(BigInteger.valueOf(count))), second);
//        } else {
//            BigInteger delta = second.subtract(first);
//            long count = delta.divide(first).longValue();
//            if (count != 0 && delta.mod(first).compareTo(BigInteger.ZERO) > 0) {
//                count++;
//            }
//            count = count == 0 ? 1 : count;
//            return count + count(first, second.subtract(first.multiply(BigInteger.valueOf(count))));
//        }
//    }

/**
 * NumberFormatException
 */
//public static String solution(String x, String y) {
//    long count = count(Long.parseLong(x), Long.parseLong(y));
//    if (count < 0) {
//        return "impossible";
//    } else {
//        return String.valueOf(count);
//    }
//}

//    private static long count(long first, long second) {
//        if (1 == first && 1 == second) {
//            return 0;
//        } else if (first < 0
//                || second < 0
//                || second == first) {
//            return Long.MIN_VALUE;
//        }
//
//        if (first > second) {
//            long count = (first - second)/second;
//            if (count != 0 && (first - second) % second > 0) {
//                count++;
//            }
//            count = count == 0 ? 1 : count;
//            return count + count(first - count * second, second);
//        } else {
//            long count = (second - first)/first;
//            if (count != 0 && (second - first) % first > 0) {
//                count++;
//            }
//            count = count == 0 ? 1 : count;
//            return count + count(first, second - count * first);
//        }
//    }

/**
 * Verifying solution...
 * Test 1 passed!
 * Test 2 passed!
 * Test 3 failed  [Hidden]
 * Test 4 failed  [Hidden]
 * Test 5 failed  [Hidden]
 */
//    public static String solution(String x, String y) {
//        int count = count(Integer.parseInt(x), Integer.parseInt(y));
//        if (count < 0) {
//            return "impossible";
//        } else {
//            return String.valueOf(count);
//        }
//    }
//
//    private static int count(int first, int second) {
//        if (1 == first && 1 == second) {
//            return 0;
//        } else if (first < 0
//                || second < 0
//                || second == first) {
//            return Integer.MIN_VALUE;
//        } else if (first > second) {
//            return 1 + count(first - second, second);
//        } else {
//            return 1 + count(first, second - first);
//        }
//    }
//

/**
 *
 */
//    public static String solution(String x, String y) {
//        return count(new BigInteger(x), new BigInteger(y))
//                .map(String::valueOf)
//                .orElse("impossible");
//    }

//    private static Optional<Integer> count(BigInteger first, BigInteger second) {
//        if (BigInteger.ONE.equals(first) && BigInteger.ONE.equals(second)) {
//            return Optional.of(0);
//        } else if (first.compareTo(BigInteger.ZERO) < 0
//                || second.compareTo(BigInteger.ZERO) < 0
//                || second.equals(first)) {
//            return Optional.empty();
//        } else if (first.compareTo(second) > 0) {
//            return count(first.subtract(second), second)
//                    .map(count -> count + 1);
//        } else {
//            return count(first, second.subtract(first))
//                    .map(count -> count + 1);
//        }
//    }

/**
 *
 */
//    public static String solution(String x, String y) {
//        return count(Integer.parseInt(x), Integer.parseInt(y))
//                .map(String::valueOf)
//                .orElse("impossible");
//    }
//
//    private static Optional<Integer> count(int first, int second) {
//        if (1 == first && 1 == second) {
//            return Optional.of(0);
//        } else if (first < 0
//                || second < 0
//                || second == first) {
//            return Optional.of(-1);
//        } else if (first > second) {
//            return count(first - second, second)
//                    .map(count -> count + 1);
//        } else {
//            return count(first, second - first)
//                    .map(count -> count + 1);
//        }
//    }
