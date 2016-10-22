package ee.fj.utils.columnpredictor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnPredictor {
	private final ColumnMatcher[] matchers;
	private final List<Map<String, Integer>> matchingColumns = new ArrayList<>();


	public ColumnPredictor(ColumnMatcher... matchers) {
		this.matchers = matchers;
	}

	public void addColumns(Object[] value) {
		for (int i = 0; i < value.length; i++) {
			add(i, value[i]);
		}
	}
	
	public void add(int column, Object value) {
		synchronized (matchingColumns) {
			if (result != null)
				result = null;
			for (ColumnMatcher m : matchers) {
				if (m.matches(value)) {
					while(matchingColumns.size() <= column) {
						matchingColumns.add(new HashMap<>());
					}
					if (!matchingColumns.get(column).containsKey(m.getId())) {
						matchingColumns.get(column).put(m.getId(), 1);
					} else {
						matchingColumns.get(column).put(m.getId(), matchingColumns.get(column).get(m.getId()) + 1);
					}
				}
				
			}
		}
	}

	private MatchingResult result = null;
	public MatchingResult calculate() {
		synchronized (matchingColumns) {
			if (this.result != null) {
				return this.result;
			}
			List<String> result = new ArrayList<>();
			for (ColumnMatcher m : matchers) {
				int maxValue = Integer.MIN_VALUE;
				int maxCol = -1;
				for (int col = 0; col < matchingColumns.size(); col++) {
					 if (matchingColumns.get(col).containsKey(m.getId())) {
						 if (maxValue < matchingColumns.get(col).get(m.getId())) {
							 maxValue = matchingColumns.get(col).get(m.getId());
							 maxCol = col;
						 }
					 }
				}
				if (maxCol > -1) {
					while(result.size() <= maxCol) {
						result.add(null);
					}
					result.set(maxCol, m.getId());
				}
			}
			this.result = new MatchingResult(result.toArray(new String[result.size()]));
			matchingColumns.clear();
			return this.result;
		}
	}

	
	public ColumnPredictor newInstance() {
		return new ColumnPredictor(matchers);
	}
}
