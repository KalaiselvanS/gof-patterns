package ulk.co.gof.cor.json.flatner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.json.simple.parser.ParseException;

public class WriteTSV {

	private File outputDir = new File("./tsv");
	private File inputDir = new File("./output");
	private String outputFile = "Filtered_JSON_files.tsv";

	public WriteTSV() {
	}
	public WriteTSV(File inputDir, File outputDir, String outFile) {
		this.inputDir = inputDir;
		this.outputDir = outputDir;
		this.outputFile = outFile;
		if (!outputDir.exists()){
			outputDir.mkdir();
		}
	}

	public void run() throws IOException {
		Writer writer = new BufferedWriter(new FileWriter(new File(outputDir, outputFile)));
		try {
			File[] files = inputDir.listFiles();
			if (files == null || files.length == 0) {
				return;
			}
			JSONFlatner flatner = new JSONFlatner(null, "data", "\t");
			//			flatner.hasSerialNoIncluded();
			for (File file : files) {
				FileReader reader = new FileReader(file);
				flatner.visitAndFlatenToTable(reader);
			}
			flatner.writeHeaderTo(writer)
			.writeValuesTo(writer);
			writer.flush();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		} finally {
			try{
				writer.flush();
				writer.close();
			} catch (Throwable e) {}
		}
	}
}
