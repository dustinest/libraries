package ee.fj.utils.columnpredictor;

public interface MatchingProbability {
	/**
	 * Get column
	 * @return the column type at column index
	 */
	public ColumnMatcher getColumn();

	/**
	 * get probability of this column
	 * @return percentage of the probability
	 */
	public float getProbability();
}
