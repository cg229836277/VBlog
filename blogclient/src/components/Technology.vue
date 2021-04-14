<template>
  <article class="article_grid">
    <template v-for="item in articleGridItems">
      <div :style="item.item_should_margin?item_style_margin:item_style" :class="item.item_class"
           v-on:submit.prevent="gridItemClicked(item)">
        <img :class="item.img_item_class" :src="item.img_item_src" :width="item.img_item_width"
             :height="item.img_item_height"/>
        <p v-bind:class="item.item_title_class">{{ item.item_title }}</p>
        <p v-bind:class="item.item_detail_class">{{ item.item_detail }}</p>
      </div>
    </template>
  </article>
</template>

<script>
import { getRequest } from '@/net'

export default {
  name: 'Technology',
  data () {
    return {
      item_style_margin: {
        width: '30em',
        height: '10em',
        border: '1px solid #ACACAC',
        borderRadius: '10px',
        marginTop: '2em',
      },
      item_style: {
        width: '30em',
        height: '10em',
        border: '1px solid #ACACAC',
        borderRadius: '10px',
      },
      articleGridItem: {
        item_should_margin: false,
        item_class: '',//article_grid_item_margin or article_grid_item
        img_item_class: 'article_grid_item_img',
        img_item_src: '',
        img_item_width: '20px',
        img_item_height: '20px',
        item_title: '',
        item_title_class: 'article_grid_item_title',
        item_detail: '',
        item_detail_class: 'article_grid_item_detail'
      },
      categories: [],
      articleGridItems: [],
      iconSrc: ['../images/android.svg', '../images/algorithm.svg', '../images/html_css_js.svg',
        '../images/flutter.svg', '../images/ios.svg', '../images/spring_boot.svg']
    }
  },
  methods: {
    getAllCategories () {
      this.loading = true
      getRequest('/category/all').then(resp => {
        let jsonData = resp.data
        if (jsonData && jsonData.code == 0) {
          this.categories = jsonData.data
          let length = this.categories.length
          for (var index = 0; index < length; index++) {
            let itemData = this.categories[index]
            if (itemData && itemData.categoryDesc) {
              let shouldMargin = index != 0 && index != 3
              this.articleGridItem = {
                img_item_src: this.iconSrc[index],
                item_title: itemData.parentName,
                item_detail: itemData.categoryDesc,
                item_should_margin: shouldMargin,
                item_class: shouldMargin ? 'article_grid_item_margin' : 'article_grid_item'
              }
              let tempGridItem = this.articleGridItem
              this.articleGridItems.push(tempGridItem)
            }
          }
        } else {
          this.$message({
            type: 'error',
            message: jsonData.message
          })
        }
        this.loading = false
      })
    },
    gridItemClicked (itemData) {
      console.log('itemData is ' + itemData.item_detail)
    }
  },
  mounted () {
    this.getAllCategories()
  }
}
</script>

<style scoped>
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
