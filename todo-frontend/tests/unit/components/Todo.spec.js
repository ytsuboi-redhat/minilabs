import { mount } from '@vue/test-utils'
import { expect } from 'chai'
import sinon from 'sinon'

import Todo from '@/components/Todo'
import TodoService from '@/services/TodoService'

describe('Todo.vue', () => {
  let sandbox

  beforeEach(() => {
    sandbox = sinon.createSandbox()
  })

  afterEach(() => {
    sandbox.restore()
  })

  context('メソッドの検証', () => {
    context('getAll() service 処理が成功する場合', () => {
      it('返却値がセットされる', done => {
        const wrapper = mount(Todo)

        sandbox.stub(TodoService.prototype, 'getAll').resolves({ data: 1 })

        wrapper.vm.getAll()

        wrapper.vm.$nextTick(() => {
          expect(wrapper.vm.todos).to.equal(1)
          expect(wrapper.vm.message).to.equal('')
          done()
        })
      })
    })

    context('getAll() service 処理が失敗する場合', () => {
      it('メッセージがセットされる', done => {
        const wrapper = mount(Todo)

        sandbox.stub(TodoService.prototype, 'getAll').rejects({ data: 1 })

        wrapper.vm.getAll()

        wrapper.vm.$nextTick(() => {
          wrapper.vm.$nextTick(() => {
            expect(wrapper.vm.todos).to.have.lengthOf(0)
            expect(wrapper.vm.message).to.equal('error')
            done()
          })
        })
      })
    })
  })

  context('コンポーネントの振る舞いを検証', () => {
    context('初期表示の場合', () => {
      it('TODOリストは表示されない', () => {
        const wrapper = mount(Todo)

        expect(wrapper.find('.table tbody').element.hasChildNodes()).to.equal(false)
      })
    })

    context('ボタンをクリックした場合', () => {
      it('TODOリストが表示される', done => {
        const todo1 = {
          todoId: 1,
          title: 'title1',
          status: 'status1',
          description: 'desc1'
        }
        const todo2 = {
          todoId: 2,
          title: 'title2',
          status: 'status2',
          description: 'desc2'
        }

        const wrapper = mount(Todo)
        sandbox.stub(TodoService.prototype, 'getAll').resolves({ data: [todo1, todo2] })

        wrapper.find('form').trigger('submit.prevent')

        wrapper.vm.$nextTick(() => {
          expect(wrapper.findAll('.table tbody tr')).to.have.lengthOf(2)
          expect(wrapper.findAll('.table tbody .index').at(0).element.textContent).to.equal(String(todo1.todoId))
          expect(wrapper.findAll('.table tbody .title input').at(0).element.value).to.equal(todo1.title)
          expect(wrapper.findAll('.table tbody .status input').at(0).element.value).to.equal(todo1.status)
          expect(wrapper.findAll('.table tbody .desc textarea').at(0).element.value).to.equal(todo1.description)
          expect(wrapper.findAll('.table tbody .index').at(1).element.textContent).to.equal(String(todo2.todoId))
          expect(wrapper.findAll('.table tbody .title input').at(1).element.value).to.equal(todo2.title)
          expect(wrapper.findAll('.table tbody .status input').at(1).element.value).to.equal(todo2.status)
          expect(wrapper.findAll('.table tbody .desc textarea').at(1).element.value).to.equal(todo2.description)
          done()
        })
      })
    })
  })
})
