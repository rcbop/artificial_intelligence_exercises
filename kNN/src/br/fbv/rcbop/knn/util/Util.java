package br.fbv.rcbop.knn.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.swing.ImageIcon;

import br.fbv.rcbop.knn.model.Iris;

public class Util {
	public static int countLines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n')
						++count;
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	public ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public static Iris[] basicQuickSort(int beginIdx, int len, Iris[] database) {
		if (len <= 1)
			return database;

		final int endIdx = beginIdx + len - 1;

		// Pivot selection
		final int pivotPos = beginIdx + len / 2;
		final Iris pivot = database[pivotPos];
		database = swap(pivotPos, endIdx, database);

		// partitioning
		int p = beginIdx;
		for (int i = beginIdx; i != endIdx; ++i) {
			if (database[i].getDistance() <= pivot.getDistance()) {
				database = swap(i, p++, database);
			}
		}
		database = swap(p, endIdx, database);

		// recursive call
		database = basicQuickSort(beginIdx, p - beginIdx, database);
		database = basicQuickSort(p + 1, endIdx - p, database);
		return database;
	}

	public static Iris[] shuffleDatabase(Iris[] database) {
		int n = database.length;
		Random random = new Random();
		random.nextInt();
		for (int i = 0; i < n; i++) {
			int change = i + random.nextInt(n - i);
			database = swap(i, change, database);
		}
		return database;
	}

	private static Iris[] swap(int i, int j, Iris[] database) {
		Iris temp = database[i];
		database[i] = database[j];
		database[j] = temp;
		return database;
	}
}
