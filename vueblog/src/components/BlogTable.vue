<style type="text/css">
.blog_table_footer {
  display: flex;
  box-sizing: content-box;
  padding-top: 10px;
  padding-bottom: 0px;
  margin-bottom: 0px;
  justify-content: space-between;
}
</style>
<template>
  <div>
    <div style="display: flex;justify-content: flex-start">
      <el-input
          placeholder="通过标题搜索该分类下的博客..."
          prefix-icon="el-icon-search"
          v-model="keywords" style="width: 400px" size="mini">
      </el-input>
      <el-button type="primary" icon="el-icon-search" size="mini" style="margin-left: 3px" @click="searchClick">搜索
      </el-button>
    </div>
    <!--<div style="width: 100%;height: 1px;background-color: #20a0ff;margin-top: 8px;margin-bottom: 0px"></div>-->
    <el-table
        ref="multipleTable"
        :data="articles"
        tooltip-effect="dark"
        style="width: 100%;overflow-x: hidden; overflow-y: hidden;"
        max-height="390"
        @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column
          type="selection"
          width="35" align="left" v-if="showEdit || showDelete">
      </el-table-column>
      <el-table-column
          label="标题"
          width="400" align="left">
        <template slot-scope="scope"><span style="color: #409eff;cursor: pointer"
                                           @click="itemClick(scope.row)">{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column
          label="最近编辑时间" width="140" align="left">
        <template slot-scope="scope">{{ scope.row.publish_date | formatDateTime }}</template>
      </el-table-column>
      <el-table-column
          prop="author"
          label="作者"
          width="120" align="left">
      </el-table-column>
      <el-table-column
          prop="type"
          label="所属分类"
          width="120" align="left">
      </el-table-column>
      <el-table-column label="操作" align="left" v-if="showEdit || showDelete">
        <template slot-scope="scope">
          <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)" v-if="showEdit">编辑
          </el-button>
          <el-button
              size="mini"
              @click="handleRestore(scope.$index, scope.row)" v-if="showRestore">还原
          </el-button>
          <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)" v-if="showDelete">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="blog_table_footer">
      <el-button type="danger" size="mini" style="margin: 0px;" v-show="this.articles.length>0 && showDelete"
                 :disabled="this.selItems.length===0" @click="deleteMany">批量删除
      </el-button>
      <span></span>
      <el-pagination
          background
          :page-size="pageSize"
          layout="prev, pager, next"
          :total="totalCount" @current-change="currentChange" v-show="this.articles.length>0">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { putRequest } from '../utils/api'
import { getRequest } from '../utils/api'
import { STATUS_ALL } from '../constant/status'
import { STATUS_STORED } from '../constant/status'
import { STATUS_UNFINISHED } from '../constant/status'
import { STATUS_DELETED } from '../constant/status'

export default {
  data () {
    return {
      articles: [],
      selItems: [],
      loading: false,
      currentPage: 1,
      totalCount: -1,
      pageSize: 10,
      keywords: '',
      dustbinData: []
    }
  },
  mounted: function () {
    var _this = this
    this.loading = true
    this.loadBlogs()
    window.bus.$on('blogTableReload', function () {
      _this.loading = true
      _this.loadBlogs()
    })
  },
  methods: {
    searchClick () {
      this.currentPage = 1
      this.loadBlogs()
    },
    itemClick (row) {
      this.$router.push({ path: '/blogDetail', query: { aid: row.id } })
    },
    deleteMany () {
      var selItems = this.selItems
      for (var i = 0; i < selItems.length; i++) {
        this.dustbinData.push(selItems[i].id)
      }
      this.deleteToDustBin(selItems[0].state)
    },
    //翻页
    currentChange (currentPage) {
      this.currentPage = currentPage
      this.loading = true
      this.loadBlogs()
    },
    loadBlogs () {
      var _this = this
      let url = '/article/status'
      var status = STATUS_ALL
      // console.log('current state is ' + this.state)
      if (this.state == -1) {
        status = STATUS_ALL
      } else if (this.state == 1) {
        status = STATUS_STORED
      } else if (this.state == 0) {
        status = STATUS_UNFINISHED
      } else if (this.state == 2) {
        status = STATUS_DELETED
      }
      console.log('current status is ' + status)
      getRequest(url, { status: status, pageIndex: _this.currentPage, pageSize: _this.pageSize }).then(response => {
        _this.loading = false
        let articleJson = response.data
        if (articleJson != null && articleJson.code === 0) {
          let articleData = articleJson.data.articleDOList
          _this.articles = articleData
          _this.totalCount = articleData.pageSize
        } else {
          _this.$message({ type: 'error', message: '数据加载失败!' })
        }
      }, response => {
        _this.loading = false
        if (response.response.status === 403) {
          _this.$message({ type: 'error', message: response.response.data })
        } else {
          _this.$message({ type: 'error', message: '数据加载失败!' })
        }
      }).catch(_ => {
        //压根没见到服务器
        _this.loading = false
        _this.$message({ type: 'error', message: '数据加载失败!' })
      })
    },
    handleSelectionChange (val) {
      this.selItems = val
    },
    handleEdit (index, row) {
      this.$router.push({ path: '/editBlog', query: { from: this.activeName, id: row.id } })
    },
    handleDelete (index, row) {
      this.dustbinData.push(row.id)
      this.deleteToDustBin(row.state)
    },
    handleRestore (index, row) {
      let _this = this
      this.$confirm('将该文件还原到原处，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _this.loading = true
        putRequest('/article/restore', { articleId: row.id }).then(resp => {
          if (resp.status === 200) {
            var data = resp.data
            _this.$message({ type: data.status, message: data.msg })
            if (data.status === 'success') {
              window.bus.$emit('blogTableReload')//通过选项卡都重新加载数据
            }
          } else {
            _this.$message({ type: 'error', message: '还原失败!' })
          }
          _this.loading = false
        })
      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '已取消还原'
        })
      })
    },
    deleteToDustBin (state) {
      var _this = this
      this.$confirm(state !== 2 ? '将该文件放入回收站，是否继续?' : '永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _this.loading = true
        var url = ''
        if (_this.state === -2) {
          url = '/admin/article/dustbin'
        } else {
          url = '/article/dustbin'
        }
        putRequest(url, { aids: _this.dustbinData, state: state }).then(resp => {
          if (resp.status === 200) {
            var data = resp.data
            _this.$message({ type: data.status, message: data.msg })
            if (data.status === 'success') {
              window.bus.$emit('blogTableReload')//通过选项卡都重新加载数据
            }
          } else {
            _this.$message({ type: 'error', message: '删除失败!' })
          }
          _this.loading = false
          _this.dustbinData = []
        }, resp => {
          _this.loading = false
          _this.$message({ type: 'error', message: '删除失败!' })
          _this.dustbinData = []
        })
      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '已取消删除'
        })
        _this.dustbinData = []
      })
    }
  },
  props: ['state', 'showEdit', 'showDelete', 'activeName', 'showRestore']
}
</script>
