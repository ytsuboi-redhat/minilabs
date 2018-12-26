<template>
  <div class="todo-container">
    <h1>todo-app</h1>
    <div id="result">{{ message }}</div>
    <form v-on:submit.prevent="getAll">
      <button id="submitButton" type="submit" name="button" class="btn btn-primary col-sm-3">click me!!</button>
    </form>
    <table class="table">
      <thead>
        <tr>
          <th scope="col"></th>
          <th scope="col">title</th>
          <th scope="col">status</th>
          <th scope="col">description</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(todo, index) in todos" :key="todo.todoId">
          <td class="index">{{ index }}</td>
          <td class="title">
            <input v-model="todo.title" placeholder="title">
          </td>
          <td class="status">
            <input v-model="todo.status" placeholder="status">
          </td>
          <td class="desc">
            <input v-model="todo.description" placeholder="description">
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import TodoService from '@/services/TodoService'

export default {
  name: 'Todo',
  props: {
    msg: String
  },
  data () {
    return {
      message: '',
      todos: []
    }
  },
  methods: {
    getAll () {
      const service = new TodoService()
      service.getAll()
        .then(response => {
          this.todos = response.data
        })
        .catch(() => {
          this.message = 'error'
        })
    }
  }
}
</script>

<style>
  .todo-container {
    padding: 3rem 1.5rem;
    text-align: center;
  }
</style>
