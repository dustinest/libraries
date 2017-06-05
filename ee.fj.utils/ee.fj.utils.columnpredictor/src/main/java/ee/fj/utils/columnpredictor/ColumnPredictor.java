package ee.fj.utils.columnpredictor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class ColumnPredictor {
	private final ColumnMatcher[] matchers;
	private final List<List<ColumnStatistics>> columnStatistics = new ArrayList<>();
	
	private static class ColumnStatistics {
		private long amount = 1;
		private final int column;
		private final ColumnMatcher matcher;
		ColumnStatistics(ColumnMatcher matcher, int column) {
			this.matcher = matcher;
			this.column = column;
		}
		@Override
		public String toString() {
			return this.amount + "@" + this.column + ": " + this.matcher;
		}
	}

	private static final Comparator<MatchingProbability> COMPARATOR = new Comparator<MatchingProbability>() {
		@Override public int compare(MatchingProbability o1, MatchingProbability o2) {
			return Float.compare(o2.getProbability(), o1.getProbability());
		}
	};

	private static final Comparator<ColumnStatistics> COLUMN_STATISTICS_COMPARATOR = new Comparator<ColumnStatistics>() {
		@Override public int compare(ColumnStatistics o1, ColumnStatistics o2) {
			int rv = Long.compare(o2.amount, o1.amount);
			if (rv == 0) {
				return Integer.compare(o1.column, o2.column);
			}
			return rv;
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
						index = columnStatistics.get(columnIndex).size();
						columnStatistics.get(columnIndex).add(new ColumnStatistics(m, columnIndex));
					} else {
						columnStatistics.get(columnIndex).get(index).amount = columnStatistics.get(columnIndex).get(index).amount + 1;
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
		List<ColumnStatistics> data = new ArrayList<>();
		int[] maxSize = new int[columnStatistics.size()];
		long[] totals = new long[columnStatistics.size()];

		for (List<ColumnStatistics> colStatsList : columnStatistics) {
			for (ColumnStatistics col : colStatsList) {
				data.add(col);
				maxSize[col.column] = maxSize[col.column] + 1;
				totals[col.column] += col.amount;
			}
		}
		data.sort(COLUMN_STATISTICS_COMPARATOR);
		MatchingProbability[][] columnProbabilities = new MatchingProbability[columnStatistics.size()][];
		MatchingProbability[] bestProbabilities = new MatchingProbability[columnStatistics.size()];
		int[] pointer = new int[columnStatistics.size()];

		for (ColumnStatistics c : data) {
			if (columnProbabilities[c.column] == null) {
				columnProbabilities[c.column] = new MatchingProbability[maxSize[c.column]];
			}
			MatchingProbability toAdd = getMatchingProbabolity(totals[c.column], c);
			columnProbabilities[c.column][pointer[c.column]] = toAdd;
			if (bestProbabilities[c.column] == null) {
				boolean toSet = true;
				// check if smaller value exists
				for (int col = 0; col < columnStatistics.size(); col ++) {
					if (bestProbabilities[col] != null && bestProbabilities[col].getColumn() == toAdd.getColumn()) {
						if (bestProbabilities[col].getProbability() >= toAdd.getProbability()) {
							toSet = false;
						} else {
							bestProbabilities[col] = null;
						}
					}
				}
				if (toSet) {
					bestProbabilities[c.column] = toAdd;
				}
			}
			 
			pointer[c.column]++;
		}
		this.result = getNewInstance(columnProbabilities, bestProbabilities);
		return this.result;
	}

	public void reset() {
		synchronized (columnStatistics) {
			columnStatistics.clear();
			result = null;
		}
	}
	
	public ColumnPredictor newInstance() {
		return new ColumnPredictor(matchers);
	}

	final Function<Boolean, Boolean> MAP_TO_BOOLEAN = t -> t;
	final Function<Boolean, Float> MAP_TO_FLOAT = t -> 0f;

	BiFunction<MatchingProbability, Class<? extends ColumnMatcher>, Boolean> CLASS_FILTER = (t, u) -> t.getColumn().getClass() == u;
	BiFunction<MatchingProbability, String, Boolean> LABEL_FILTER = (t, u) -> t.getColumn().getLabel() == u;

	private MatchingResult getNewInstance(MatchingProbability[][] probabilities, MatchingProbability[] bestColumns) {
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

			private <T> int getBestColumn(T value, BiFunction<MatchingProbability, T, Boolean> filter) {
				for (int column = 0; column < bestColumns.length; column++) {
					if (bestColumns[column] != null && filter.apply(bestColumns[column], value)) {
						return column;
					}
				}
				return -1;
			}
			
			@Override
			public int getBestResultFor(String columnLabel) {
				if (columnLabel == null) {
					return -1;
				}
				return getBestColumn(columnLabel, LABEL_FILTER);
			}

			@Override
			public <T extends ColumnMatcher> int getBestResultFor(Class<T> columnClass) {
				if (columnClass == null) {
					return -1;
				}
				return getBestColumn(columnClass, CLASS_FILTER);
			}
			
		};
	}
	
}
