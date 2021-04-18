import Vue from 'vue'
import Router from 'vue-router'

import Home from '@/components/Home'
import Technology from '@/components/Technology'
import Reading from '@/components/Reading'
import MusicList from '@/components/MusicList'
import About from '@/components/About'
import TechDetail from "@/components/TechDetail";

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            component: Home,
            name: '',
            iconCls: 'fa fa-file-text-o',
            menu_item: false,
            redirect: '/tech',
        }, {
            path: '/',
            component: Home,
            name: '主页',
            menu_item: false,
            children: [
                {
                    path: '/tech',
                    component: Technology,
                    name: '主页',
                    menu_item: true,
                }
            ]
        }, {
            path: '/',
            component: Home,
            name: '生活',
            menu_item: false,
            children: [
                {
                    path: '/reading',
                    name: '读书',
                    component: Reading,
                    menu_item: true,
                },
                {
                    path: '/music_list',
                    name: '歌单',
                    component: MusicList,
                    menu_item: true,
                }
            ]
        }, {
            path: '/',
            component: Home,
            name: '关于',
            menu_item: false,
            children: [
                {
                    path: '/about',
                    component: About,
                    name: '关于',
                    menu_item: true,
                },
            ]
        },
        {
            path: '/tech_detail/:item_name',
            component: TechDetail,
            name: 'tech_detail',
            iconCls: 'fa fa-file-text-o',
            menu_item: false,
        },
    ]
})
