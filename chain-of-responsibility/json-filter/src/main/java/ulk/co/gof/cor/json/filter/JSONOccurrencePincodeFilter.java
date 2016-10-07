package ulk.co.gof.cor.json.filter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

import org.json.simple.JSONObject;

public class JSONOccurrencePincodeFilter extends JSONFilterChainAdaptor<JSONObject> {

	private final String dataRootEntity;
	private final TreeSet<String> pinCodeSet = new TreeSet<String>();
	public static TreeSet<String> validPinCodeSet = new TreeSet<String>();
	public TreeSet<String> thisFileValidPinCodeSet = new TreeSet<String>();

	public JSONOccurrencePincodeFilter(String dataRootEntity) {
		if (dataRootEntity == null){
			this.dataRootEntity = "";
		} else {
			this.dataRootEntity = dataRootEntity;
		}
		loadPinCodes();
	}

	//jsonObject is one of occurrences. Validating the pincode
	@Override
	public JSONObject doFilter(JSONObject jsonObject) throws Throwable {//dataRootEntity = pincode
		Object pincode = jsonObject.get(dataRootEntity);

		if (isInValid(String.valueOf(pincode))) {// or not in business pincodes list
			return null;
		}
		validPinCodeSet.add(pincode.toString());
		thisFileValidPinCodeSet.add(pincode.toString());
		return super.doFilter(jsonObject); // further filtering for performances
	}

	private boolean isInValid(String pincode) {
		if (pincode == null || (pincode = pincode.trim()).length() == 0 ) {
			return true;
		}
		for (String pin : pinCodeSet) {
			if (pincode.startsWith(pin) || pin.trim().equals(pincode)) {
				return false;
			}
		}
		return true;
	}

	private void loadPinCodes() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = br.readLine();
			while (line != null) {
				line = line.trim().replace(",", "");
				if(line.length()==0){continue;}
				pinCodeSet.add(line);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final String file = "required-pincodes.txt";
}
