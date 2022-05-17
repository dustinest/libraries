package com.foxjunior.l1xn.l10n.impl

import spock.lang.Specification


class TranslatableFieldSpec extends Specification {
	private final TranslatableField[] MINA = TranslatableField.getTranslatables("Mina \${name} olen \\\${test} \${sugu} \${")

	def "simple translatable test"() {
		given:
			StringBuilder b = new StringBuilder()
		when:
			for (TranslatableField f : MINA) {
				b.append(f.getValue(new String[]{}, new String[]{}))
			}
		then:
			b.toString() == "Mina name olen \\\${test} sugu \${"
	}

	def "test if labelIs first"() {
		given:
			TranslatableField[] fields = TranslatableField.getTranslatables("\${name} olen \\\${test} \${sugu} \${")
			StringBuilder b = new StringBuilder()
		when:
			for (TranslatableField f : fields) {
				b.append(f.getValue(new String[]{}, new String[]{}))
			}
		then:
			b.toString() == "name olen \\\${test} sugu \${"
	}

	def "test happy translation"() {
		given:
			StringBuilder b = new StringBuilder()
			Object[] vals = new Object[]{"Peeter Paan", "mees"}
			String[] keys = new String[]{"name", "sugu"}
		when:
			for (TranslatableField f : MINA) {
				b.append(f.getValue(keys, vals))
			}
		then:
			b.toString() == "Mina Peeter Paan olen \\\${test} mees \${"
	}

	def "test mixed bags"() {
		given:
			StringBuilder b = new StringBuilder()
			Object[] vals = new Object[]{"Peeter Paan", "mees"}
			String[] keys = new String[]{"sugu", "name"}
		when:
			for (TranslatableField f : MINA) {
				b.append(f.getValue(keys, vals))
			}
		then:
			b.toString() == "Mina mees olen \\\${test} Peeter Paan \${"
	}

	def "add non translatable"() {
		given:
			StringBuilder b = new StringBuilder()
			Object[] vals = new Object[]{"Peeter Paan", "mees"}
			String[] keys = new String[]{"test", "name"}
		when:
			for (TranslatableField f : MINA) {
				b.append(f.getValue(keys, vals))
			}
		then:
			b.toString() == "Mina mees olen \\\${test} sugu \${"
	}

	def "with more keys"() {
		given:
			StringBuilder b = new StringBuilder()
			Object[] vals = new Object[]{"Peeter Paan"}
			String[] keys = new String[]{"sugu", "name"}
		when:
			for (TranslatableField f : MINA) {
				b.append(f.getValue(keys, vals))
			}
		then:
			b.toString() == "Mina name olen \\\${test} Peeter Paan \${"
	}

	def testMoreValues() {
		given:
			StringBuilder b = new StringBuilder()
			Object[] vals = new Object[]{"Peeter Paan", "Nimeta"}
			String[] keys = new String[]{"sugu"}
		when:
			for (TranslatableField f : MINA) {
				b.append(f.getValue(keys, vals))
			}
		then:
			b.toString() == "Mina name olen \\\${test} Peeter Paan \${"
	}
}
