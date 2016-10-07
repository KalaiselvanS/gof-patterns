package ulk.co.gof.cor.json.filter;

public interface JSONFilterChain<J> {

	public abstract J doFilter(J jsonObject) throws Throwable;

	public abstract JSONFilterChain<J> nextChain(JSONFilterChain<J> jsonFilterChain);

}
