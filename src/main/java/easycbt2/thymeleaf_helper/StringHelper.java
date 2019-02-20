package easycbt2.thymeleaf_helper;

public class StringHelper {
	public String substring(String str, int beginIndex, int endIndex) {
		if(str == null) {
			return null;
		}
		return str.substring(beginIndex, Math.min(str.length(), endIndex));
	}
}
