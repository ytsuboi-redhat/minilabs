import { mount } from '@vue/test-utils'
import { expect } from 'chai'

import App from '@/App'
import Todo from '@/components/Todo'
import router from '@/router.js'

describe('router.js', () => {
  context('/todo にアクセスした場合', () => {
    it('Todoコンポーネントが表示される', () => {
      const wrapper = mount(App, { router, Todo })
      expect(wrapper.find(Todo).exists()).to.equal(false)

      router.push('/todo')

      expect(wrapper.find(Todo).exists()).to.equal(true)
    })
  })
})
