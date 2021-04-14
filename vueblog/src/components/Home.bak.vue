<template>
  <el-row class="container">
    <el-col :span="12" class="header">
      <div class="title">V部落博客管理平台</div>
    </el-col>
    <el-col :span="12" class="header">
      <div class="userinfoContainer">
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link userinfo">
            {{ currentUserName }}<i class="el-icon-arrow-down el-icon--right userinfo"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="sysMsg">系统消息</el-dropdown-item>
            <el-dropdown-item command="MyArticle">我的文章</el-dropdown-item>
            <el-dropdown-item command="MyHome">个人主页</el-dropdown-item>
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-col>
  </el-row>
</template>
<script>
export default {
  methods: {
    handleCommand (command) {
      var _this = this
      if (command == 'logout') {
        this.$confirm('注销登录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          _this.$http.get('/logout')
          _this.currentUserName = '游客'
          _this.$router.replace({ path: '/' })
        }, function () {
          //取消
        })
      }
    }
  },
  mounted: function () {
    this.$http.get('/currentUserName').then(function (msg) {
      this.currentUserName = msg.bodyText
    }, function (msg) {
      this.currentUserName = '游客'
    })
  },
  data () {
    return {
      currentUserName: ''
    }
  }
}
</script>
<style>
.container {
  position: absolute;
  top: 0px;
  left: 0px;
  width: 100%;
  background-color: rgba(235, 235, 235, 0.08);
}

.header {
  background-color: #20a0ff;
  height: 60px;
}

.title {
  color: #fff;
  font-size: 22px;
  display: flex;
  align-items: center;
  margin-left: 20px;
  height: 60px;
}

.userinfo {
  color: #fff;
  cursor: pointer;
}

.userinfoContainer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 60px;
  margin-right: 20px;
}
</style>
