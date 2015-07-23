package homeworkLSD;

import java.util.Arrays;
import java.util.Random;

public class LSDsort {

	private static final int MAX = 1000000;
	private static final int COLUMN = 4;
	private final static int BITS_PER_BYTE = 8;
	private static final int NUMBER_RUN = 100;

	private static int[] generate() {

		final int[] data = new int[MAX];

		final Random random = new Random();

		for (int i = 0; i < data.length; i++) {
			data[i] = random.nextInt(MAX);// 0-99999
		}
		return data;
	}

	public static void sort(final int[] a) {
		final int BITS = 32;
		final int W = BITS / BITS_PER_BYTE;
		final int R = 1 << BITS_PER_BYTE;

		final int N = a.length;
		final int[] aux = new int[N];

		for (int d = 0; d < W; d++) {

			final int[] count = new int[R + 1];
			for (int i = 0; i < N; i++) {
				final int c = (a[i] >> BITS_PER_BYTE * d) & 0xFF;
				count[c + 1]++;
			}

			for (int r = 0; r < R; r++) {
				count[r + 1] += count[r];
			}

			for (int i = 0; i < N; i++) {
				final int c = (a[i] >> BITS_PER_BYTE * d) & 0xFF;
				aux[count[c]++] = a[i];
			}

			for (int i = 0; i < N; i++) {
				a[i] = aux[i];
			}
		}
	}

	public static void main(final String[] args) {

		long countTimeStandartSort = 0;
		long countTimeLSDSort = 0;
		
		for (int i = 0; i < NUMBER_RUN; i++) {
			final int[] data = generate();
			final int[] dataCopy = data.clone();
			
			final long start = System.nanoTime();
			Arrays.sort(data);
			final long stop = System.nanoTime();

			final long start1 = System.nanoTime();
			sort(dataCopy);
			final long stop1 = System.nanoTime();

			final boolean areEqual = Arrays.equals(data, dataCopy);
			
			if(areEqual) {
				countTimeStandartSort += stop - start;
				countTimeLSDSort += stop1 - start1;
			}else {
				System.err.println("Something wrong with data equals");
			}
		
		}
		final double timeWinnings = (double)countTimeStandartSort / (double)countTimeLSDSort;
		System.out.print("This program can compare Standart and LSD sorts, LSD-sort faster in: ");
		System.out.println(timeWinnings);

	}
}
