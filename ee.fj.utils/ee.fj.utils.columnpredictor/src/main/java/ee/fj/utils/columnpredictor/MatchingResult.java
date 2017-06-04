package ee.fj.utils.columnpredictor;

public interface MatchingResult {
	/**
	 * Return true if {@link ColumnMatcher#getLabel()} matches to the label at column index
	 * @param columnIndex the index of the column
	 * @param columnLabel {@link ColumnMatcher#getLabel()} value
	 * @return true if the label match at column index
	 */
	public boolean isColumnAt(int columnIndex, String columnLabel);

	/**
	 * Return true if column class match at index
	 * @param columnIndex the index of the column
	 * @param columnClass the column type
	 * @return true if the column type match at column index
	 */
	public <T extends ColumnMatcher> boolean isColumnAt(int columnIndex, Class<T> columnClass);

	/**
	 * Return percentage probability if {@link ColumnMatcher#getLabel()} matches to the label at column index
	 * @param columnIndex the index of the column
	 * @param columnLabel {@link ColumnMatcher#getLabel()} value
	 * @return probability this column contains the matcher label
	 */
	public float getProbabilityAt(int columnIndex, String columnLabel);

	/**
	 * Return probability of this maching class
	 * @param columnIndex the index of the column
	 * @param columnClass the column type
	 * @return return probability percentage this class exists
	 */
	public <T extends ColumnMatcher> float getProbabilityAt(int columnIndex, Class<T> columnClass);

	/**
	 * @return amount of columns
	 */
	public int getColumnsCount();
}
