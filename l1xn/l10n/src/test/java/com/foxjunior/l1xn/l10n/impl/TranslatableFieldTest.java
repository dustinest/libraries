package com.foxjunior.l1xn.l10n.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TranslatableFieldTest {

	@Test
	public void simpleTranslatableTest() {
		TranslatableField[] fields = TranslatableField.getTranslatables("Mina ${name} olen \\${test} ${sugu} ${");
		StringBuilder b = new StringBuilder();
		for (TranslatableField f : fields) {
			b.append(f.getValue(new String[]{}, new String[]{}));
		}
		Assertions.assertEquals("Mina name olen \\${test} sugu ${", b.toString());
	}

	@Test
	public void testIfLabelIsFirst() {
		TranslatableField[] fields = TranslatableField.getTranslatables("${name} olen \\${test} ${sugu} ${");
		StringBuilder b = new StringBuilder();
		for (TranslatableField f : fields) {
			b.append(f.getValue(new String[]{}, new String[]{}));
		}
		Assertions.assertEquals("name olen \\${test} sugu ${", b.toString());
	}

	@Test
	public void testHappyTranslatin() {
		TranslatableField[] fields = TranslatableField.getTranslatables("Mina ${name} olen \\${test} ${sugu} ${");
		StringBuilder b = new StringBuilder();

		Object[] vals = new Object[]{"Peeter Paan", "mees"};
		String[] keys = new String[]{"name", "sugu"};

		for (TranslatableField f : fields) {
			b.append(f.getValue(keys, vals));
		}
		Assertions.assertEquals("Mina Peeter Paan olen \\${test} mees ${", b.toString());
	}

	@Test
	public void testMixedBags() {
		TranslatableField[] fields = TranslatableField.getTranslatables("Mina ${name} olen \\${test} ${sugu} ${");
		StringBuilder b = new StringBuilder();

		Object[] vals = new Object[]{"Peeter Paan", "mees"};
		String[] keys = new String[]{"sugu", "name"};

		for (TranslatableField f : fields) {
			b.append(f.getValue(keys, vals));
		}
		Assertions.assertEquals("Mina mees olen \\${test} Peeter Paan ${", b.toString());
	}

	@Test
	public void addNonTranslatables() {
		TranslatableField[] fields = TranslatableField.getTranslatables("Mina ${name} olen \\${test} ${sugu} ${");
		StringBuilder b = new StringBuilder();

		Object[] vals = new Object[]{"Peeter Paan", "mees"};
		String[] keys = new String[]{"test", "name"};

		for (TranslatableField f : fields) {
			b.append(f.getValue(keys, vals));
		}
		Assertions.assertEquals("Mina mees olen \\${test} sugu ${", b.toString());
	}

	@Test
	public void testMoreKeys() {
		TranslatableField[] fields = TranslatableField.getTranslatables("Mina ${name} olen \\${test} ${sugu} ${");
		StringBuilder b = new StringBuilder();

		Object[] vals = new Object[]{"Peeter Paan"};
		String[] keys = new String[]{"sugu", "name"};

		for (TranslatableField f : fields) {
			b.append(f.getValue(keys, vals));
		}
		Assertions.assertEquals("Mina name olen \\${test} Peeter Paan ${", b.toString());
	}

	@Test
	public void testMoreValues() {
		TranslatableField[] fields = TranslatableField.getTranslatables("Mina ${name} olen \\${test} ${sugu} ${");
		StringBuilder b = new StringBuilder();

		Object[] vals = new Object[]{"Peeter Paan", "Nimeta"};
		String[] keys = new String[]{"sugu"};

		for (TranslatableField f : fields) {
			b.append(f.getValue(keys, vals));
		}
		Assertions.assertEquals("Mina name olen \\${test} Peeter Paan ${", b.toString());
	}
}
