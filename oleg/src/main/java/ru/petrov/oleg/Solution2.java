package ru.petrov.oleg;

import java.util.*;
import java.util.stream.Collectors;

/**
 * https://gist.github.com/dishbreak/48bca2c9c60420a53920c39e08d16c56
 * https://habr.com/ru/post/560060/
 */
public class Solution2 {

    public static int[] solution(int h, int[] q) {
        final int rootNodeValue = pow(2,h) - 1;

        for (int i = 0; i < q.length; i++) {
            q[i] = findRootValueFromTree(rootNodeValue, h, q[i]);
        }

        return q;
    }

    private static int findRootValueFromTree(int rootNodeValue,
                                             int currentLevel,
                                             int givenValue) {
        int childLevel = currentLevel - 1;
        int leftChildNodeValue = rootNodeValue - pow(2, childLevel);
        if (givenValue == leftChildNodeValue) {
            return rootNodeValue;
        }
        if (givenValue < leftChildNodeValue) {
            return findRootValueFromTree(leftChildNodeValue, childLevel, givenValue);
        }
        int rightChildNodeValue = rootNodeValue - 1;
        if (givenValue == rightChildNodeValue) {
            return rootNodeValue;
        }
        if (givenValue < rightChildNodeValue) {
            return findRootValueFromTree(rightChildNodeValue, childLevel, givenValue);
        }
        return -1;
    }

    private static int pow(int value, int powValue) {
        int result = 1;
        for (int i = 1; i <= powValue; i++) {
            result = result * value;
        }
        return result;
    }
}

//        for (int currentLevel = h; currentLevel > 0; currentLevel--) {
//            int rootNodeValue = pow(2, currentLevel) - 1;
//            int nodePairQuantityInChildLevel = pow(2, h - currentLevel + 1)/2;
//            for (int currentPair = nodePairQuantityInChildLevel; currentPair > 0; currentPair--) {
//                int childLevel = currentLevel - 1;
//                int currentRootNodeValue = rootNodeValue * currentPair;
//                int leftChildNodeValue = currentRootNodeValue - pow(2, childLevel);
//                int rightChildNodeValue = currentRootNodeValue - 1;
//            }
//        }
//
//1    private static void fillValueAndRootValueFromTree(List<Integer> rootNodeValues,
//        int currentLevel,
//        LinkedHashMap<Integer, Integer> valueAndRootValueMap) {
//        List<Integer> nextRootNodeValues = new ArrayList<>();
//        int childLevel = currentLevel - 1;
//        for (Integer currentRootNodeValue: rootNodeValues) {
//        int leftChildNodeValue = currentRootNodeValue - pow(2, childLevel);
//        nextRootNodeValues.add(leftChildNodeValue);
//        valueAndRootValueMap.computeIfPresent(leftChildNodeValue, (k, v) -> currentRootNodeValue);
//
//        int rightChildNodeValue = currentRootNodeValue - 1;
//        nextRootNodeValues.add(rightChildNodeValue);
//        valueAndRootValueMap.computeIfPresent(rightChildNodeValue, (k, v) -> currentRootNodeValue);
//
//        boolean isNotFilled = valueAndRootValueMap.values().stream()
//        .anyMatch(v -> v == FLAG_FOR_NOT_FILLED_VALUES);
//        if (!isNotFilled) {
//        return;
//        }
//        }
//
//        fillValueAndRootValueFromTree(nextRootNodeValues, childLevel, valueAndRootValueMap);
//        }