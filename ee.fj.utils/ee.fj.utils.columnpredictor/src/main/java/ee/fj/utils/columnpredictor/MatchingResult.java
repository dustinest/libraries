package ee.fj.utils.columnpredictor;

public class MatchingResult {
	private final String[] result;
	public MatchingResult(String[] result) {
		this.result = result;
	}

	public String getResultAt(int column) {
		return result.length >= column ? null : result[column];
	}

	public boolean isColumnAt(int column, String val) {
		return result[column] == null ? val == null : result[column].equals(val);
	}

	public int getColumn(String val) {
		for (int i = 0; i < result.length; i++) {
			if (isColumnAt(i, val)) {
				return i;
			}
		}
		return -1;
	}
}
