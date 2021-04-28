<template>
  <div class="markdown-body" v-html="htmlContent"></div>
</template>

<script>
import { getRequest } from '@/net'
import 'github-markdown-css'

export default {
  name: 'About',
  data () {
    return {
      title: 'About-ChuckChan',
      articleExist: false,
      htmlContent: '',
    }
  },
  methods: {
    getAboutArticle () {
      let _this = this
      _this.loading = true
      getRequest('/article/title/' + _this.title
      ).then(resp => {
        _this.loading = false
        let jsonData = resp.data
        if (jsonData && jsonData.code == 0) {
          let articleData = jsonData.data
          _this.htmlContent = articleData.htmlContent
          _this.articleExist = true
        } else {
          _this.articleExist = false
          _this.$message({ type: 'error', message: '还没有添加关于文章!' })
        }
      }, resp => {
        console.log(resp.data)
        _this.articleExist = false
        _this.loading = false
        _this.$message({ type: 'error', message: '关于文章加载失败!' })
      })
    }
  },
  mounted: function () {
    this.getAboutArticle()
  }
}
</script>

<style scoped>

</style>
