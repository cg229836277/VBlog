<template>
  <el-container>
    <el-container id="aside_container">
      <el-aside style="background-color: brown">
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
    </el-container>
    <el-container class="home_container">
      <el-header>
        <el-menu :default-active="activeIndex" class="header-el-menu" mode="horizontal"
                 @select="handleItemSelect" background-color="transparent" active-text-color="#1e90ff" router>
          <template v-for="(item,index) in this.$router.options.routes">
            <el-submenu :index="'' + index" :key="index"
                        v-if="item.children && item.children.length > 1 && item.children[0].menu_item"
                        class="header-el-submenu-life">
              <template slot="title">{{ item.name }}</template>
              <el-menu-item v-for="child in item.children" :index="child.path" :key="child.path">
                {{ child.name }}
              </el-menu-item>
            </el-submenu>
            <template v-else-if="item.children && !item.menu_item && item.children[0].menu_item">
              <el-menu-item :index="item.children[0].path" :key="index">{{ item.name }}</el-menu-item>
            </template>
          </template>
        </el-menu>
      </el-header>
      <el-container>
        <el-main>
          <router-view></router-view>
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
    },
  },
  data () {
    return {
      activeIndex: '/tech',
    }
  },
}
</script>

<style scoped>

.header-el-submenu-life {
  margin-left: 15%;
  margin-right: 15%;
}

#aside_container {
  align-items: center;
  margin-left: 2%;
}

.home_container {
  width: 75%
}

.aside_list {
  display: inline-block;
  position: absolute;
  top: 20%;
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
</style>
