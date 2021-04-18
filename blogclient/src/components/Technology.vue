<template>
  <main>
    <article class="article_grid">
      <template v-for="(item,index) in articleGridItems">
        <div :style="item_style"
             :class="item.item_should_margin?'article_grid_item_margin':'article_grid_item'"
             v-on:submit.prevent="gridItemClicked(item)" :key="index">
          <img class="article_grid_item_img" :src="iconSrc[index]"/>
          <p class="article_grid_item_title">
            <router-link :to="{ name: 'tech_detail', params: { item_name: item.item_title }}">{{
                item.item_title
              }}
            </router-link>
          </p>
          <p class="article_grid_item_detail">{{ item.item_detail }}</p>
        </div>
      </template>
    </article>
    <section>
      <div :style="item_style" class="article_grid_item_margin"
           v-on:submit.prevent="gridItemClicked(lastGridItem)" :key="'last'">
        <img class="article_grid_item_img" :src="iconSrc[iconSrc.length - 1]"/>
        <p class="article_grid_item_title">{{ lastGridItem.item_title }}</p>
        <p class="article_grid_item_detail">{{ lastGridItem.item_detail }}</p>
      </div>
    </section>
  </main>
</template>

<script>
import {getRequest, postRequest} from '@/net'
import {SET_USER} from '@/store'

export default {
  name: 'Technology',
  data() {
    return {
      item_style: {
        width: '30em',
        height: '10em',
        border: '1px solid #ACACAC',
        borderRadius: '10px',
      },
      articleGridItem: {
        item_should_margin: false,
        item_title: '',
        item_detail: '',
      },
      lastGridItem: {},
      categories: [],
      articleGridItems: [],
      iconSrc: ['assets/android.svg', 'assets/html_css_js.svg', 'assets/spring_boot.svg',
        'assets/flutter.svg', 'assets/algorithm.svg'],
    }
  },
  methods: {
    getAllCategories() {
      let _this = this
      _this.loading = true
      getRequest('/category/all').then(resp => {
        let jsonData = resp.data
        if (jsonData && jsonData.code == 0) {
          _this.categories = jsonData.data
          let length = _this.categories.length
          let validIndex = 0
          for (var index = 0; index < length; index++) {
            let itemData = _this.categories[index]
            if (itemData && itemData.categoryDesc) {
              let shouldMargin = validIndex == 1 || validIndex == 3
              console.log('should margin:' + shouldMargin + ",validIndex:" + validIndex)
              let tempGridItem = {
                item_title: itemData.parentName,
                item_detail: itemData.categoryDesc,
                item_should_margin: shouldMargin,
              }
              if (index == length - 1) {
                _this.lastGridItem = tempGridItem
              } else {
                _this.articleGridItems.push(tempGridItem)
              }
              ++validIndex
            }
          }
        } else {
          _this.$message({
            type: 'error',
            message: jsonData.message
          })
        }
        _this.loading = false
      })
    },
    gridItemClicked(itemData) {
      console.log('itemData is ' + itemData.item_detail)
    },
    login() {
      let _this = this
      _this.loading = true
      postRequest('/login', {
        username: 'unknownUser',
        password: 'unknownUser',
      }).then(resp => {
        _this.loading = false
        if (resp.status === 200) {
          //成功
          var json = resp.data
          if (json.code == 0) {
            _this.$store.commit(SET_USER, json.data)
            _this.getAllCategories()
          } else {
            _this.$alert('网站初始化失败!', '失败!')
          }
        } else {
          //失败
          _this.$alert('网站初始化失败!', '失败!')
          _this.$store.commit(SET_USER, {})
        }
      }, error => {
        console.log(error)
        _this.$store.commit(SET_USER, {})
        _this.loading = false
        _this.$alert('网站初始化失败!', '失败!')
      })
    }
  },
  mounted() {
    this.login()
  },
}
</script>

<style scoped>
.article_grid {
  width: 100%;
  height: 100%;
  column-count: 2;
}

.article_grid_item_margin {
  margin-top: 2em;
}

.article_grid_item, .last_grid_item {
  width: 30em;
  height: 10em;
  border: 1px solid #ACACAC;
  border-radius: 10px;
}

.article_grid_item_img {
  width: 1.5em;
  height: 1.5em;
  margin-top: 1.2em;
  margin-left: 1em;
  margin-right: 0.1em;
  display: inline-block;
  vertical-align: middle;
}

.article_grid_item_title {
  color: dodgerblue;
  display: inline-block;
  vertical-align: bottom;
}

.article_grid_item_detail {
  margin: 1em 1.2em;
}
</style>
