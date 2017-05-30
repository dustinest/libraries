package ee.fj.utils.looseversion;

import org.junit.Assert;
import org.junit.Test;

public class LooseVersionTest {
	@Test
	public void simpleTest() {

		Assert.assertEquals(0, LooseVersion.compare("1.1.1", "1.1.1"));
		Assert.assertEquals(-1, LooseVersion.compare("1.1.1", "1.1.1.1"));

		Assert.assertEquals(1, LooseVersion.compare("1.1.1.1", "1.1.1"));

		Assert.assertEquals(-1, LooseVersion.compare("1.1.1", "1.1.2"));
		Assert.assertEquals(1, LooseVersion.compare("1.1.2", "1.1.1"));
		Assert.assertEquals(-1, LooseVersion.compare("1.1.a", "1.1.1"));
		Assert.assertEquals(1, LooseVersion.compare("1.1.1", "1.1.a"));
		Assert.assertEquals(1, LooseVersion.compare("1.1.1-a", "1.1.1-b"));
		Assert.assertEquals(-1, LooseVersion.compare("1.1.1-b", "1.1.1-a"));

		Assert.assertEquals(1, LooseVersion.compare("1.1.11", "1.1.1"));
		Assert.assertEquals(-1, LooseVersion.compare("1.1.1", "1.1.11"));
		Assert.assertEquals(-1, LooseVersion.compare("1.1.11", "1.1.12"));
		Assert.assertEquals(-1, LooseVersion.compare("1.1.11", "1.1-12"));
		Assert.assertEquals(0, LooseVersion.compare("1.1.12", "1.1-12"));

	}
}
