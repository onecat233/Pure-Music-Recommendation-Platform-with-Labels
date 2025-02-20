<template>
    <el-input
        v-model="query"
        placeholder="Input name to search"
        @keyup.enter="search"
        style="width: 400px;margin-top: 15px"
    >
      <template #append>
        <el-button @click="search">search</el-button>
      </template>
    </el-input>
  </template>
  
  <script>
  export default {
    name: 'SearchBar',
    data() {
      return {
        query: '',
      };
    },
    methods: {
      search() {
        this.$emit('search', this.query);
      },
    },
  };
  </script>
  