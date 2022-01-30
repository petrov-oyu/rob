package ru.petrov.oleg;

import java.util.Arrays;

public class OlegApplication {

	public static void main(String[] args) {
		//int[] solution = Solution3_3ThirPart.solution(new int[][]{{0, 1, 0, 0, 0, 1}, {4, 0, 0, 3, 2, 0}, {0, 0, 0, 0, 0, 0},
		//		{0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}});
		//Arrays.stream(solution).forEach(System.err::println);
		// 0 3 2 9 14

		System.err.println("=========");
		int[] solution = Solution3_3Test.solution(new int[][]{{0, 1, 0, 0, 0, 1}, {4, 0, 0, 3, 2, 0}, {0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}});
		Arrays.stream(solution).forEach(System.err::println);
		// 0 3 2 9 14

		//System.err.println("=========");
//
		//solution = Solution3_3.solution(new int[][]{{0, 223, 113, 230, 245}, {0, 220, 233, 145, 143},
		//		{0, 0, 0, 230, 245}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}});
		//Arrays.stream(solution).forEach(System.err::println);
		// -123628897 361794946 837864663
	}
}