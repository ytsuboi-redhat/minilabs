import Axios from 'axios'

export default class TodoService {
  constructor () {
    this.instance = Axios.create({
      baseURL: '/api/todos',
      timeout: 1000,
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'POST, GET, OPTIONS',
        'Access-Control-Allow-Headers': '*'
      }
    })
  }

  getAll () {
    return this.instance.get('/')
  }
}
