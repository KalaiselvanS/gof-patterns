package ulk.co.gof.cor.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("rawtypes")
public class JSONToString {

	private static final String INDENT = "    ";

	public static void writeFormatedJSONString(Writer out, Object value, int indent)
			throws IOException {
		if (value instanceof Map) {
			Map map = (Map) value;
			Iterator iter = map.entrySet().iterator();
			print(out, "{", indent);
			writeFormatedJSONString(out, iter, indent+1);
			print(out, "}", indent);
		} else if (value instanceof List) {
			Iterator iter = ((List) value).iterator();
			print(out, "[", 0);
			writeFormatedJSONString(out, iter, indent+1);
			print(out, "]", indent);
		} else if (value instanceof Iterator) {
			Iterator iter = (Iterator) value;
			if (iter.hasNext()){
				println(out, "", 0);
				writeFormatedJSONString(out, iter.next(), indent);
				while (iter.hasNext()) {
					println(out, ",", 0);
					writeFormatedJSONString(out, iter.next(), indent);
				}
				println(out, "", 0);
			}
		} else if (value instanceof Entry) {
			Entry entry = (Entry) value;
			print(out, "\"", indent);
			escape(String.valueOf(entry.getKey()), out);
			print(out, "\"", 0);
			out.write(" : ");
			writeFormatedJSONString(out, entry.getValue(), indent);
		} else if (value instanceof String) {
			print(out, "\"", 0);
			escape(String.valueOf(value), out);
			print(out, "\"", 0);
		} else {
			print(out, String.valueOf(value), 0);
		}
	}

	static boolean newLine;
	private static void println(Writer out, String s, int indent) throws IOException {
		print(out, s, indent);
		out.write('\n');
		newLine =true;
	}

	private static void print(Writer out, String s, int indent) throws IOException {
		while (newLine && indent-- > 0){
			out.write(INDENT);
		}
		out.write(s);
		newLine = false;
	}

	public static void writeJSONString(Writer out, Object value)
			throws IOException {
		if (value instanceof Map) {
			Map map = (Map) value;
			Iterator iter = map.entrySet().iterator();
			out.write('{');
			writeJSONString(out, iter);
			out.write('}');
		} else if (value instanceof List) {
			Iterator iter = ((List) value).iterator();
			out.write('[');
			writeJSONString(out, iter);
			out.write(']');
		} else if (value instanceof Iterator) {
			Iterator iter = (Iterator) value;
			if (iter.hasNext()){
				writeJSONString(out, iter.next());
			}
			while (iter.hasNext()) {
				out.write(',');
				writeJSONString(out, iter.next());
			}
		} else if (value instanceof Entry) {
			Entry entry = (Entry) value;
			out.write('\"');
			escape(String.valueOf(entry.getKey()), out);
			out.write('\"');
			out.write(':');
			writeJSONString(out, entry.getValue());
		} else if (value instanceof String) {
			out.write('\"');
			escape(String.valueOf(value), out);
			out.write('\"');
		} else {
			out.write(String.valueOf(value));
		}
	}

	private static void escape(String s, Writer writer) throws IOException {
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				writer.write("\\\"");
				break;
			case '\\':
				writer.write("\\\\");
				break;
			case '\b':
				writer.write("\\b");
				break;
			case '\f':
				writer.write("\\f");
				break;
			case '\n':
				writer.write("\\n");
				break;
			case '\r':
				writer.write("\\r");
				break;
			case '\t':
				writer.write("\\t");
				break;
				//			case '/':
				//				writer.write("\\/");
				//				break;
			default:
				// Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if ((ch >= '\u0000' && ch <= '\u001F')
						|| (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					writer.write("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						writer.write('0');
					}
					writer.write(ss.toUpperCase());
				} else {
					writer.write(ch);
				}
			}
		}
	}
}
