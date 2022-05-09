
package lab06;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.List;

class TodoApplicationTest {

    private TodoApplication todoApp;
    private PersonService personServiceMock;
    private TodoService todoServiceMock;

    private final String userName = "SomeUser";
    private final Long userID = 1L;
    private final List<String> todos = List.of("Wake up", "Test the code", "Celebrate the victory!");

    @Test
    void addTodo() {
        // Ensure that it's possible to add a todo to the app, and that the correct methods are called

        TodoService todoServiceMock = mock(TodoService.class);
        PersonService personServiceMock = mock(PersonService.class);

        todoApp = new TodoApplication(todoServiceMock, personServiceMock);

        when(personServiceMock.findUsernameById(userID)).thenReturn("SomeUser");
        when(todoServiceMock.addTodo(userName, "Wake up")).thenReturn(true);

        assertThat(todoApp.addTodo(1L, "Wake up")).isEqualTo(true);

        verify(personServiceMock, times(1)).findUsernameById(userID);
        verifyNoMoreInteractions(personServiceMock);

        verify(todoServiceMock, times(1)).addTodo(userName, "Wake up");
        verifyNoMoreInteractions(todoServiceMock);



    }

    @Test
    void retrieveTodos() {
        // add multiple todos to the app, and retrieve a strict subset of them using a substring.
        TodoService todoServiceMock = mock(TodoService.class);
        PersonService personServiceMock = mock(PersonService.class);
        todoApp = new TodoApplication(todoServiceMock, personServiceMock);

        when(personServiceMock.findUsernameById(userID)).thenReturn(userName);
        when(todoServiceMock.retrieveTodos(userName)).thenReturn(List.of("Test the code", "Celebrate the victory!"));

        assertThat(todoApp.retrieveTodos(userID, "the")).containsExactly("Test the code", "Celebrate the victory!");

        verify(personServiceMock, times(1)).findUsernameById(userID);
        verifyNoMoreInteractions(personServiceMock);

        verify(todoServiceMock, times(1)).retrieveTodos(userName);

        verifyNoMoreInteractions(personServiceMock);
        verifyNoMoreInteractions(todoServiceMock);


    }

    @Test
    void completeAllWithNoTodos() {
       // confirm that the appropriate behaviour occurs when there are no todos being tracked by the app
        TodoService todoServiceMock = mock(TodoService.class);
        PersonService personServiceMock = mock(PersonService.class);
        todoApp = new TodoApplication(todoServiceMock, personServiceMock);

        when(personServiceMock.findUsernameById(userID)).thenReturn(userName);
        when(todoServiceMock.retrieveTodos(userName)).thenReturn(List.of());

        todoApp.completeAllTodos(userID);

        verify(personServiceMock, times(1)).findUsernameById(userID);
        verify(todoServiceMock, times(1)).retrieveTodos(userName);
        verify(todoServiceMock, times(0)).completeTodo("");

        verifyNoMoreInteractions(personServiceMock);
        verifyNoMoreInteractions(todoServiceMock);

    }

    @Test
    void completeAllWithThreeTodos() {
        // confirm that the appropriate behaviour occurs when there are three todos being tracked by the app
        TodoService todoServiceMock = mock(TodoService.class);
        PersonService personServiceMock = mock(PersonService.class);
        todoApp = new TodoApplication(todoServiceMock, personServiceMock);

        when(personServiceMock.findUsernameById(userID)).thenReturn(userName);
        when(todoServiceMock.retrieveTodos(userName)).thenReturn(todos);

        todoApp.completeAllTodos(userID);

        verify(personServiceMock, times(1)).findUsernameById(userID);
        verify(todoServiceMock, times(1)).retrieveTodos(userName);


        verify(todoServiceMock, times(1)).completeTodo("Wake up");
        verify(todoServiceMock, times(1)).completeTodo("Test the code");
        verify(todoServiceMock, times(1)).completeTodo("Celebrate the victory!");

        verifyNoMoreInteractions(personServiceMock);
        verifyNoMoreInteractions(todoServiceMock);

    }
}