package com.redhat.labsjp.sample.todobackend.controller;

import javax.validation.Valid;

import com.redhat.labsjp.sample.todobackend.domain.Todo;
import com.redhat.labsjp.sample.todobackend.service.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(path = "todos")
public class TodoController {

    @Autowired
    TodoService todoService;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Todo> getTodos() {
        return todoService.getTodos();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{todoId}")
    public Todo getTodo(@PathVariable Long todoId) {
        return todoService.getTodo(todoId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Todo postTodo(@RequestBody @Valid Todo todo) {
        todoService.createTodo(todo);
        return todo;
    }
}