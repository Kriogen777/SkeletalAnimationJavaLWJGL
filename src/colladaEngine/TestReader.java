package colladaEngine;

import java.io.File;

public class TestReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		File f = new File("C:\\Users\\Kriogen\\Dropbox\\Programs\\Eclipse\\SkeletalAnimationJavaLWJGL\\src\\colladaEngine\\model\\astroBoy_walk_Max.dae");
		ColladaReader cr = new ColladaReader(f);
		long endTime = System.currentTimeMillis();
		System.out.println("Parsed " + f.getName() + " in " + (endTime - startTime) + " milliseconds.");
	}

}
