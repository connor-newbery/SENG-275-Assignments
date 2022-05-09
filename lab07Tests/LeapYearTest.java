
package lab07;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class LeapYearTest {

    @Property
    void isLeapYear(@ForAll("isLeapWith400") int year) {
        assertTrue(LeapYear.isLeapYear(year));
    }

    @Provide
    private Arbitrary<Integer> isLeapWith400() {
        return Arbitraries.integers().filter(n -> n % 400 == 0).filter(n -> n > 0);
    }

    @Property
    void isLeapWithJust4(@ForAll("isLeapWith4") int year) {
        assertTrue(LeapYear.isLeapYear(year));
    }

    @Provide
    private Arbitrary<Integer> isLeapWith4() {
        return Arbitraries.integers().filter(n -> n % 4 == 0).filter(n -> n % 400 != 0).filter(n -> n % 100 != 0).filter(n -> n > 0);
    }

    @Property
    void isNotLeapWith100(@ForAll("isntLeap") int year) {
        assertFalse(LeapYear.isLeapYear(year));
    }

    @Provide
    private Arbitrary<Integer> isntLeap() {
        return Arbitraries.integers().filter(n -> n % 100 == 0).filter(n -> n % 400 != 0).filter(n -> n > 0);
    }


    @Property
    void isNotLeap(@ForAll("notLeap") int year){
        assertFalse(LeapYear.isLeapYear(year));
    }

    @Provide
    private Arbitrary<Integer> notLeap() {
        return Arbitraries.integers().filter(n -> n % 100 != 0).filter(n -> n % 400 != 0).filter(n -> n % 4 != 0).filter(n -> n > 0);
    }

    @Property
    void invalidYear(@ForAll("invalidYear")int year){
        assertThrows(IllegalArgumentException.class, () -> LeapYear.isLeapYear(year));
    }

    @Provide
    private Arbitrary<Integer> invalidYear() {
        return Arbitraries.integers().filter(n -> n < 1);
    }

}