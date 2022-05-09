package lab03;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoundaryTest {

    @Test
    void isUnsafe() {
        assertTrue(Boundary.isUnsafe(86)); //off point
    }

    @Test
    void isNotUnsafe() {
        assertFalse(Boundary.isUnsafe(85)); //On point
    }

    @ParameterizedTest
    @ValueSource(ints = {5,15,20}) //on point, in point, second on point
    void isComfortable(int temperature){
        assertTrue(Boundary.isComfortable(temperature));
    }

    @ParameterizedTest
    @ValueSource(ints = {4,21}) //off points on either end
    void isUnComfortable(int temperature){
        assertFalse(Boundary.isComfortable(temperature));
    }

    @Test
    void zeroElevators(){
        assertEquals(0, Boundary.elevatorsRequired(1)); //off point
    }

    @ParameterizedTest
    @ValueSource(ints = {2,5})  //on point for 1 elevator, off point for 1 elevator
    void oneElevator(int storeys ){
        assertEquals(1, storeys);
    }

    @Test
    void twoElevators(){
        assertEquals(2, Boundary.elevatorsRequired(6));
    } //on point for 2 elevators

    @ParameterizedTest
    @ValueSource(doubles = {0,49})  //on point, off point
    void FGrade(double grades){
        assertEquals("F", Boundary.percentageToLetterGrade(grades));
    }
    @ParameterizedTest
    @ValueSource(doubles = {50,59})
    void DGrade(double grades){
        assertEquals("D", Boundary.percentageToLetterGrade(grades));
    }
    @ParameterizedTest
    @ValueSource(doubles = {60,64})
    void CGrade(double grades){
        assertEquals("C", Boundary.percentageToLetterGrade(grades));
    }
    @ParameterizedTest
    @ValueSource(doubles = {65,69})
    void CPlusGrade(double grades){
        assertEquals("C+", Boundary.percentageToLetterGrade(grades));
    }
    @ParameterizedTest
    @ValueSource(doubles = {70,72})
    void BMinusGrade(double grades){
        assertEquals("B-", Boundary.percentageToLetterGrade(grades));
    }
    @ParameterizedTest
    @ValueSource(doubles = {73,76})
    void BGrade(double grades){
        assertEquals("B", Boundary.percentageToLetterGrade(grades));
    }
    @ParameterizedTest
    @ValueSource(doubles = {77,79})
    void BPlusGrade(double grades){
        assertEquals("B+", Boundary.percentageToLetterGrade(grades));
    }
    @ParameterizedTest
    @ValueSource(doubles = {80,84})
    void AMinusGrade(double grades){
        assertEquals("A-", Boundary.percentageToLetterGrade(grades));
    }
    @ParameterizedTest
    @ValueSource(doubles = {85,89})
    void AGrade(double grades){
        assertEquals("A", Boundary.percentageToLetterGrade(grades));
    }
    @ParameterizedTest
    @ValueSource(doubles = {90,100})
    void APlusGrade(double grades){
        assertEquals("A+", Boundary.percentageToLetterGrade(grades));
    }
    @ParameterizedTest
    @ValueSource(doubles = {-1,101})
    void ExceptionGrade(double grades){

        assertThrows(IllegalArgumentException.class, ()->Boundary.percentageToLetterGrade(grades));
//        boolean thrown = false;
//        try{
//            Boundary.percentageToLetterGrade(grades);
//        }catch(IllegalArgumentException e){
//            thrown = true;
//        }
//        assertTrue(thrown);
    }
}