package ulk.co.gof.cor.json.filter;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONDataArrayFilter extends JSONFilterChainAdaptor<JSONObject> {

	private final String dataRootEntity;

	public JSONDataArrayFilter(String dataRootEntity) {
		if (dataRootEntity == null){
			this.dataRootEntity = "";
		} else {
			this.dataRootEntity = dataRootEntity;
		}
	}

	@Override
	public JSONObject doFilter(JSONObject jsonObject) throws Throwable {//dataRootEntity = data | occurrences | performances
		JSONArray dataArray = (JSONArray) jsonObject.get(dataRootEntity);
		if (dataArray == null) {
			return null;
		}
		Iterator itr = dataArray.iterator();
		while (itr.hasNext()) {
			JSONObject json = (JSONObject) itr.next();
			if (super.doFilter(json) == null) {
				itr.remove();
			};
		}

		if (dataArray.size() == 0) {
			return null;
		}
		return jsonObject;
	}
}
