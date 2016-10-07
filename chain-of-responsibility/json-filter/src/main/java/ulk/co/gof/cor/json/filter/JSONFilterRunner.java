package ulk.co.gof.cor.json.filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ulk.co.gof.cor.json.JSONToString;

public class JSONFilterRunner {

	private final File file;
	private File outFileParent = new File("./output");
	private String startDate = "2015-04-20";
	private String endDate = "2015-07-31";

	JSONDataArrayFilter dataArrayFilter = new JSONDataArrayFilter("data");
	JSONDataArrayFilter occurrencesArrayFilter = new JSONDataArrayFilter("occurrences");
	JSONOccurrencePincodeFilter occurrencePincodeFilter = new JSONOccurrencePincodeFilter("pincode");
	JSONDataArrayFilter performancesArrayFilter = new JSONDataArrayFilter("performances");
	JSONPerformanceDateFilter performanceDateFilter = new JSONPerformanceDateFilter(
			"start_date", "end_date", "yyy-MM-dd", startDate, endDate);

	private final JSONFilterChain<JSONObject> jsonRootFilter;

	public JSONFilterRunner(File file, File outFileParent, String startDate, String endDate) {
		this(file);
		this.startDate = startDate;
		this.endDate = endDate;
		this.outFileParent = outFileParent;
		if (!outFileParent.exists()){
			outFileParent.mkdir();
		}
	}
	public JSONFilterRunner(File file) {
		this.file = file;

		dataArrayFilter
		.nextChain(occurrencesArrayFilter)
		.nextChain(occurrencePincodeFilter)
		.nextChain(performancesArrayFilter)
		.nextChain(performanceDateFilter)
		;

		jsonRootFilter = dataArrayFilter;
	}

	public void run () throws Exception {
		try {
			FileReader reader = new FileReader(file);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

			JSONObject filteredJson = jsonRootFilter.doFilter(jsonObject);

			if (filteredJson != null) {
				File out = new File(outFileParent, "MK_"+file.getName());
				out.createNewFile();
				Writer writer = new BufferedWriter(new FileWriter(out));
				JSONToString.writeFormatedJSONString(writer, filteredJson, 0);
				writer.flush();
				writer.close();
				System.out.println(file.getName()
						+ " has records with filter conditions!");
				System.out.println(file.getName() + " file has the below postal codes");
				System.out.println(occurrencePincodeFilter.thisFileValidPinCodeSet);
			} else {
				System.out.println(file.getName() + " has no records with filter conditions!");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println(file.getName() + " is not processed :(");
			//			throw new RuntimeException(e);
		} finally {
		}
	}
}
