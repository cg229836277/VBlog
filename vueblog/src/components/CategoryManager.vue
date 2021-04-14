<template>
  <el-container>
    <el-header class="cate_mana_header">
      <el-input
          placeholder="请输入父一级栏目名称"
          v-model="parentName" style="width: 120px;">
      </el-input>
      <el-input
          placeholder="请输入子一级栏目名称"
          v-model="childName" style="width: 120px;margin-left: 10px">
      </el-input>

      <el-input
          placeholder="请输入描述"
          v-model="categoryDesc" style="width: 200px;margin-left: 10px">
      </el-input>
      <el-button type="primary" size="medium" style="margin-left: 10px" @click="addCategory">新增栏目</el-button>
    </el-header>
    <el-main class="cate_mana_main">
      <el-table
          ref="multipleTable"
          :data="categories"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange" v-loading="loading">
        <el-table-column
            type="selection"
            width="55" align="left">
        </el-table-column>
        <el-table-column
            label="编号"
            prop="id"
            width="120" align="left">
        </el-table-column>
        <el-table-column
            label="父栏目名称"
            prop="parentName"
            width="120" align="left">
        </el-table-column>
        <el-table-column
            label="子栏目名称"
            prop="childName"
            width="120" align="left">
        </el-table-column>
        <el-table-column
            label="描述"
            prop="categoryDesc"
            width="120" align="left">
        </el-table-column>
        <el-table-column
            prop="createTime"
            label="启用时间" align="left">
          <template slot-scope="scope">{{ scope.row.date | formatDate }}</template>
        </el-table-column>
        <el-table-column label="操作" align="left">
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="editCategory(scope.row)">编辑
            </el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button type="danger" :disabled="this.deleteItems.length==0" style="margin-top: 10px;width: 100px;"
                 @click="deleteChoseAll" v-if="this.categories.length>0">批量删除
      </el-button>

      <el-dialog title="编辑栏目" :visible="dialogFormVisible" :show-close="false" v-model="categoryItem">
        <template slot-scope="scope">
          <el-form label-width="200px">
            <el-form-item label="父一级名称">
              <el-input v-model="categoryItem.parentName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="子一级名称">
              <el-input v-model="categoryItem.childName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="categoryItem.categoryDesc" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="commitCategory(categoryItem)">确 定</el-button>
          </div>
        </template>
      </el-dialog>
    </el-main>
  </el-container>
</template>
<script>
import { postRequest } from '../utils/api'
import { getRequest } from '../utils/api'

export default {
  methods: {
    addCategory () {
      this.loading = true
      var _this = this
      let createTime = new Date()
      if (!_this.parentName || !_this.childName) {
        this.loading = false
        _this.$message({
          type: 'info',
          message: '两个栏目内容不能为空!'
        })
        return
      }
      postRequest('/category/add', {
        createTime: createTime,
        parentName: _this.parentName,
        childName: _this.childName,
        categoryDesc: _this.categoryDesc,
      }).then(resp => {
        let jsonData = resp.data
        if (jsonData && jsonData.code == 0) {
          _this.$message({ type: jsonData.code, message: jsonData.message })
          _this.refresh()
        }
        _this.loading = false
      }, resp => {
        if (resp.response.status == 403) {
          _this.$message({
            type: 'error',
            message: resp.response.data
          })
        }
        _this.loading = false
      })
    },
    deleteChoseAll () {
      this.$confirm('确认删除这 ' + this.deleteItems.length + ' 条数据?', '提示', {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        this.deleteAllCategory(this.deleteItems)
      }).catch(() => {
        //取消
        this.loading = false
      })
    },
    handleSelectionChange (item) {
      this.deleteItems = item
    },
    handleDelete (index, row) {
      let _this = this
      this.$confirm('确认删除 ' + row.parentName + '/' + row.childName + ' ?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _this.deleteCategory(row.id)
      }).catch(() => {
        //取消
        _this.loading = false
      })
    },
    deleteAllCategory () {
      this.loading = true
      let deleteIds = []
      let length = this.deleteItems.length
      for (var index = 0; index < length; index++) {
        let deleteItem = this.deleteItems[index]
        let id = deleteItem.id
        deleteIds.push(id)
      }
      //删除
      postRequest('/category/delete/ids', { ids: deleteIds }).then(resp => {
        let jsonData = resp.data
        this.$message({
          type: jsonData.code,
          message: jsonData.message
        })
        this.refresh()
      }, resp => {
        this.loading = false
        if (resp.response.status == 403) {
          this.$message({
            type: 'error',
            message: resp.response.data
          })
        } else if (resp.response.status == 500) {
          this.$message({
            type: 'error',
            message: '该栏目下尚有文章，删除失败!'
          })
        }
      })
    },
    deleteCategory (id) {
      var _this = this
      this.loading = true
      //删除
      postRequest('/category/delete', { id: id }).then(resp => {
        let jsonData = resp.data
        console.log('/category/delete:' + jsonData)
        _this.$message({
          type: jsonData.code,
          message: jsonData.message
        })
        _this.refresh()
      }, resp => {
        _this.loading = false
        _this.$message({
          type: 'error',
          message: resp.response.data
        })
      })
    },
    refresh () {
      let _this = this
      getRequest('/category/all').then(resp => {
        let jsonData = resp.data
        if (jsonData && jsonData.code == 0) {
          _this.categories = jsonData.data
        } else {
          _this.$message({
            type: 'error',
            message: jsonData.message
          })
        }
        _this.loading = false
      })
    },
    editCategory (row) {
      this.dialogFormVisible = true
      this.categoryItem = {
        parentName: row.parentName,
        childName: row.childName,
        categoryId: row.id,
        categoryDesc: row.categoryDesc,
      }
    },
    commitCategory (modifiedCategory) {
      this.dialogFormVisible = false
      this.loading = true
      console.log('categoryId is ' + modifiedCategory.categoryId + ',parentName:' + modifiedCategory.parentName + ',childName:' + modifiedCategory.childName)
      postRequest('/category/update', {
        parentName: modifiedCategory.parentName,
        childName: modifiedCategory.childName,
        createTime: new Date(),
        id: modifiedCategory.categoryId,
        categoryDesc: modifiedCategory.categoryDesc,
      }).then(resp => {
        var json = resp.data
        this.$message({
          type: json.code,
          message: json.message
        })
        this.refresh()
      }, resp => {
        this.$message({
          type: 'error',
          message: resp.response.data
        })
        this.loading = false
      })
    }
  },
  mounted: function () {
    this.loading = true
    this.refresh()
  },
  data () {
    return {
      dialogFormVisible: false,
      parentName: '',
      childName: '',
      categoryDesc: '',
      categoryName: '',
      deleteItems: [],
      categories: [],
      loading: false,
      categoryItem: {
        categoryId: 0,
        parentName: '',
        childName: '',
        createTime: new Date(),
        categoryDesc: ''
      }
    }
  }
}
</script>
<style>
.cate_mana_header {
  background-color: #ececec;
  margin-top: 20px;
  padding-left: 5px;
  display: flex;
  justify-content: flex-start;
}

.cate_mana_main {
  /*justify-content: flex-start;*/
  display: flex;
  flex-direction: column;
  padding-left: 5px;
  background-color: #ececec;
  margin-top: 20px;
  padding-top: 10px;
}
</style>
