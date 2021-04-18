<template>
  <el-container>
    <span>{{ itemName }}</span>
  </el-container>
</template>

<script>
export default {
  name: "TechDetail",
  data() {
    return {
      itemName: '',
    }
  },
  methods: {
    initItemName(){
      let pageParameters = this.$route.params
      this.itemName = pageParameters.item_name
      console.log('item_name=' + this.itemName)
    },
  },
  mounted() {
    this.initItemName()
  },
}
</script>

<style scoped>

</style>