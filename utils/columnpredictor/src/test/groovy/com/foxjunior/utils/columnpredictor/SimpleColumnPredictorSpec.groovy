package com.foxjunior.utils.columnpredictor


import spock.lang.Specification

class SimpleColumnPredictorSpec extends Specification {
	private MatchingResult result

	def setup() {
		ColumnPredictor predictor = new ColumnPredictor(
			EmailPatternMatcher.INSTANCE,
			UrlPatternMatcher.INSTANCE,
			NumberPatternMatcher.INSTANCE,
			IntegerPatternMatcher.INSTANCE
		)
		predictor.addRow("uhhuu@ahaa.ee", 1)
		predictor.addRow("uhhuu@ahaa.ee")
		predictor.addRow("uhhuu@ahaa.ee")
		predictor.addRow("somethins else", "http://www.neti.ee")
		result = predictor.calculate()
	}

	def EmptyTest() {
		given:
			ColumnPredictor predictor = new ColumnPredictor(
				EmailPatternMatcher.INSTANCE,
				UrlPatternMatcher.INSTANCE,
				NumberPatternMatcher.INSTANCE,
				IntegerPatternMatcher.INSTANCE
			)
		when:
			predictor.addRow("abc", "cde")
			MatchingResult result = predictor.calculate()
		then:
			result.getBestResultFor(EmailPatternMatcher.DEFAULT_NAME) == -1
			result.getBestResultFor(UrlPatternMatcher.DEFAULT_NAME) == -1
			result.getBestResultFor(NumberPatternMatcher.DEFAULT_NAME) == -1
			result.getBestResultFor(IntegerPatternMatcher.DEFAULT_NAME) == -1
			result.getBestResultFor("does not exist") == -1
	}

	def testColumnsCount() {
		expect:
			result.getColumnsCount() == 2
	}

	def testBestColumnLabels() {
		expect:
			result.getBestResultFor(EmailPatternMatcher.DEFAULT_NAME) == 0
			result.getBestResultFor(UrlPatternMatcher.DEFAULT_NAME) == 1
			result.getBestResultFor("does not exist") == -1
	}

	def testBestColumnClass() {
		expect:
			result.getBestResultFor(EmailPatternMatcher.class) == 0
			result.getBestResultFor(UrlPatternMatcher.class) == 1
	}

	def testColumnLabels() {
		expect:
			result.getProbabilityAt(0, EmailPatternMatcher.DEFAULT_NAME) == 100f
			result.getProbabilityAt(0, UrlPatternMatcher.DEFAULT_NAME) == 0

			result.getProbabilityAt(1, EmailPatternMatcher.DEFAULT_NAME) == 0
			result.getProbabilityAt(1, UrlPatternMatcher.DEFAULT_NAME) == 100f

			result.getProbabilityAt(2, EmailPatternMatcher.DEFAULT_NAME) == 0
			result.getProbabilityAt(2, UrlPatternMatcher.DEFAULT_NAME) == 0
			for (int i = 0; i < result.getColumnsCount(); i++) {
				assert result.getProbabilityAt(i, "does not exist") == 0
			}
	}

	def testColumnClasses() {
		expect:
			result.getProbabilityAt(0, EmailPatternMatcher.class) == 100f
			result.getProbabilityAt(0, UrlPatternMatcher.class) == 0

			result.getProbabilityAt(1, EmailPatternMatcher.class) == 0
			result.getProbabilityAt(1, UrlPatternMatcher.class) == 100

			result.getProbabilityAt(2, EmailPatternMatcher.class) == 0
			result.getProbabilityAt(2, UrlPatternMatcher.class) == 0
	}

	def isColumnLabelAt() {
		expect:
			result.isColumnAt(0, EmailPatternMatcher.DEFAULT_NAME)
			!result.isColumnAt(0, UrlPatternMatcher.DEFAULT_NAME)

			!result.isColumnAt(1, EmailPatternMatcher.DEFAULT_NAME)
			result.isColumnAt(1, UrlPatternMatcher.DEFAULT_NAME)

			!result.isColumnAt(2, EmailPatternMatcher.DEFAULT_NAME)
			!result.isColumnAt(2, UrlPatternMatcher.DEFAULT_NAME)

			for (int i = 0; i < result.getColumnsCount(); i++) {
				assert !result.isColumnAt(i, "does not exist")
			}
	}

	def isColumnClassAt() {
		expect:
			result.isColumnAt(0, EmailPatternMatcher.class)
			!result.isColumnAt(0, UrlPatternMatcher.class)

			!result.isColumnAt(1, EmailPatternMatcher.class)
			result.isColumnAt(1, UrlPatternMatcher.class)

			!result.isColumnAt(2, EmailPatternMatcher.class)
			!result.isColumnAt(2, UrlPatternMatcher.class)
	}

}
