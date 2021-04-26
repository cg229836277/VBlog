<template>
  <el-main class="main">
    <div id="editor">
      <mavon-editor style="height: 100%;width: 100%;" ref=md v-model="article.originContent"></mavon-editor>
    </div>
    <div style="display: flex;align-items: center;margin-top: 15px;justify-content: flex-end">
      <el-button type="primary" @click="saveBlog(0)">发表文章</el-button>
    </div>
  </el-main>
</template>

<script>
import { isNotNullORBlank } from '../utils/utils'
import { getRequest, postRequest } from '../utils/api'
import { STATUS_ABOUT_ME, STATUS_PUBLISHED } from '../constant/status'
import { formatCurDate } from '../utils/date'
import { mavonEditor } from 'mavon-editor'

export default {
  name: 'EditAbout',
  methods: {
    saveBlog () {
      if (!(isNotNullORBlank(this.article.title, this.article.originContent))) {
        this.$message({ type: 'error', message: '数据不能为空!' })
        return
      }
      var _this = this
      _this.loading = true
      postRequest('/article/update', {
        title: _this.article.title,
        htmlContent: _this.$refs.md.d_render,
        originContent: _this.$refs.md.d_value,
        status: STATUS_ABOUT_ME,
        tags: _this.article.dynamicTags,
        author: _this.$store.getters.getUserName,
        publish_date: formatCurDate(),
        type: _this.article.type,
        create_time: _this.article.create_time.length > 0 ? _this.article.create_time : formatCurDate(),
        category_id: _this.article.category_id,
      }).then(resp => {
        _this.loading = false
        let jsonData = resp.data
        console.log('saveBlog:' + jsonData)
        if (jsonData && jsonData.code == 0) {
          _this.$message({ type: 'success', message: '保存成功!' })
        }
      }, resp => {
        _this.loading = false
        _this.$message({ type: 'error', message: '发布失败!' })
      })
    },
    getAboutArticle () {
      let _this = this
      _this.loading = true
      getRequest('/article/title/' + _this.article.title
      ).then(resp => {
        _this.loading = false
        let jsonData = resp.data
        if (jsonData && jsonData.code == 0) {
          let articleData = jsonData.data
          _this.article.htmlContent = articleData.htmlContent
          _this.article.originContent = articleData.originContent
          _this.article.title = articleData.title
          _this.article.category_id = articleData.category_id
          _this.article.category_name = articleData.type
          _this.article.create_time = articleData.create_time
          _this.article.dynamicTags = []

          let tags = articleData.tags
          let length = tags.length
          for (var i = 0; i < length; i++) {
            _this.article.dynamicTags.push(tags[i])
          }
          _this.articleExist = true
        } else {
          _this.articleExist = false
          _this.$message({ type: 'error', message: '还没有添加关于文章!' })
        }
      }, resp => {
        _this.articleExist = false
        _this.loading = false
        _this.$message({ type: 'error', message: '关于文章加载失败!' })
      })
    }
  },
  components: {
    mavonEditor
  }
  ,
  data () {
    return {
      articleExist: false,
      article: {
        dynamicTags: ['关于'],
        title: 'About-ChuckChan',
        htmlContent: '',
        originContent: '',
        create_time: '',
        category_id: '-1',
        category_name: '-1',
        type: '-1',
      }
    }
  },
  mounted: function () {
    this.getAboutArticle()
  }
}
</script>

<style scoped>
#editor {
  width: 100%;
  height: 80%;
  text-align: left;
}
</style>
