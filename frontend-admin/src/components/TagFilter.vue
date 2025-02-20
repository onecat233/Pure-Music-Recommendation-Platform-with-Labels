<template>
    <div>
      <!-- Add a label to indicate the purpose of the select box -->
      <label for="tag-select" style="font-size: 16px; font-weight: bold; margin-right: 10px;">
        Select a Tag:
      </label>
      <el-select
          v-model="selectedTag"
          placeholder="Select a Tag"
          @change="onTagChange"
          id="tag-select"
          style="width: 200px;margin-bottom: 10px;"
      >
        <el-option
            v-for="tag in tags"
            :key="tag.id"
            :label="tag.description"
            :value="tag.id"
        />
      </el-select>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    name: 'TagFilter',
    emits: ['filterByTag'],  // Declare the event in the 'emits' option
    data() {
      return {
        tags: [],
        selectedTag: null,
      };
    },
    mounted() {
      this.fetchTags();
    },
    methods: {
      async fetchTags() {
        try {
          const response = await axios.get('http://localhost:8888/tag/all');
          this.tags = response.data;
        } catch (error) {
          console.error('Error fetching tags:', error.message);
        }
      },
      onTagChange() {
        if (this.selectedTag) {
          this.$emit('filterByTag', this.selectedTag); // Emit the event when the tag is selected
        }
      },
    },
  };
  </script>
  
  <style scoped>
  /* Optional styling to make the label and select look better */
  label {
    font-size: 16px;
    font-weight: bold;
  }
  </style>
  
  
  