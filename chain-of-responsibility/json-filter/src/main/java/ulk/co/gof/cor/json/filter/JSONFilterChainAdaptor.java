package ulk.co.gof.cor.json.filter;

public class JSONFilterChainAdaptor<J> implements JSONFilterChain<J> {

	private JSONFilterChain<J> nextChain;

	public J doFilter(J json) throws Throwable {
		if (this.nextChain == null || json == null) {
			return json;
		}
		return this.nextChain.doFilter(json);
	}

	public JSONFilterChain<J> nextChain(JSONFilterChain<J> jsonFilterChain) {
		return this.nextChain = jsonFilterChain;
	}

}
