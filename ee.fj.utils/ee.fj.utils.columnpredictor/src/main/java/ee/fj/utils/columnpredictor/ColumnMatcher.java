package ee.fj.utils.columnpredictor;

public interface ColumnMatcher {
	public boolean matches(Object column);
	public String getId();
}