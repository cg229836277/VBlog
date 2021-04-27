<template>
  <el-container direction="vertical" class="document-main">
    <!--    <div style="text-align: left;">-->
    <!--      <el-button type="text" icon="el-icon-back" @click="goBack" class="back_button">返回-->
    <!--      </el-button>-->
    <!--    </div>-->
    <!--    <el-container direction="horizontal">-->
    <!--      <keep-alive>-->
    <!--        <el-aside class="content_aside">-->
    <!--          <template v-for="(childItem, index) in categories" class="category_menu_parent">-->
    <!--            <el-button plain :key="index" @click="getArticleForCategory(childItem.id)">{{-->
    <!--                childItem.childName-->
    <!--              }}-->
    <!--            </el-button>-->
    <!--          </template>-->
    <!--          <el-menu default-active="0" background-color="transparent">-->
    <!--            <template v-for="(contentItem, index) in articles">-->
    <!--              <el-menu-item :key="index">-->
    <!--                <el-link type="primary" :key="index" @click="articleTitleClicked(index)">{{-->
    <!--                    contentItem.title-->
    <!--                  }}-->
    <!--                </el-link>-->
    <!--              </el-menu-item>-->
    <!--            </template>-->
    <!--          </el-menu>-->
    <!--        </el-aside>-->
    <!--      </keep-alive>-->
    <!--      <div class="divider_view"></div>-->
    <!--          <div class="html_content" v-html="articles[activeArticleIndex].content">-->
    <!--          </div>-->
    <!--    </el-container>-->
    <header class="header">
      <a class="logo" href="/">
        <img src="../images/logo.png" alt="Chuck的主页">
      </a>
      <ul class="header-nav">
        <li>
          <form class="header-search-form">
            <input type="text" id="search-query-nav" class="header-search" aria-label="搜索" autocomplete="off"
                   spellcheck="false" role="combobox" aria-autocomplete="list" aria-expanded="false"
                   aria-owns="algolia-autocomplete-listbox-0" dir="auto" style="">
            <pre aria-hidden="true"
                 style="position: absolute; visibility: hidden; white-space: pre; font-family: Arial; font-size: 13.3333px; font-style: normal; font-variant: normal; font-weight: 400; word-spacing: 0px; letter-spacing: normal; text-indent: 0px; text-rendering: auto; text-transform: none;"></pre>
          </form>
        </li>
      </ul>
    </header>
    <div id="main" class="fix-sidebar">
      <div class="sidebar">
        <h3>

          分类

          <select class="category-select" v-model="selected" @change="categoryOnChanged($event)">
            <template v-for="(childItem, index) in categories">
              <option :value="index" :key="index">{{ childItem.childName }}
              </option>
            </template>
          </select>
        </h3>
        <ul class="menu-root">
          <template v-for="(contentItem, index) in articles">
            <li @click="articleTitleClicked(index)" :key="index">
              <a data-scroll href="#">{{ contentItem.title }}</a>
            </li>
          </template>
        </ul>
      </div>
      <div class="content api with-sidebar markdown-body">
        <h1 class="article-title">{{ articles[activeArticleIndex].title }}</h1>
        <p class="article-author">作者：{{ articles[activeArticleIndex].author }}</p>
        <p class="article-publish-date">发布时间：{{ articles[activeArticleIndex].publish_date }}</p>
        <div v-html="articles[activeArticleIndex].htmlContent"></div>
      </div>
    </div>
  </el-container>
</template>

<script>
import {getRequest} from '@/net'
import 'github-markdown-css'

export default {
  name: 'TechDetail',
  data() {
    return {
      itemName: '',
      itemId: '',
      activeArticleIndex: 0,
      selected: 0,
      categories: [],
      articles: [],
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    initItemName() {
      let pageParameters = this.$route.params
      this.itemName = pageParameters.item_name
      this.itemId = pageParameters.item_id
      console.log('item_name=' + this.itemName)
      this.getCurrentChildCategory()
    },
    getCurrentChildCategory() {
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
    categoryOnChanged(event) {
      let selectedIndex = event.target.value
      console.log('selectedIndex:' + selectedIndex)
      this.getArticleForCategory(this.categories[selectedIndex].id)
    },
    getArticleForCategory(categoryId) {
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
    articleTitleClicked(index) {
      this.activeArticleIndex = index
    }
  },
  mounted() {
    this.initItemName()
  },
}
</script>

<style scoped>
.document-main {
  padding-top: 61px;
}

#main.fix-sidebar {
  position: static;
}

#main {
  position: relative;
  z-index: 1;
  padding: 0 60px 30px;
  overflow-x: hidden;
}

#main.fix-sidebar .sidebar {
  position: fixed;
  width: 260px;
  /* 上边 | 右边 | 下边 | 左边 */
  padding: 35px 0px 60px 20px;
}

.sidebar {
  position: absolute;
  z-index: 10;
  top: 61px;
  left: 0;
  bottom: 0;
  overflow-x: hidden;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  -ms-overflow-style: none;
}

.sidebar .menu-root {
  padding-left: 0;
}

.sidebar ul {
  list-style-type: none;
  margin: 0;
  line-height: 1.5em;
  padding-left: 1em;
}

.sidebar li {
  margin-top: 0.5em;
}

a {
  text-decoration: none;
  color: #304455;
}

a:-webkit-any-link {
  color: -webkit-link;
  cursor: pointer;
  text-decoration: underline;
}

@media screen and (max-width: 1300px) {
  .content.with-sidebar {
    margin-left: 290px;
  }
}

.content {
  position: relative;
  max-width: 700px;
  margin: 0 auto;
  /*!* 上边 | 右边 | 下边 | 左边 *!*/
  padding: 35px 0 35px 50px;
}

.article-title, .article-author, .article-publish-date {
  width: 100%;
  text-align: center;
}

.header {
  position: fixed;
  width: 100%;
  top: 0;
  box-shadow: 0 0 1px rgb(0 0 0);
  transition: background-color 0.3s ease-in-out;
  background-color: #dbdbdb;
  height: 40px;
  padding: 10px 60px;
  z-index: 20;
}

@media screen and (max-width: 900px) {
  .sidebar {
    display: none;
  }
}

.logo {
  display: inline-block;
  font-size: 1.5em;
  line-height: 40px;
  color: #273849;
  font-family: "Dosis", "Source Sans Pro", "Helvetica Neue", Arial, sans-serif;
  font-weight: 500;
  text-decoration: none;
}

.header-search {
  height: 30px;
  line-height: 30px;
  box-sizing: border-box;
  padding: 0 15px 0 30px;
  border: 1px solid #e3e3e3;
  color: #273849;
  outline: none;
  border-radius: 15px;
  margin-right: 10px;
  transition: border-color 0.2s ease;
  background: #fff url(../images/search.svg) 8px 5px no-repeat;
  background-size: 20px;
  vertical-align: middle !important;
}

.logo img {
  vertical-align: middle;
  margin-right: 6px;
  width: 40px;
  height: 40px;
  border: none;
}

.header-nav {
  list-style-type: none;
  margin: 0;
  padding: 0;
  position: fixed;
  right: 30px;
  top: 10px;
  height: 40px;
  line-height: 40px;
}

.header-nav li {
  display: inline-block;
  position: relative;
  margin: 0 0.6em;
}

pre {
  font-family: "Roboto Mono", Monaco, courier, monospace;
  font-size: 0.85em;
  background-color: #f8f8f8;
  -webkit-font-smoothing: initial;
  -moz-osx-font-smoothing: initial;
  border-radius: 2px;
  position: relative;

}
</style>
