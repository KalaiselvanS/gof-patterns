package ulk.co.gof.cor.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import ulk.co.gof.cor.json.filter.JSONFilterRunner;
import ulk.co.gof.cor.json.filter.JSONOccurrencePincodeFilter;
import ulk.co.gof.cor.json.flatner.WriteTSV;

public class Executer {

	private static File outFileParent = new File("./output");
	private static File csvOutputDir = new File("./tsv");
	private static File inputDir = new File("./input");
	private static String startDate = "2015-04-20";
	private static String endDate = "2015-07-31";

	public static void main(String[] args) throws Exception {
		System.setOut(new PrintStream(new FileOutputStream("out.txt")));

		File[] files = inputDir.listFiles();
		for (File file : files) {
			JSONFilterRunner filterRunner = new JSONFilterRunner(file, outFileParent, startDate, endDate);
			filterRunner.run();
		}
		System.out.println("===== All valid pincodes =====");
		System.out.println(JSONOccurrencePincodeFilter.validPinCodeSet);
		System.out.println("==============================");
		String outputFile = String.format("Filtered_%s_%s_%s.tsv", "MK", startDate, endDate);
		WriteTSV csv = new WriteTSV(outFileParent, csvOutputDir, outputFile);
		csv.run();
	}
}
