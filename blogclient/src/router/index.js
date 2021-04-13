import Vue from 'vue'
import Router from 'vue-router'

import Home from '@/components/Home'
import Technology from '@/components/Technology'
import Life from '@/components/Life'
import Reading from '@/components/Reading'
import MusicList from '@/components/MusicList'
import About from '@/components/About'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: Home,
      name: '主页',
      iconCls: 'fa fa-file-text-o',
      menu_item: false,
    }, {
      path: '/',
      component: Technology,
      name: '主页',
      menu_item: true,
    }, {
      path: '/',
      component: Life,
      name: '生活',
      menu_item: true,
      children: [
        {
          path: '/reading',
          iconCls: 'fa fa-reorder',
          name: '读书',
          component: Reading,
          menu_item: true,
        },
        {
          path: '/music_list',
          iconCls: 'fa fa-reorder',
          name: '歌单',
          component: MusicList,
          menu_item: true,
        }
      ]
    }, {
      path: '/',
      component: About,
      name: '关于',
      menu_item: true,
      iconCls: 'fa fa-bar-chart',
    }
  ]
})
