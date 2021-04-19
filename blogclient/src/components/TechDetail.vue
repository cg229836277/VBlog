<template>
  <el-container direction="vertical" style="height: 100%;">
    <div style="text-align: left;">
      <el-button type="text" icon="el-icon-back" @click="goBack" class="back_button">返回
      </el-button>
    </div>
    <el-container direction="horizontal">
      <keep-alive>
        <el-aside class="content_aside">
          <template v-for="(childItem, index) in categories" class="category_menu_parent">
            <el-button plain :key="index" @click="getArticleForCategory(childItem.id)">{{
                childItem.childName
              }}
            </el-button>
          </template>
          <el-menu default-active="0" background-color="transparent">
            <template v-for="(contentItem, index) in articles">
              <el-menu-item :key="index">
                <el-link type="primary" :key="index" @click="articleTitleClicked(index)">{{
                    contentItem.title
                  }}
                </el-link>
              </el-menu-item>
            </template>
          </el-menu>
        </el-aside>
      </keep-alive>
      <div class="divider_view"></div>
      <div class="html_content" v-html="articles[activeArticleIndex].content">
      </div>
    </el-container>
  </el-container>
</template>

<script>
import { getRequest } from '@/net'

export default {
  name: 'TechDetail',
  data () {
    return {
      itemName: '',
      itemId: '',
      activeArticleIndex: 0,
      categories: [],
      articles: [],
    }
  },
  methods: {
    goBack () {
      this.$router.go(-1)
    },
    initItemName () {
      let pageParameters = this.$route.params
      this.itemName = pageParameters.item_name
      this.itemId = pageParameters.item_id
      console.log('item_name=' + this.itemName)
      this.getCurrentChildCategory()
    },
    getCurrentChildCategory () {
      let _this = this
      _this.loading = true
      getRequest('/category/' + this.itemName).then(resp => {
        let jsonData = resp.data
        if (jsonData && jsonData.code == 0) {
          _this.categories = jsonData.data
          if (_this.categories && _this.categories.length > 0) {
            let categoryId = _this.categories[0].id
            console.log('category id:' + categoryId)
            _this.getArticleForCategory(categoryId)
          } else {
            _this.$message({
              showClose: false,
              message: '当前分类下暂时还没有文章上线，敬请期待！'
            })
          }
        } else {
          _this.$message({
            type: 'error',
            message: jsonData.message
          })
        }
      })
    },
    getArticleForCategory (categoryId) {
      let _this = this
      _this.loading = true
      getRequest('/article/categoryId/' + categoryId).then(resp => {
        let jsonData = resp.data
        if (jsonData && jsonData.code == 0) {
          _this.articles = jsonData.data
          if (_this.articles && _this.articles.length > 0) {
            console.log('articles valid')
          } else {
            _this.$message({
              showClose: false,
              message: '当前分类下暂时还没有文章上线，敬请期待！'
            })
          }
        } else {
          _this.$message({
            type: 'error',
            message: jsonData.message
          })
        }
      })
    },
    articleTitleClicked (index) {
      this.activeArticleIndex = index
    }
  },
  mounted () {
    this.initItemName()
  },
}
</script>

<style scoped>
.back_button {
  font-size: 1em;
  padding-left: 1em;
  padding-top: 2em;
  padding-bottom: 2em;
}

.content_aside {
  background-color: transparent;
  width: 30%;
  height: 100%;
  overflow: auto;
  padding-left: 1.5em;
}

.category_menu_parent {
  padding-top: 1.5em;
}

.html_content {
  text-align: left;
  width: 60%;
  height: 100%;
  padding-left: 1.5em;
}

.divider_view {
  width: 0.2em;
  background-color: beige;
}
</style>
