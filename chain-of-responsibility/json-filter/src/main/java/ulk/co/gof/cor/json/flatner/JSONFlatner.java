package ulk.co.gof.cor.json.flatner;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONFlatner {

	private static final char LINE_SEPERATOR = '\n';
	private static final String S_NO = "S.No";
	private final List<Map<String, String>> flattenList = new LinkedList<Map<String,String>>();
	private final Set<String> headerSet = new LinkedHashSet<String>();
	private Map<String,String> tempRowMap;
	private final String delemitor;
	private String dataRootEntity;
	private Reader reader;
	private boolean hasSerialNoIncluded;

	public JSONFlatner hasSerialNoIncluded() {
		hasSerialNoIncluded = true;
		headerSet.add(S_NO);
		return this;
	}

	public JSONFlatner(Reader reader, String dataRootEntity, String delemitor) {
		this.reader = reader;
		this.dataRootEntity = dataRootEntity;
		this.delemitor = delemitor;
	}

	public JSONFlatner visitAndFlatenToTable(Reader reader) throws IOException, ParseException {
		this.reader = reader;
		return visitAndFlatenToTable();
	}

	public JSONFlatner visitAndFlatenToTable() throws IOException, ParseException {

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
		if (dataRootEntity == null){
			dataRootEntity = "";
		}

		JSONArray dataArray = (JSONArray) jsonObject.get(dataRootEntity);

		Object[] jsonEntities = dataArray.toArray();
		for (int i = 0; i < jsonEntities.length; i++) {
			tempRowMap = new LinkedHashMap<String, String>();
			if (hasSerialNoIncluded){
				tempRowMap .put(S_NO, String.valueOf(i+1));
			}
			extractJSONEntity(jsonEntities[i], "");
			flattenList.add(tempRowMap);
		}
		return this;
	}

	public JSONFlatner writeHeaderTo(Writer writer) throws IOException{
		Writer stringWriter = new StringWriter();
		Iterator<String> itr = headerSet.iterator();
		if (itr.hasNext()) {
			stringWriter.append(itr.next());
		}
		while (itr.hasNext()) {
			stringWriter.append(delemitor);
			stringWriter.append(itr.next());
		}
		writer.write(stringWriter.toString());
		writer.append(LINE_SEPERATOR);
		writer.flush();
		return this;
	}

	public JSONFlatner writeValuesTo(Writer writer) throws IOException{
		Writer stringWriter = new StringWriter();
		for (Map<String,String> rowMap : flattenList) {
			stringWriter = new StringWriter();
			Iterator<String> itr = headerSet.iterator();
			if (itr.hasNext()) {
				stringWriter.write(String.valueOf(rowMap.get(itr.next())).replace("\t", "\\t"));
			}
			while (itr.hasNext()) {
				stringWriter.write(delemitor);
				stringWriter.write(String.valueOf(rowMap.get(itr.next())).replace("\t", "\\t"));
			}
			writer.write(stringWriter.toString().replace("\r", "").replace("\n", "\\n"));
			writer.append(LINE_SEPERATOR);
			writer.flush();
		}
		return this;
	}

	private void extractJSONEntity(Object object, String header) {
		if (object instanceof JSONObject)
		{
			Set<Map.Entry<String, Object>> attributeSet = ((JSONObject) object).entrySet();
			for (Entry<String, Object> entry : attributeSet)
			{
				String attribName = entry.getKey();
				if (header.length() > 0)
				{
					attribName = header + "." + attribName;
				}
				extractJSONEntity(entry.getValue(), attribName);
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
				extractJSONEntity(jsonEntities[i], headerStr);
			}
		}
		else
		{
			headerSet.add(header);
			tempRowMap.put(header, String.valueOf(object));
		}
	}
}
