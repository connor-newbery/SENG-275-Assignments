package lab04;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class CollectionUtilsTest {

    @Test
    void containsAny_1_Less_2(){
        Collection coll1 = new ArrayList();
        coll1.add("a");
        coll1.add("b");

        Collection coll2 = new ArrayList();
        coll2.add("a");
        coll2.add("b");
        coll2.add("c");

        assertTrue(CollectionUtils.containsAny(coll1, coll2));
    }

    @Test
    void containsAny2_1_Equal_2(){
        Collection coll1 = new ArrayList();
        coll1.add("a");
        coll1.add("b");
        coll1.add("b");
        coll1.add("c");


        Collection coll2 = new ArrayList();
        coll2.add("a");
        coll2.add("b");
        coll2.add("c");

        assertTrue(CollectionUtils.containsAny(coll1, coll2));
    }

    @Test
    void containsNone_1_Less_2(){
        Collection coll1 = new ArrayList();
        coll1.add("d");
        coll1.add("e");


        Collection coll2 = new ArrayList();
        coll2.add("a");
        coll2.add("b");
        coll2.add("c");

        assertFalse(CollectionUtils.containsAny(coll1, coll2));
    }

    @Test
    void containsNone_1_Greater_2(){
        Collection coll1 = new ArrayList();
        coll1.add("d");
        coll1.add("e");
        coll1.add("e");
        coll1.add("e");

        Collection coll2 = new ArrayList();
        coll2.add("a");
        coll2.add("b");
        coll2.add("c");

        assertFalse(CollectionUtils.containsAny(coll1, coll2));
    }


}


