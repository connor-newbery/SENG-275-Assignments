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

class ColoursTest {

    @Property
    void pass(@ForAll @IntRange(min = 1, max = 255) int red, @ForAll @IntRange(min = 1, max = 255) int blue, @ForAll @IntRange(min = 1, max = 255) int green ) {
        assertThat(Colours.rgbBytesToInt(red,blue,green)).isEqualTo(red * 256 + blue * 16 + green);
    }

    @Property
    void invalidRed(@ForAll("invalidRange") int red, @ForAll @IntRange(min = 1, max = 255) int blue, @ForAll @IntRange(min = 1, max = 255) int green ) {
        assertThrows(IllegalArgumentException.class, () -> Colours.rgbBytesToInt(red,blue,green));
    }

    @Property
    void invalidBlue(@ForAll @IntRange(min = 1, max = 255) int red, @ForAll("invalidRange") int blue, @ForAll @IntRange(min = 1, max = 255) int green ) {
        assertThrows(IllegalArgumentException.class, () -> Colours.rgbBytesToInt(red,blue,green));
    }

    @Property
    void invalidGreen(@ForAll @IntRange(min = 1, max = 255) int red, @ForAll @IntRange(min = 1, max = 255) int blue, @ForAll("invalidRange") int green ) {
        assertThrows(IllegalArgumentException.class, () -> Colours.rgbBytesToInt(red,blue,green));
    }

    @Provide
    private Arbitrary<Integer> invalidRange(){
        return Arbitraries.oneOf(Arbitraries.integers().lessOrEqual(-1), Arbitraries.integers().greaterOrEqual(256));
    }
}