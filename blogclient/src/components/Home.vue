<template>
  <el-container class="home_container">
    <el-header>
      <el-menu :default-active="activeIndex" class="header-el-menu" mode="horizontal"
               @select="handleItemSelect" background-color="transparent" active-text-color="#1e90ff">
        <el-menu-item index="1" class="header-el-submenu-main">主页</el-menu-item>
        <el-submenu index="2" class="header-el-submenu-life">
          <template slot="title">生活</template>
          <el-menu-item index="2-1" class="child-menu-1">读书</el-menu-item>
          <el-menu-item index="2-2" class="child-menu-2">歌单</el-menu-item>
        </el-submenu>
        <el-menu-item index="3" class="header-el-submenu-about">关于</el-menu-item>
      </el-menu>

    </el-header>
    <el-container>
      <el-aside width="200px">
        <ul class="aside_list">
          <li><img class="aside_icon" src="../assets/header_icon.svg" alt="网站个人图标" title="网站图标" width="96px"
                   height="96px"/></li>
          <li><p class="aside_name">Chuck Chan</p></li>
          <li><img class="aside_img" src="../assets/motto.svg" width="24em" height="24em"/>
            <p>水清，濯吾缨；水浊，濯吾足。</p>
          </li>
          <li><img class="aside_img" src="../assets/location.svg" width="24em"
                   height="24em"/>
            <p>深圳/广东</p></li>
          <li><img class="aside_img" src="../assets/email.svg" width="24em" height="24em"/>
            <a href="mailto:1301958187@qq.com?subject = From ChuckChan WebSite&body = Nice to meet you!">
              1301958187@qq.com
            </a>
          </li>
          <li><img class="aside_img" src="../assets/wechatblog.svg" width="24em"
                   height="24em"/><a href="../assets/wechat.svg">Android部落格</a>
          </li>
          <li><img class="aside_img" src="../assets/link.svg" width="24em" height="24em"/><a
              href="https://chengang.plus">个人网站</a></li>
        </ul>
      </el-aside>
      <el-container>
        <el-main>
        </el-main>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Home',
  methods: {
    handleItemSelect (key, keyPath) {
      console.log(key, keyPath)
    },
    handleTabItemCommand (command) {
      console.log(command)
    }
  },
  data () {
    return {
      activeIndex: '1',
      categories: {
        childName: [],
        parentName: [],
      },
    }
  }
}
</script>

<style scoped>
.el-menu {
  padding-left: 30%;
  padding-right: 20%;
}

.header-el-submenu-life {
  margin-left: 15%;
  margin-right: 15%;
}

.el-aside {
  align-items: center;
  display: inline-block;
  width: 20%;
  margin-left: 2%;
}

.aside_list {
  display: inline-block;
  position: absolute;
  top: 30%;
  list-style-type: none;
  align-items: center;
}

.aside_list li {
  display: flex;
  margin-top: 0.5em;
  list-style-type: none;
}

.aside_name {
  font-size: 1.5em;
}

.aside_img {
  margin: auto 0;
  display: inline-block;
}

.aside_list li p {
  margin: auto 0;
  display: inline;
  padding-left: 0.3em;
}

.aside_list li a {
  margin: auto 0;
  padding-left: 0.5em;
}

.article_grid {
  display: inline-block;
  width: 70%;
  margin-top: 3%;
  column-count: 2;
}

.article_grid_item_margin {
  margin-top: 2em;
}

.article_grid_item, .article_grid_item_margin {
  width: 30em;
  height: 10em;
  border: 1px solid #ACACAC;
  border-radius: 10px;
}

.article_grid_item_img {
  margin-top: 1.2em;
  margin-left: 1em;
  margin-right: 0.1em;
  display: inline-block;
  vertical-align: middle;
}

.article_grid_item_title {
  color: dodgerblue;
  display: inline-block;
  vertical-align: bottom;
}

.article_grid_item_detail {
  margin: 1em 1.2em;
}
</style>
