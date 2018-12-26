package com.redhat.labsjp.sample.todobackend.service;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.redhat.labsjp.sample.todobackend.domain.Todo;
import com.redhat.labsjp.sample.todobackend.repository.TodoRepository;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    public void getTodos() {
        Todo todo1 = new Todo(1L, "title1", "status1", "desc1");
        Todo todo2 = new Todo(2L, "title2", "status2", "desc2");
        List<Todo> list = Lists.newArrayList(todo1, todo2);

        when(todoRepository.findAll()).thenReturn(list);

        assertThat(todoService.getTodos(), Matchers.contains(todo1, todo2));
    }

    @Test
    public void getTodo() {
        long id = 1L;
        Todo todo = new Todo(id, "title1", "status1", "desc1");

        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        assertThat(todoService.getTodo(id), Matchers.is(todo));
    }

    @Test
    public void getTodoIfNotPresent() {
        long id = 1L;
        Optional<Todo> todo = Optional.empty();

        when(todoRepository.findById(id)).thenReturn(todo);

        assertThat(todoService.getTodo(id), Matchers.nullValue());
    }

    @Test
    public void createTodo() {
        Todo todo = new Todo(1L, null, null, null);

        ArgumentCaptor<Todo> captor = ArgumentCaptor.forClass(Todo.class);
        when(todoRepository.save(captor.capture())).thenReturn(todo);

        todoService.createTodo(todo);

        assertThat(todo, Matchers.is(captor.getValue()));
    }

}