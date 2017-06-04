package ee.fj.utils.columnpredictor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ColumnPredictor {
	private final ColumnMatcher[] matchers;
	private final List<List<ColumnStatistics>> columnStatistics = new ArrayList<>();
	private long rows = 0;
	
	private static class ColumnStatistics {
		private long amount = 1;
		private final ColumnMatcher matcher;
		ColumnStatistics(ColumnMatcher matcher) {
			this.matcher = matcher;
		}

		void increase() {
			this.amount ++;
		}
	}

	private static final Comparator<MatchingProbability> COMPARATOR = new Comparator<MatchingProbability>() {
		@Override public int compare(MatchingProbability o1, MatchingProbability o2) {
			return Float.compare(o2.getProbability(), o1.getProbability());
		}
	};
	
	public <T extends ColumnMatcher> ColumnPredictor(T... matchers) {
		this.matchers = matchers;
	}

	/**
	 * Add new row data
	 * @param value first column, may also be native array of Objects (Object[])
	 * @param others other columns
	 */
	public void addRow(Object value, Object... others) {
		int index = 0;
		if (value != null && value.getClass().isArray()) {
			for (Object o : (Object[])value) {
				setColumn(index++, o);
			}
		} else {
			setColumn(index++, value);
		}
		for (Object o : others) {
			setColumn(index++, o);
		}
		rows++;
	}

	private void setColumn(int columnIndex, Object value) {
		synchronized (columnStatistics) {
			if (result != null)
				result = null;

			for (ColumnMatcher m : matchers) {
				if (m.matches(value)) {
					while(columnStatistics.size() <= columnIndex) {
						columnStatistics.add(new ArrayList<>());
					}
					int index = -1;
					for (int i = 0; i < columnStatistics.get(columnIndex).size(); i++) {
						if (columnStatistics.get(columnIndex).get(i).matcher == m) {
							index = i;
							break;
						}
					}
					if (index < 0) {
						columnStatistics.get(columnIndex).add(new ColumnStatistics(m));
					} else {
						columnStatistics.get(columnIndex).get(index).increase();
					}
				}
				
			}
		}
	}

	private MatchingResult result = null;

	public MatchingResult calculate() {
		synchronized (columnStatistics) {
			if (this.result != null) {
				return this.result;
			}
			return calculateInnter();
		}
	}

	private MatchingProbability getMatchingProbabolity(long maxAmount, ColumnStatistics stats) {
		final float probability = (float)Math.floor(stats.amount * 10000d / maxAmount) / 100f;
		final ColumnMatcher matcher = stats.matcher;
		return new MatchingProbability() {
			@Override public ColumnMatcher getColumn() { return matcher; }
			@Override public float getProbability() { return probability; }
		};
	}
	
	private MatchingResult calculateInnter() {
		MatchingProbability[][] columnProbabilities = new MatchingProbability[columnStatistics.size()][];
		for (int column = 0; column < columnStatistics.size(); column ++) {
			MatchingProbability[] probability = new MatchingProbability[columnStatistics.get(column).size()];
			long total = 0;
			for (ColumnStatistics p : columnStatistics.get(column)) {
				total += p.amount;
			}
			for (int probIndex = 0; probIndex < columnStatistics.get(column).size(); probIndex++) {
				probability[probIndex] = getMatchingProbabolity(total, columnStatistics.get(column).get(probIndex));
			}
			if (probability.length > 0) {
				Arrays.sort(probability, COMPARATOR);
			}
			columnProbabilities[column] = probability;
		}
		this.result = getNewInstance(columnProbabilities);
		return this.result;
	}

	public void reset() {
		synchronized (columnStatistics) {
			columnStatistics.clear();
			result = null;
			rows = 0;
		}
	}
	
	public ColumnPredictor newInstance() {
		return new ColumnPredictor(matchers);
	}

	final Function<Boolean, Boolean> MAP_TO_BOOLEAN = t -> t;
	final Function<Boolean, Float> MAP_TO_FLOAT = t -> 0f;

	BiFunction<MatchingProbability, Class<? extends ColumnMatcher>, Boolean> CLASS_FILTER = (t, u) -> t.getColumn().getClass() == u;
	BiFunction<MatchingProbability, String, Boolean> LABEL_FILTER = (t, u) -> t.getColumn().getLabel() == u;

	private MatchingResult getNewInstance(MatchingProbability[][] probabilities) {

		return new MatchingResult() {

			private <T, S> T testValue(int columnIndex, S something,
					Function<Boolean, T> mapResult,
					BiFunction<MatchingProbability, S, Boolean> filter,
					Function<Stream<MatchingProbability>, T> callback) {
				if (something == null && probabilities.length >= columnIndex) {
					return mapResult.apply(true);
				}
				if (probabilities.length <= columnIndex) {
					return mapResult.apply(false);
				}
				if (something == null && probabilities[columnIndex] == null) {
					return  mapResult.apply(true);
				}
				if (probabilities[columnIndex] == null) {
					return mapResult.apply(false);
				}

				return callback.apply(Arrays.stream(probabilities[columnIndex]).filter(maching -> filter.apply(maching, something)));
			}

			@Override
			public boolean isColumnAt(int columnIndex, String columnLabel) {
				return testValue(columnIndex, columnLabel, MAP_TO_BOOLEAN, LABEL_FILTER, stream -> {
						return stream.count() > 0;
					}
				);
			}

			@Override
			public <T extends ColumnMatcher> boolean isColumnAt(int columnIndex, Class<T> columnClass) {
				return testValue(columnIndex, columnClass, MAP_TO_BOOLEAN, CLASS_FILTER, stream -> {
					return stream.count() > 0;
				});
			}

			@Override
			public float getProbabilityAt(int columnIndex, String columnLabel) {
				return testValue(columnIndex, columnLabel, MAP_TO_FLOAT, LABEL_FILTER, stream -> {
					return stream.map(MatchingProbability::getProbability).findAny().orElse(0f);
				});
			}

			@Override
			public <T extends ColumnMatcher> float getProbabilityAt(int columnIndex, Class<T> columnClass) {
				return testValue(columnIndex, columnClass, MAP_TO_FLOAT, CLASS_FILTER, stream -> {
					return stream.map(MatchingProbability::getProbability).findAny().orElse(0f);
				});
			}

			@Override
			public int getColumnsCount() {
				return probabilities != null ? probabilities.length : 0;
			}
			
		};
	}
	
}
