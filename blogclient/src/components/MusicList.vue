<template>
  <div class="music-list-parent">
    <div class="music-list-child">
      <template v-for="(value,index) in musicList">
        <div v-html="value.content" :key="index"></div>
      </template>
    </div>
  </div>
</template>

<script>
import { getRequest } from '@/net'

export default {
  name: 'MusicList',
  mounted () {
    this.initMusicList()
  },
  data () {
    return {
      musicList: [],
    }
  },
  methods: {
    initMusicList () {
      let _this = this
      _this.loading = true
      getRequest('/music/all').then(resp => {
        let responseData = resp.data
        if (responseData && responseData.code == 0) {
          let musicData = responseData.data
          if (musicData && musicData.length > 0) {
            _this.musicList = musicData
          } else {
            _this.musicList = []
            _this.$message({ type: 'info', message: '当前音乐列表为空，赶紧通过微信公众号通知管理员添加吧!' })
          }
        } else {
          _this.$message({ type: 'error', message: '音乐列表获取失败!' })
        }
        _this.loading = false
      })
    },
  }
}
</script>

<style scoped>
.music-list-parent {
  width: 85%;
  border: 1px solid #ebedef;
  border-radius: 5px;
  display: flex;
  justify-content: center;
}

.music-list-child {
  align-items: center;
}
</style>
