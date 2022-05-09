package lab03;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class SpecificationTest {

    @ParameterizedTest(name = "x{0}, y{1}")
    @CsvSource({"-1,0", "0,-1", "1280,720","1281, 720", "1280,721", "0,720", "-1, 719","1281,0", "1279,-1"})
    void FalseHD(int x, int y){
        assertFalse(Specification.insideDisplayArea(x,y));
    }
    @ParameterizedTest(name = "x{0}, y{1}")
    @CsvSource({"0,0", "1279, 719", "0, 719", "1279,0"})
    void TrueHD(int x, int y){
        assertTrue(Specification.insideDisplayArea(x,y));
    }

    @ParameterizedTest(name = "x{0}, y{1}")
    @CsvSource({"-1,0", "0,-1", "1920,1080","1921, 1079", "1919,1081", "0, 1080", "-1, 1079","1920,0", "1919,-1"})
    void FalseFHD(int x, int y){
        Specification.setDefinition(1);
        assertFalse(Specification.insideDisplayArea(x,y));
    }
    @ParameterizedTest(name = "x{0}, y{1}")
    @CsvSource({"0,0", "1919, 1079", "0, 1079", "1919,0"})
    void TrueFHD(int x, int y){
        Specification.setDefinition(1);
        assertTrue(Specification.insideDisplayArea(x,y));
    }


    @ParameterizedTest(name = "input{0}, motorcycle{1}")
    @MethodSource("AllFalse")
    void AllFalse(String input, boolean motorcycle){
        assertFalse(Specification.messageIsValid(input, motorcycle));
    }

    private static Stream<Arguments>AllFalse(){
        return Stream.of(
        Arguments.of("",false),  //empty string
        Arguments.of("111", false), //string length>2 but only numbers
        Arguments.of("34ABA5B", false), //string length = 7 but no spaces
        Arguments.of("-AG3", false),  //dash not surrounded by letters or numbers
        Arguments.of("ABAABAA", true), //string length = 7 but all letters & no spaces
        Arguments.of("ABA-ABBA", false)); //string length = 8 with a hyphen

    }

    @ParameterizedTest(name = "input{0}, motorcycle{1}")
    @MethodSource("AllTrue")
    void AllTrue(String input, boolean motorcycle){
        assertTrue(Specification.messageIsValid(input, motorcycle));
    }

    private static Stream<Arguments>AllTrue(){
        return Stream.of(
                Arguments.of("AA",false),
                Arguments.of("111AAA", false),
                Arguments.of("ABA-45B", false),
                Arguments.of("A-1 G-3", false),
                Arguments.of("AB-ABA", true));
    }
}