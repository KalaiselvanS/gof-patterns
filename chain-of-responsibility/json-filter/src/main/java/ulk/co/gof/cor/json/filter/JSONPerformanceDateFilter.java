package ulk.co.gof.cor.json.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;

public class JSONPerformanceDateFilter extends JSONFilterChainAdaptor<JSONObject> {

	private final String datePattern = "yyy-MM-dd";
	private final String startDateKey;
	private final String endDateKey;
	private final Date startDate;
	private final Date endDate;

	public JSONPerformanceDateFilter(String startDateKey, String endDateKey,
			String datePattern, String startDate, String endDate) {
		this.startDateKey = startDateKey;
		this.endDateKey = endDateKey;
		SimpleDateFormat format = new SimpleDateFormat(datePattern);
		try {
			this.startDate = format.parse(String.valueOf(startDate));
			this.endDate = format.parse(String.valueOf(endDate));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	//	"start_date": "2013-04-10",
	//  "end_date": "2013-04-10",
	//jsonObject is one of performances. Validating the pincode
	@Override
	public JSONObject doFilter(JSONObject jsonObject) throws Throwable{
		Object startDateStr = jsonObject.get(startDateKey);
		Object endDateStr = jsonObject.get(endDateKey);

		if(startDateStr == null || endDateStr == null){
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat(datePattern);
		Date startDate = format.parse(String.valueOf(startDateStr));
		Date endDate = format.parse(String.valueOf(endDateStr));

		if (startDate.before(this.startDate) || endDate.after(this.endDate)) {
			return null;
		}
		return super.doFilter(jsonObject);
	}
}
