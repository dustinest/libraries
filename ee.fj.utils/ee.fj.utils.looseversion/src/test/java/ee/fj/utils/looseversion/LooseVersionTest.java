package ee.fj.utils.looseversion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LooseVersionTest {
	@Test
	public void simpleTest() {

		Assertions.assertEquals(0, LooseVersion.compare("1.1.1", "1.1.1"));
		Assertions.assertEquals(-1, LooseVersion.compare("1.1.1", "1.1.1.1"));

		Assertions.assertEquals(1, LooseVersion.compare("1.1.1.1", "1.1.1"));

		Assertions.assertEquals(-1, LooseVersion.compare("1.1.1", "1.1.2"));
		Assertions.assertEquals(1, LooseVersion.compare("1.1.2", "1.1.1"));
		Assertions.assertEquals(-1, LooseVersion.compare("1.1.a", "1.1.1"));
		Assertions.assertEquals(1, LooseVersion.compare("1.1.1", "1.1.a"));
		Assertions.assertEquals(1, LooseVersion.compare("1.1.1-a", "1.1.1-b"));
		Assertions.assertEquals(-1, LooseVersion.compare("1.1.1-b", "1.1.1-a"));

		Assertions.assertEquals(1, LooseVersion.compare("1.1.11", "1.1.1"));
		Assertions.assertEquals(-1, LooseVersion.compare("1.1.1", "1.1.11"));
		Assertions.assertEquals(-1, LooseVersion.compare("1.1.11", "1.1.12"));
		Assertions.assertEquals(-1, LooseVersion.compare("1.1.11", "1.1-12"));
		Assertions.assertEquals(0, LooseVersion.compare("1.1.12", "1.1-12"));

	}
}
