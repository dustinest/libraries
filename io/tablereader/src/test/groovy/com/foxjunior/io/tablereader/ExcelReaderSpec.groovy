package com.foxjunior.io.tablereader


import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.Month

class ExcelReaderSpec extends Specification {
	@Unroll
	def "File #fileName can be read"() {
		when:
			def result = read(TableReaderFactory.getReaderByExtendsion(".xls").as(ExcelReader.class), fileName)
		then:
			result == [15, 2, 2] as int[]
		where:
			fileName << ["simple.xls", "simple.xlsx"]
	}

	@Unroll
	def "Type #type work"() {
		expect:
			TableReaderFactory.getReader(type).as(ExcelReader.class) != null
		where:
			type << SupportedFiles.values().findAll {![SupportedFiles.TXT, SupportedFiles.CSV].contains(it)}
	}

	@Unroll
	def "extends #extension #name works"() {
		expect:
			TableReaderFactory.getReaderByExtendsion(extension).as(ExcelReader.class) != null
			TableReaderFactory.getReader(name + "." + extension).as(ExcelReader.class) != null
		where:
			[extension, name] << [["xls", "xlsx"], ["me", "my.file"]].combinations()
	}


	private static def read(ExcelReader reader, String fileName) throws IOException {
		//Šašlikul on kole nälg õäöüÕÄÖÜ
		String SASHLIK
		String TYINA
		try (BufferedReader testDataReader =  new BufferedReader(new InputStreamReader(ExcelReaderSpec.class.getClassLoader().getResourceAsStream("test.txt"), StandardCharsets.UTF_8))) {
			SASHLIK = testDataReader.readLine()
			TYINA = testDataReader.readLine()
		}

		int[] loadingAmount = [-1, -1, -1]
		try (
		InputStream inputStream = ExcelReaderSpec.class.getClassLoader().getResourceAsStream(fileName) ) {
			reader.read(0, inputStream, (type, row, col, value) -> {
				loadingAmount[0]++
				if (row == 0 && col == 0) {
					assert type == TableResult.STRING
					assert TableResult.STRING.as(value) == SASHLIK
					assert row == loadingAmount[1]
				} else if (row == 0 && col == 1) {
					assert type == TableResult.DOUBLE
					assert value == 1d
					assert row == loadingAmount[1]
				} else if (row == 0 && col == 2) {
					assert type == TableResult.DATE
					LocalDateTime dt = TableResult.DATE.as(value)
					assert dt.getDayOfMonth() == 21
					assert dt.getMonth() == Month.JUNE
					assert dt.getYear() == 2011
					assert row == loadingAmount[1]
				} else if (row == 0 && col == 3) {
					assert type == TableResult.DATE
					LocalDateTime dt = TableResult.DATE.as(value)
					assert dt.getDayOfMonth() == 21
					assert dt.getMonth() == Month.JUNE
					assert dt.getYear() == 2011
					assert dt.getHour() == 12
					assert dt.getMinute() == 31
					assert dt.getSecond() == 41
					assert row == loadingAmount[1]
				} else if (row == 0 && col == 4) {
					assert type == TableResult.NULL
					assert value == null
					assert row == loadingAmount[1]
				} else if (row == 1 && col == 0) {
					assert type == TableResult.STRING
					assert TableResult.STRING.as(value) == TYINA
					assert row == loadingAmount[1]
				} else if (row == 1 && col == 1) {
					assert type == TableResult.DOUBLE
					assert value == 2.1123d
					assert row == loadingAmount[1]
				} else if (row == 1 && col == 2) {
					assert type == TableResult.NULL
					assert value == null
					assert row == loadingAmount[1]
				} else if (row == 2 && col == 0) {
					assert type == TableResult.NULL
					assert value == null
					assert row == loadingAmount[1]
				} else if (row == 2 && col == 1) {
					assert type == TableResult.DOUBLE
					assert value == -1d
					assert row == loadingAmount[1]
					assert row == loadingAmount[1]
				} else if (row == 2 && col == 2) {
					assert type == TableResult.NULL
					assert value == null
					assert row == loadingAmount[1]
				} else if (row == 2 && col == 3) {
					assert type == TableResult.NULL
					assert value == null
					assert row == loadingAmount[1]
				} else if (row == 2 && col == 4) {
					assert type == TableResult.STRING
					assert TableResult.STRING.as(value) == "Kolmas"
					assert row == loadingAmount[1]
				} else if (type == TableResult.BEGIN) {
					assert col == -1
					assert row == -1
					assert value == null
				} else if (type == TableResult.END) {
					assert col == -1
					assert row == -1
					assert value == null
				} else if (type == TableResult.ROW_START) {
					loadingAmount[1]++
					assert col == -1
					assert row > -1
					assert TableResult.ROW_START.as(value) > -1
				} else if (type == TableResult.ROW_END) {
					loadingAmount[2]++
					assert loadingAmount[2] == loadingAmount[1]
					assert col == -1
					assert row > -1
					assert TableResult.ROW_START.as(value) > -1
				} else if (type == TableResult.NULL) {
					assert col > 0
					assert row > -1
					assert value == null
				} else {
					throw new IllegalArgumentException(row + "," + col + ": " + type + ": " + value)
				}
			} )
		}
		return loadingAmount
	}
}
