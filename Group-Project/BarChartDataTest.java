package org.jfree.chart.demo2.DBChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.part_2.createBarChartDataset;
import org.jfree.DBChartSENG275.PieChartData;
import java.sql.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import net.jqwik.api.*;

class BarChartDataTest {
    ResultSet resultSetMock;
    PieChartData BarChartData = null;
    DefaultCategoryDataset dataSetMock;

    @BeforeEach
    public void init(){
        resultSetMock = mock(ResultSet.class);
    }

    //Created by Connor
    @Test
    public void emptyResultSet(){
        try {
            when(resultSetMock.next()).thenReturn(false);
            assertThat(createBarChartDataset.createDataset(resultSetMock).getRowCount()).isEqualTo(0);

            verify(resultSetMock, times(0)).getString("Name");          //basically verify that we didn't actually make it into
            verify(resultSetMock, times(0)).getInt("Milliseconds");     //the loop

        }catch(SQLException e){
            System.out.println(e.getMessage());

        }
    }
    //Created by Connor
    @Test
    public void OneSong(){
        try {
            when(resultSetMock.next()).thenReturn(true, false);
            when(resultSetMock.getString("Name")).thenReturn("RandomName");
            when(resultSetMock.getInt("Milliseconds")).thenReturn(40000);


            assertThat(createBarChartDataset.createDataset(resultSetMock).getRowCount()).isEqualTo(1);
            verify(resultSetMock, times(1)).getString("Name");
            verify(resultSetMock, times(1)).getInt("Milliseconds");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    //Created by Connor
    @Test
    public void checkCorrectNameInputIntoDataset(){
        try {
            when(resultSetMock.next()).thenReturn(true, false);
            when(resultSetMock.getString("Name")).thenReturn("Group10");
            when(resultSetMock.getInt("Milliseconds")).thenReturn(40000);

            assertThat(createBarChartDataset.createDataset(resultSetMock).getRowKeys()).containsExactly("Group10");
            verify(resultSetMock, times(1)).getString("Name");
            verify(resultSetMock, times(1)).getInt("Milliseconds");

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    //Created by Connor
    @Test
    public void checkMillisecondsInputCorrectly(){
        try {
            when(resultSetMock.next()).thenReturn(true, false);
            when(resultSetMock.getString("Name")).thenReturn("Group10");
            when(resultSetMock.getInt("Milliseconds")).thenReturn(40000);

            assertThat(createBarChartDataset.createDataset(resultSetMock).getValue("Group10", "Songs")).isEqualTo(40000.0);
            verify(resultSetMock, times(1)).getString("Name");
            verify(resultSetMock, times(1)).getInt("Milliseconds");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkMultipleSongs(){
        try {
            when(resultSetMock.next()).thenReturn(true, true,false);
            when(resultSetMock.getString("Name")).thenReturn("Group10", "Group11");
            when(resultSetMock.getInt("Milliseconds")).thenReturn(40000, 5000);
            assertThat(createBarChartDataset.createDataset(resultSetMock).getRowCount()).isEqualTo(2);

            verify(resultSetMock, times(2)).getString("Name");

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void negativeMilliseconds(){
        try{
            when(resultSetMock.next()).thenReturn(true, false);
            when(resultSetMock.getString("Name")).thenReturn("Group10");
            when(resultSetMock.getInt("Milliseconds")).thenReturn(-1);

            assertThrows(AssertionError.class,
                    () -> createBarChartDataset.createDataset(resultSetMock).getRowCount());

            verify(resultSetMock, times(1)).getString("Name");
            verify(resultSetMock, times(1)).getInt("Milliseconds");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    //created by Rena
    @Test
    public void aboveThresholdMilliseconds(){
        try{
            when(resultSetMock.next()).thenReturn(true, false);
            when(resultSetMock.getString("Name")).thenReturn("Group10");
            when(resultSetMock.getInt("Milliseconds")).thenReturn(40001);

            assertThat(createBarChartDataset.createDataset(resultSetMock).getRowCount()).isEqualTo(0);
            verify(resultSetMock, times(1)).getString("Name");
            verify(resultSetMock, times(1)).getInt("Milliseconds");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    //created by Rena
    @Property
    void passMultipleValid(@ForAll("validRange") int milliseconds){
        try{
            resultSetMock = mock(ResultSet.class);
            when(resultSetMock.next()).thenReturn(true,false);
            when(resultSetMock.getString("Name")).thenReturn("Group10");
            when(resultSetMock.getInt("Milliseconds")).thenReturn(milliseconds);
            double millisecondsDouble = milliseconds;
            assertThat(createBarChartDataset.createDataset(resultSetMock).getValue("Group10", "Songs")).isEqualTo(millisecondsDouble);
            verify(resultSetMock, times(1)).getString("Name");
            verify(resultSetMock, times(1)).getInt("Milliseconds");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //created by Rena
    @Property
    void passMultipleInvalidMilliseconds(@ForAll("invalidMillisecondsRange") int milliseconds){
        try{
            resultSetMock = mock(ResultSet.class);
            when(resultSetMock.next()).thenReturn(true,false);
            when(resultSetMock.getString("Name")).thenReturn("Group10");
            when(resultSetMock.getInt("Milliseconds")).thenReturn(milliseconds);
            assertThat(createBarChartDataset.createDataset(resultSetMock).getRowCount()).isEqualTo(0);
            verify(resultSetMock, times(1)).getString("Name");
            verify(resultSetMock, times(1)).getInt("Milliseconds");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Test created by Charlie
    @Property
    void testMultipleInvalidNegativeSongLengths(@ForAll("invalidRangeNegative")int mseconds) {
        try{

            resultSetMock = mock(ResultSet.class);
            when(resultSetMock.next()).thenReturn(true, false);
            when(resultSetMock.getString("Name")).thenReturn("Group10");
            when(resultSetMock.getInt("Milliseconds")).thenReturn(mseconds);
            assertThrows(AssertionError.class,
                    () -> createBarChartDataset.createDataset(resultSetMock).getRowCount());
            verify(resultSetMock, times(1)).getString("Name");
            verify(resultSetMock, times(1)).getInt("Milliseconds");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Provide
    private Arbitrary<Integer> validRange() {
        return Arbitraries.integers().filter(n -> n <=40000).filter(n -> n > 0);
    }

    @Provide
    private Arbitrary<Integer> invalidMillisecondsRange() {
        return Arbitraries.oneOf(Arbitraries.integers().filter(n -> n >40000),Arbitraries.integers().filter(n -> n < 0) ;
    }

    @Provide
    private Arbitrary<Integer> invalidRangeNegative() {
        return Arbitraries.integers().filter(n -> n <= 0);
    }
}