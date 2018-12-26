import MockAdapter from 'axios-mock-adapter'
import { expect } from 'chai'
import TodoService from '@/services/TodoService'

describe('TodoService.js', () => {
  const uri = '/api/todos/'

  context('通信が成功する場合', () => {
    it('正常に処理が完了すること', done => {
      const service = new TodoService()
      const mockAxios = new MockAdapter(service.instance)
      mockAxios.onGet(uri).reply(200, { message: 'OK' })
      service.getAll()
        .then(res => {
          expect(res.data.message).to.equal('OK')
          done()
        })
        .catch(err => {
          done(err)
        })
    })
  })

  context('通信が失敗する場合', () => {
    it('エラーが返却されること', done => {
      const service = new TodoService()
      const mockAxios = new MockAdapter(service.instance)
      mockAxios.onGet(uri).reply(500, { message: 'NG' })
      service.getAll()
        .then(res => {
          expect.fail()
          done()
        })
        .catch(err => {
          expect(err.response.data.message).to.equal('NG')
          done()
        })
    })
  })
})
