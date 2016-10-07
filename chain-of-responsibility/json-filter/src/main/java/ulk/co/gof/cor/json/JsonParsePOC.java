package ulk.co.gof.cor.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParsePOC {

	private static final String filePath = "./input/initial_data_0.json";

	public static void main(String[] args) {

		try {
			FileReader reader = new FileReader(filePath);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

			visitAndFlatenToTable(jsonObject);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}

	private static void visitAndFlatenToTable(JSONObject jsonObject) {
		JSONArray dataArray = (JSONArray) jsonObject.get("data");
		int i =0;
		for (Object obj : dataArray.toArray()) {
			System.out.println("=======[ "+ ++i +" ]============");
			JSONObject jsonEntity = (JSONObject) obj;
			extractJSONEntity(jsonEntity, "");
			System.out.println();
		}
	}

	private static Set<String> extractJSONEntity(Object object, String header) {
		Set<String> headers = new TreeSet<String>();
		if (object instanceof JSONObject)
		{
			JSONObject jsonObject = (JSONObject) object;
			Set<Map.Entry<String, Object>> attributeSet = jsonObject.entrySet();
			for (Entry<String, Object> entry : attributeSet)
			{
				String attribName = entry.getKey();
				if (header.length() > 0)
				{
					attribName = header + "." + attribName;
				}
				headers.addAll(extractJSONEntity(entry.getValue(), attribName));
			}
		}
		else if (object instanceof JSONArray)
		{
			JSONArray jsonArray = (JSONArray) object;
			Object[] jsonEntities = jsonArray.toArray();
			String headerStr = header;
			for (int i = 0; i < jsonEntities.length; i++) {
				if(i>0){
					headerStr = String.format("%s_%s", header, i);
				}
				headers.addAll(extractJSONEntity(jsonEntities[i], headerStr));
			}
		}
		else
		{
			headers.add(header);
			printCell(object, header);
		}

		return headers;
	}

	private static void printCell(Object object, String header) {
		String string = String.valueOf(object);
		string = string.replace("\t", "\\t").replace("\n", "\\n");
		System.out.format("\t%s%s%s\n", header, " : ", string);
	}

}