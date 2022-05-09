package lab04;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class WordUtilitiesTest {
    @ParameterizedTest
    @MethodSource("generator")
    void swapCase(String description, String str, String expected){
        String result = WordUtilities.swapCase(str);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> generator(){
        return Stream.of(
                Arguments.of("empty str", "", ""),
                Arguments.of("null string", null, null),
                Arguments.of("only Upper", "CC", "cc"),
                Arguments.of("only lower", "cc", "CC"),
                Arguments.of("lower case with whitespace", "eels and goats", "EELS AND GOATS"),
                Arguments.of("no whitespaces, upper and lower", "CaBgtHH", "cAbGThh"),
                Arguments.of("spaces and upper case", "G D HHG", "g d hhg"),
                Arguments.of("only spaces", "  ", "  "),
                Arguments.of("everything", "hello Squid", "HELLO sQUID"),
                Arguments.of("Upper, space, lower", "C c", "c C")

        );
    }
}