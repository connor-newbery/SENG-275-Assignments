package lab07;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.UpperCase;

import javax.xml.stream.events.Characters;

class PalindromeTest {

    /*
     * Try testing the following cases:
     *  - any string, followed by the reverse of that string is a palindrome.
     *  - any string, followed by a single character, then the reverse of the string is a palindrome.
     *  - Any string made up of unique characters of length 2 or greater is not a palindrome.
     *  - Any palindrome set to uppercase is still a palindrome.
     */

    @Test
    void nullString(){
        assertThrows(IllegalArgumentException.class, () -> Palindrome.isPalindrome(null));
    }

    @Property
    void stringPlusReverse(@ForAll @AlphaChars String string){
        assertTrue(Palindrome.isPalindrome(string + reverse(string)));
    }

    @Property
    void stringPlusLetterPlusReverse(@ForAll @AlphaChars String string, @ForAll @AlphaChars char letter){
        assertTrue(Palindrome.isPalindrome(string+letter+reverse(string)));
    }

    @Property
    void upperPalindrome(@ForAll @UpperChars String string, @ForAll @UpperChars char letter){
        assertTrue(Palindrome.isPalindrome(string+letter+reverse(string)));
    }

    @Property
    void uniqueSequence(@ForAll @AlphaChars @UniqueElements @Size(min=2) char[] c){
        String string = String.valueOf(c);
        assertFalse(Palindrome.isPalindrome(string));
    }


    static String reverse(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        return sb.toString();
    }
}