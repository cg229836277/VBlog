<template>
  <div>
    <div style="display: flex;justify-content: flex-start;align-items: center;margin-top: 8px">
      <el-input
          type="textarea"
          autosize
          placeholder="添加音乐外链源码"
          v-model="musicContent" style="width: 480px">
      </el-input>
      <el-input
          placeholder="输入音乐标题"
          v-model="musicTitle" style="width: 180px;margin-left: 8px" size="mini">
      </el-input>
      <el-button type="primary" icon="el-icon-plus" size="mini" style="margin-left: 6px" @click="addClick">添加
      </el-button>

      <el-button type="primary" icon="el-icon-refresh-left" size="mini" style="margin-left: 6px" @click="resetContent">
        重置
      </el-button>
    </div>
    <div v-if="showPreview" v-html="musicContent" style="margin-top: 8px">

    </div>
    <el-table
        ref="multipleTable"
        :data="musicList"
        tooltip-effect="dark"
        style="width: 100%;overflow-x: hidden; overflow-y: hidden;margin-top: 8px"
        max-height="390"
        @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column
          label="标题"
          width="400" align="left">
        <template slot-scope="scope"><span style="color: #409eff;cursor: pointer"
                                           @click="itemClick(scope.row)">{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="left">
        <template slot-scope="scope">
          <el-button
              size="mini"
              @click="handleEdit(scope.row)">编辑
          </el-button>
          <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getRequest, postRequest } from '../utils/api'
import { formatCurDate } from '../utils/date'

export default {
  name: 'MusicList',
  mounted () {
    this.initMusicList()
  },
  data () {
    return {
      showPreview: false,
      musicContent: '',
      musicTitle: '',
      musicList: [],
      selectedItems: [],
      currentPage: 1,
      totalCount: -1,
      pageSize: 10,
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
            _this.$message({ type: 'info', message: '当前音乐列表为空，赶紧添加吧!' })
          }
        } else {
          _this.$message({ type: 'error', message: '音乐列表获取失败!' })
        }
        _this.loading = false
      })
    },
    handleSelectionChange (val) {
      this.selectedItems = val
    },
    addClick () {
      let _this = this
      _this.loading = true
      postRequest('/music/add', {
        title: _this.musicTitle,
        content: _this.musicContent,
        createTime: new Date()
      }).then(resp => {
        _this.loading = false
        let jsonData = resp.data
        if (jsonData && jsonData.code == 0) {
          _this.$message({ type: 'success', message: '音乐保存成功!' })
          _this.initMusicList()
        } else {
          _this.$message({ type: 'error', message: '音乐保存失败!' })
        }
      }, resp => {
        _this.loading = false
        _this.$message({ type: 'error', message: '音乐保存失败!' })
      })
    },
    itemClick (rowData) {
      this.musicContent = rowData.content
      this.musicTitle = rowData.title
      this.showPreview = true
    },
    handleEdit (rowData) {
      this.itemClick(rowData)
    },
    handleDelete (rowData) {
      let _this = this
      this.$confirm('将该音乐删除，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _this.loading = true
        postRequest('/music/delete', { id: rowData.id }).then(resp => {
          let responseData = resp.data
          if (responseData && responseData.code == 0) {
            _this.$message({ type: 'info', message: '删除成功!' })
            _this.initMusicList()
          } else {
            _this.$message({ type: 'error', message: '删除失败!' })
          }
          _this.loading = false
        })
      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    resetContent () {
      this.musicContent = ''
      this.musicTitle = ''
      this.showPreview = false
    }
  }
}
</script>

<style scoped>

</style>
