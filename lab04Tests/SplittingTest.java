/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package lab04;

import org.apache.commons.math3.geometry.spherical.oned.ArcsSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class SplittingTest {
    @ParameterizedTest
    @MethodSource("generator")
    void canBalance(String description, int[] nums, boolean expected){
        boolean result = Splitting.canBalance(nums);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> generator(){
        return Stream.of(
                Arguments.of("array null", null, false),
                Arguments.of("array = 0", new int[]{0}, false),
                Arguments.of("sum%2 == 1", new int[]{2,3}, false),
                Arguments.of("sum%2 == 0", new int[]{2,2,2}, false),
                Arguments.of("half == 0", new int[]{2,2,2,2}, true)
        );
    }

}