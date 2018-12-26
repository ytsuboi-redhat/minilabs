package com.redhat.labsjp.sample.todobackend.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

public class Todo extends GenericDomain {

    private static final long serialVersionUID = -4841313814182406622L;

    @Id
    private long todoId;

    @NotNull
    @Size(min = 1)
    private String title;

    @NotNull
    @Size(min = 1)
    private String status;

    private String description;

    public Todo() {
    }

    public Todo(String title, String status, String description) {
      this.title = title;
      this.status = status;
      this.description = description;
    }

    public Todo(Long todoId, String title, String status, String description) {
      this.todoId = todoId;
      this.title = title;
      this.status = status;
      this.description = description;
    }

    @Override
    protected Object keyObject() {
      return getTodoId();
    }

    /**
     * @return the todoId
     */
    public Long getTodoId() {
      return todoId;
    }

    /**
     * @param todoId the todoId to set
     */
    public void setTodoId(Long todoId) {
      this.todoId = todoId;
    }

    /**
     * @return the title
     */
    public String getTitle() {
      return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
      this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
      return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
      this.description = description;
    }

    /**
     * @return the status
     */
    public String getStatus() {
      return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
      this.status = status;
    }

}