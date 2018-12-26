package com.redhat.labsjp.sample.todobackend.repository;

import com.redhat.labsjp.sample.todobackend.domain.Todo;

import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long> {
    
}