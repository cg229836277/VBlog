<template>
  <el-container v-loading="loading" class="post-article">
    <el-header class="header">
      <el-select v-model="article.type" placeholder="请选择文章栏目" style="width: 150px;">
        <el-option-group
            v-for="(item, index) in categoryParent"
            :key="item"
            :label="item">
          <el-option
              v-for="itemValue in categoryChild[index]"
              :key="itemValue"
              :label="itemValue"
              :value="itemValue">
          </el-option>
        </el-option-group>
      </el-select>
      <el-input v-model="article.title" placeholder="请输入标题..." style="width: 400px;margin-left: 10px"></el-input>
      <el-tag
          :key="tag"
          v-for="tag in article.dynamicTags"
          closable
          :disable-transitions="false"
          @close="handleClose(tag)" style="margin-left: 10px">
        {{ tag }}
      </el-tag>
      <el-input
          class="input-new-tag"
          v-if="tagInputVisible"
          v-model="tagValue"
          ref="saveTagInput"
          size="small"
          @keyup.enter.native="handleInputConfirm"
          @blur="handleInputConfirm">
      </el-input>
      <el-button v-else class="button-new-tag" type="primary" size="small" @click="showInput">+Tag</el-button>
    </el-header>
    <el-main class="main">
      <div id="editor">
        <mavon-editor style="height: 100%;width: 100%;" ref=md v-model="article.originContent"></mavon-editor>
      </div>
      <div style="display: flex;align-items: center;margin-top: 15px;justify-content: flex-end">
        <el-button @click="cancelEdit" v-if="from!=undefined">放弃修改</el-button>
        <template v-if="from==undefined || from=='draft'">
          <el-button @click="saveBlog(1)">保存到草稿箱</el-button>
          <el-button type="primary" @click="saveBlog(0)">发表文章</el-button>
        </template>
        <template v-else="from==post">
          <el-button type="primary" @click="saveBlog(1)">保存修改</el-button>
        </template>
      </div>
    </el-main>
  </el-container>
</template>
<script>
import { postRequest } from '../utils/api'
import { getRequest } from '../utils/api'
import { mavonEditor } from 'mavon-editor'
// 可以通过 mavonEditor.markdownIt 获取解析器markdown-it对象
import 'mavon-editor/dist/css/index.css'
import { isNotNullORBlank } from '../utils/utils'
import { formatCurDate } from '../utils/date'

export default {
  mounted: function () {
    this.getCategories()
    var from = this.$route.query.from
    this.from = from
    var _this = this
    if (from) {
      var id = this.$route.query.id
      this.id = id
      this.loading = true
      getRequest('/article/id/' + id).then(resp => {
        _this.loading = false
        let jsonData = resp.data
        if (jsonData && jsonData.code == 0) {
          let articleData = jsonData.data
          _this.article.originContent = articleData.originContent
          _this.article.htmlContent = articleData.htmlContent
          _this.article.title = articleData.title
          _this.article.category_id = articleData.category_id
          _this.article.category_name = articleData.type
          _this.article.create_time = articleData.create_time
          _this.article.dynamicTags = []
          _this.article.type = articleData.type

          let tags = articleData.tags
          let length = tags.length
          for (var i = 0; i < length; i++) {
            _this.article.dynamicTags.push(tags[i])
          }
        } else {
          _this.$message({ type: 'error', message: '页面加载失败!' })
        }
      }, resp => {
        _this.loading = false
        _this.$message({ type: 'error', message: '页面加载失败!' })
      })
    }
  },
  components: {
    mavonEditor
  },
  methods: {
    cancelEdit () {
      this.$router.go(-1)
    },
    saveBlog (state) {
      if (!(isNotNullORBlank(this.article.title, this.article.originContent))) {
        this.$message({ type: 'error', message: '数据不能为空!' })
        return
      }
      var _this = this
      _this.loading = true
      let requestUrl = _this.from ? '/article/update' : '/article/upload'
      postRequest(requestUrl, {
        id: _this.id,
        title: _this.article.title,
        originContent: _this.$refs.md.d_value,//this.$refs.md.d_value
        htmlContent: _this.$refs.md.d_render,//this.$refs.md.d_render
        status: state,
        tags: _this.article.dynamicTags,
        author: _this.$store.getters.getUserName,
        publish_date: formatCurDate(),
        type: _this.article.type,
        create_time: _this.article.create_time.length > 0 ? _this.article.create_time : formatCurDate(),
        category_id: this.getCategoryId(),
      }).then(resp => {
        _this.loading = false
        let jsonData = resp.data
        console.log('saveBlog:' + jsonData)
        if (jsonData && jsonData.code == 0) {
          _this.$message({ type: 'success', message: state == 0 ? '保存成功!' : '发布成功!' })
          window.bus.$emit('blogTableReload')
          _this.$router.replace({ path: '/articleList' })
        } else {
          _this.$message({ type: 'error', message: state == 0 ? '保存草稿失败!' : '博客发布失败!' })
        }
      }, resp => {
        _this.loading = false
        _this.$message({ type: 'error', message: state == 0 ? '保存草稿失败!' : '博客发布失败!' })
      })
    },
    getCategories () {
      var _this = this
      getRequest('/category/all').then(resp => {
        let jsonData = resp.data
        if (jsonData != null && jsonData.code == 0) {
          _this.categories = jsonData.data
          _this.setCategoryMap()
        } else {
          _this.$message({ type: 'error', message: '获取文章类型失败' })
        }
      })
    },
    handleClose (tag) {
      this.article.dynamicTags.splice(this.article.dynamicTags.indexOf(tag), 1)
    },
    showInput () {
      this.tagInputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleInputConfirm () {
      var _this = this
      let tagValue = _this.tagValue
      if (tagValue) {
        _this.article.dynamicTags.push(tagValue)
      }
      _this.tagInputVisible = false
      _this.tagValue = ''
    },
    getCategoryId () {
      var _this = this
      let length = _this.categories.length
      for (var index = 0; index < length; index++) {
        let item = _this.categories[index]
        if (item.childName == _this.article.type) {
          return item.id
        }
      }
    },
    setCategoryMap () {
      var _this = this
      let categoryMap = new Map()
      let length = _this.categories.length
      for (var index = 0; index < length; index++) {
        var categoryItem = _this.categories[index]
        let parentName = categoryItem.parentName
        let childName = categoryItem.childName
        console.log(parentName + ':' + childName)
        if (categoryMap.get(parentName)) {
          let itemArray = categoryMap.get(parentName)
          itemArray.push(childName)
          categoryMap.set(parentName, itemArray)
        } else {
          let itemArray = []
          itemArray.push(childName)
          categoryMap.set(parentName, itemArray)
        }
      }
      this.categoryParent = Array.from(categoryMap.keys())
      this.categoryChild = Array.from(categoryMap.values())
      console.log('categoryMap values:' + this.categoryChild)
      console.log('categoryMap keys:' + this.categoryParent)
    },
  }
  ,
  data () {
    return {
      id: '',
      categories: [],
      categoryParent: [],
      categoryChild: [],
      tagValue: '',
      loading: false,
      from: '',
      tagInputVisible: true,
      article: {
        dynamicTags: [],
        title: '',
        originContent: '',
        htmlContent: '',
        create_time: '',
        category_id: '',
        category_name: '',
        type: '',
      }
    }
  }
}
</script>
<style>
.post-article > .main > #editor {
  width: 100%;
  height: 450px;
  text-align: left;
}

.post-article > .header {
  background-color: #ececec;
  margin-top: 10px;
  padding-left: 5px;
  display: flex;
  justify-content: flex-start;
}

.post-article > .main {
  /*justify-content: flex-start;*/
  display: flex;
  flex-direction: column;
  padding-left: 5px;
  background-color: #ececec;
  padding-top: 0px;
}

.post-article > .header > .el-tag + .el-tag {
  margin-left: 10px;
}

.post-article > .header > .button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.post-article > .header > .input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}

.post-article {
}
</style>
