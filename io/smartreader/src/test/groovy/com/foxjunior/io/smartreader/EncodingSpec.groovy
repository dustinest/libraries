package com.foxjunior.io.smartreader


import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class EncodingSpec extends Specification {
    private static Path getFile(String fileName) throws URISyntaxException {
        return Paths.get(EncodingSpec.class.getResource( fileName ).toURI())
    }

    def "UTF8BE  or UTF_16"() {
        when:
        CharsetAwareInputStream inStream = Encoding.predict( Files.newInputStream(getFile("UTF_16BE.txt")))
        then:
        inStream.getCharset() == StandardCharsets.UTF_16BE || inStream.getCharset() == StandardCharsets.UTF_16
        cleanup:
        inStream.close()
    }

    @Unroll
    def "predict #fileName charset is #charset"() {
        when:
            CharsetAwareInputStream inStream = Encoding.predict( Files.newInputStream(getFile(fileName)))
        then:
            inStream.getCharset() == charset
        cleanup:
            inStream.close()
        where:
            fileName        | charset
            "utf8.txt"      | StandardCharsets.UTF_8
            "UTF_16LE.txt"  | StandardCharsets.UTF_16LE
    }

    def "Windows-1257 can be detected with hint"() {
        given:
            def windowsCharset = Charset.forName("Windows-1257")
        when:
            CharsetAwareInputStream inStream = Encoding.predict( getFile("Windows1257.txt"), windowsCharset)
        then: // it might be system default if it is too close to it
            inStream.getCharset() == windowsCharset || inStream.getCharset() == Charset.defaultCharset()
        cleanup:
            inStream.close()
    }

    def "Windows-1257 with hint of UTF16LE finds closest charset possible"() {
        when:
            CharsetAwareInputStream inStream = Encoding.predict( getFile("Windows1257.txt"), StandardCharsets.UTF_16LE)
        then: // it might be system default if it is too close to it
            inStream.getCharset() == Charset.defaultCharset() || inStream.getCharset() == StandardCharsets.UTF_8
        cleanup:
            inStream.close()
    }

    def "Windows-1257 without hint finds closest charset possible"() {
        given:
            def windowsCharset = Charset.forName("Windows-1257")
        when:
            CharsetAwareInputStream inStream = Encoding.predict( getFile("Windows1257.txt"))
        then: // it might be system default if it is too close to it
            inStream.getCharset() == Charset.defaultCharset() || inStream.getCharset() == windowsCharset
        cleanup:
            inStream.close()
    }
}
