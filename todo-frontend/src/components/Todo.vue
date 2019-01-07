<template>
  <div class="container todo-container">
    <h1>todo-app</h1>
    <div class="row p-3">
      <div class="col-sm">
        <form v-on:submit.prevent="getAll">
          <button id="submitButton" type="submit" name="button" class="btn btn-primary col-sm-3">show todos</button>
        </form>
      </div>
    </div>
    <div class="row p-3">
      <div class="col-sm">
        <div id="result">{{ message }}</div>
        <div v-bind:class="tabVisible">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">title</th>
                <th scope="col">status</th>
                <th scope="col">description</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="todo in todos" :key="todo.todoId">
                <td class="index">{{ todo.todoId }}</td>
                <td class="title">
                  <input class="form-control" v-model="todo.title" placeholder="title">
                </td>
                <td class="status">
                  <input class="form-control" v-model="todo.status" placeholder="status">
                </td>
                <td class="desc">
                  <textarea class="form-control" rows="5" v-model="todo.description" placeholder="description"></textarea>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
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
      tabVisible: 'd-none',
      message: '',
      todos: []
    }
  },
  methods: {
    getAll () {
      const service = new TodoService()
      service.getAll()
        .then(response => {
          this.tabVisible = 'd-block'
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
