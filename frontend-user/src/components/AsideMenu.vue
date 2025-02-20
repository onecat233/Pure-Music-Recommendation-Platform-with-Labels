<template>
  <el-col>
    <el-menu
      default-active="1"
      class="el-menu-vertical-demo"
      @select="handleSelect"
    >
      <el-menu-item index="1">
        <el-icon><icon-menu /></el-icon>
        <span>Find songs</span>
      </el-menu-item>
      <el-menu-item index="2">
        <el-icon><document /></el-icon>
        <span>Your favourites</span>
      </el-menu-item>
    </el-menu>
  </el-col>
</template>

<script>
export default {
  methods: {
    handleSelect(index) {
      this.$emit('menu-selected',index);
    },
  },
};
</script>
