package com.foxjunior.io.tablereader


import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class CsvReaderSpec extends Specification {
	def "Simple IO test"() {
		given:
			//Šašlikul on kole nälg õäöüÕÄÖÜ
			String SASHLIK
			String TYINA
			try (BufferedReader testDataReader =  new BufferedReader(new InputStreamReader(CsvReaderSpec.class.getClassLoader().getResourceAsStream("test.txt"), StandardCharsets.UTF_8))) {
				SASHLIK = testDataReader.readLine()
				TYINA = testDataReader.readLine()
			}

			int[] loadingAmount = [-1, -1, -1]
			final InputStreamReader reader =  new InputStreamReader(CsvReaderSpec.class.getClassLoader().getResourceAsStream("simple.csv"), Charset.forName("Windows-1257"))
			final CsvReader csvReader = TableReaderFactory.getReader(SupportedFiles.CSV).as(CsvReader.class)
		expect:
			csvReader.read(reader, ',' as char, '"' as char, {type, row, col, value ->
				loadingAmount[0]++
				if (row == 0 && col == 0) {
					// This is the problem with š in excel to csv.
					assert TableResult.STRING.as(value) != SASHLIK
					assert TableResult.STRING.as(value).substring(3) == SASHLIK.substring(3)
					assert type == TableResult.STRING
					assert row == loadingAmount[1]
				} else if (row == 0 && col == 1) {
					assert value == "1"
					assert type == TableResult.STRING
					assert row == loadingAmount[1]
				} else if (row == 0 && col == 2) {
					assert value == "21/06/11"
					assert type == TableResult.STRING
					assert row == loadingAmount[1]
				} else if (row == 0 && col == 3) {
					assert value == "21/06/2011 12:31:41"
					assert type == TableResult.STRING
					assert row == loadingAmount[1]
				} else if (row == 0 && col == 4) {
					assert value == null
					assert type == TableResult.NULL
					assert row == loadingAmount[1]
				} else if (row == 1 && col == 0) {
					assert value == TYINA
					assert type == TableResult.STRING
					assert row == loadingAmount[1]
				} else if (row == 1 && col == 1) {
					assert value == "2.1123"
					assert type == TableResult.STRING
					assert row == loadingAmount[1]
				} else if (row == 1 && col == 2) {
					assert value == null
					assert type == TableResult.NULL
					assert row == loadingAmount[1]
				} else if (row == 2 && col == 0) {
					assert value == null
					assert type == TableResult.NULL
					assert row == loadingAmount[1]
				} else if (row == 2 && col == 1) {
					assert value == "-1"
					assert type == TableResult.STRING
					assert row == loadingAmount[1]
				} else if (row == 2 && col == 2) {
					assert value == null
					assert type == TableResult.NULL
					assert row == loadingAmount[1]
				} else if (row == 2 && col == 3) {
					assert value == null
					assert type == TableResult.NULL
					assert row == loadingAmount[1]
				} else if (row == 2 && col == 4) {
					assert value == "Kolmas"
					assert type == TableResult.STRING
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
					//assert value == 1
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
			})
		and:
			loadingAmount == [22, 2, 2] as int[]
		cleanup:
			reader.close()
	}

	@Unroll
	def "Type #type work"() {
		expect:
			TableReaderFactory.getReader(type).as(CsvReader.class) != null
		where:
			type << SupportedFiles.values().findAll {![SupportedFiles.EXCEL_97, SupportedFiles.EXCEL_2007].contains(it)}
	}

	@Unroll
	def "extends #extension #name works"() {
		expect:
			TableReaderFactory.getReaderByExtendsion(extension).as(CsvReader.class) != null
			TableReaderFactory.getReader(name + "." + extension).as(CsvReader.class) != null
		where:
			[extension, name] << [["csv", "txt", ".csv", ".txt"], ["me", "my.file"]].combinations()
	}
}
