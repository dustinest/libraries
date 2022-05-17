package com.foxjunior.utils.columnpredictor


import spock.lang.Specification

class ColumnPredictorSpec extends Specification {
	private MatchingResult result

	def setup() {
		ColumnPredictor predictor = new ColumnPredictor(
			new ColumnMatcher() {
				@Override
				String getLabel() { return "name_column" }

				@Override
				boolean matches(Object column) { return column == "name" }

			},
			EmailPatternMatcher.INSTANCE,
			UrlPatternMatcher.INSTANCE
		)
		predictor.addRow(new String[]{"uhhuu@ahaa.ee", "name", "other"})
		predictor.addRow(new String[]{"uhhuu@ahaa.ee", "teie", "name"})
		predictor.addRow("uhhuu@ahaa.ee", "you man!", "name", "uhhuu@ahaa.ee")
		predictor.addRow(new String[]{"ONLY US", "http://www.neti.ee", "name", "uhhuu@ahaa.ee"})
		predictor.addRow(new String[]{"ONLY US", "teie", "nimi", "uhhuu@ahaa.ee"})
		predictor.addRow("ONLY US", "you man!", "name", "uhhuu@ahaa.ee")
		result = predictor.calculate()
	}

	def "test columns count"() {
		expect:
			result.getColumnsCount() == 4
	}

	def "test best column labels"() {
		expect:
			result.getBestResultFor(EmailPatternMatcher.DEFAULT_NAME) == 3
			result.getBestResultFor("name_column") == 2
			result.getBestResultFor(UrlPatternMatcher.DEFAULT_NAME) == 1
			result.getBestResultFor("does not exist") == -1
	}

	def "test best column class"() {
		expect:
			result.getBestResultFor(EmailPatternMatcher.class) == 3
			result.getBestResultFor(UrlPatternMatcher.class) == 1
	}

	def "test column labels"() {
		expect:
			result.getProbabilityAt(0, EmailPatternMatcher.DEFAULT_NAME) == 100f
			result.getProbabilityAt(0, UrlPatternMatcher.DEFAULT_NAME) == 0
			result.getProbabilityAt(0, "name_column") == 0

			result.getProbabilityAt(1, EmailPatternMatcher.DEFAULT_NAME) == 0
			result.getProbabilityAt(1, UrlPatternMatcher.DEFAULT_NAME) == 50f
			result.getProbabilityAt(1, "name_column") == 50f

			result.getProbabilityAt(2, EmailPatternMatcher.DEFAULT_NAME) == 0
			result.getProbabilityAt(2, UrlPatternMatcher.DEFAULT_NAME) == 0f
			result.getProbabilityAt(2, "name_column") == 100f

			result.getProbabilityAt(3, EmailPatternMatcher.DEFAULT_NAME) == 100f
			result.getProbabilityAt(3, UrlPatternMatcher.DEFAULT_NAME) == 0
			result.getProbabilityAt(3, "name_column") == 0

			result.getProbabilityAt(4, EmailPatternMatcher.DEFAULT_NAME) == 0
			result.getProbabilityAt(4, UrlPatternMatcher.DEFAULT_NAME) == 0
			result.getProbabilityAt(4, "name_column") == 0

			for (int i = 0; i < result.getColumnsCount(); i++) {
				assert result.getProbabilityAt(i, "does not exist") == 0
			}
	}

	def "test column classes"() {
		expect:
			result.getProbabilityAt(0, EmailPatternMatcher.class) == 100f
			result.getProbabilityAt(0, UrlPatternMatcher.class) == 0

			result.getProbabilityAt(1, EmailPatternMatcher.class) == 0
			result.getProbabilityAt(1, UrlPatternMatcher.class) == 50f

			result.getProbabilityAt(2, EmailPatternMatcher.class) == 0
			result.getProbabilityAt(2, UrlPatternMatcher.class) == 0

			result.getProbabilityAt(3, EmailPatternMatcher.class) == 100f
			result.getProbabilityAt(3, UrlPatternMatcher.class) == 0

			result.getProbabilityAt(4, EmailPatternMatcher.class) == 0
			result.getProbabilityAt(4, UrlPatternMatcher.class) == 0
	}

	def "is column label at"() {
		expect:
			result.isColumnAt(0, EmailPatternMatcher.DEFAULT_NAME)
			!result.isColumnAt(0, UrlPatternMatcher.DEFAULT_NAME)
			!result.isColumnAt(0, "name_column")

			!result.isColumnAt(1, EmailPatternMatcher.DEFAULT_NAME)
			result.isColumnAt(1, UrlPatternMatcher.DEFAULT_NAME)
			result.isColumnAt(1, "name_column")

			!result.isColumnAt(2, EmailPatternMatcher.DEFAULT_NAME)
			!result.isColumnAt(2, UrlPatternMatcher.DEFAULT_NAME)
			result.isColumnAt(2, "name_column")

			result.isColumnAt(3, EmailPatternMatcher.DEFAULT_NAME)
			!result.isColumnAt(3, UrlPatternMatcher.DEFAULT_NAME)
			!result.isColumnAt(3, "name_column")

			!result.isColumnAt(4, EmailPatternMatcher.DEFAULT_NAME)
			!result.isColumnAt(4, UrlPatternMatcher.DEFAULT_NAME)
			!result.isColumnAt(4, "name_column")

			for (int i = 0; i < result.getColumnsCount(); i++) {
				assert !result.isColumnAt(i, "does not exist")
			}
	}

	def "is column class at"() {
		expect:
			result.isColumnAt(0, EmailPatternMatcher.class)
			!result.isColumnAt(0, UrlPatternMatcher.class)

			!result.isColumnAt(1, EmailPatternMatcher.class)
			result.isColumnAt(1, UrlPatternMatcher.class)

			!result.isColumnAt(2, EmailPatternMatcher.class)
			!result.isColumnAt(2, UrlPatternMatcher.class)

			result.isColumnAt(3, EmailPatternMatcher.class)
			!result.isColumnAt(3, UrlPatternMatcher.class)

			!result.isColumnAt(4, EmailPatternMatcher.class)
			!result.isColumnAt(4, UrlPatternMatcher.class)
	}
}
