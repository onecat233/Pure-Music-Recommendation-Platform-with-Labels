<template>
  <div>
    <!-- Add Tag Form -->
    <el-form :model="newTag" inline>
      <el-form-item label="Tag Description">
        <el-input v-model="newTag.description" placeholder="Enter tag description"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addTag">Add</el-button>
      </el-form-item>
    </el-form>

    <!-- Tag Table -->
    <el-table :data="tags" border style="margin-top: 20px;">
      <!-- Description Column -->
      <el-table-column prop="description" label="Description"></el-table-column>
      <!-- Actions Column -->
      <el-table-column label="Actions">
        <template #default="{ row }">
          <!-- Delete Button -->
          <el-button size="small" type="danger" @click="deleteTag(row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "TagMenu",
  data() {
    return {
      tags: [], // Store tag data
      newTag: {
        description: "", // Store new tag description
      },
    };
  },
  mounted() {
    this.initData();
  },
  methods: {
    async initData() {
      await this.fetchTags();
    },
    async fetchTags() {
      try {
        const response = await axios.get("http://localhost:8888/tag/all");
        this.tags = response.data; // Assign fetched tag data to tags
      } catch (error) {
        console.error("Error fetching tags:", error.message);
        alert("Failed to fetch tags. Check your backend server.");
      }
    },
    async addTag() {
      if (!this.newTag.description) {
        this.$message.error("Tag description cannot be empty.");
        return;
      }
      try {
        const response = await axios.post(
          "http://localhost:8888/tag/add",
          new URLSearchParams({
            description: this.newTag.description,
          }),
          { headers: { "Content-Type": "application/x-www-form-urlencoded" } } // Set Content-Type
        );
        if (response.data) {
          this.$message.success("Tag added successfully!");
          this.newTag.description = ""; // Clear input field
          this.fetchTags(); // Refresh tag list
        } else {
          this.$message.error("Failed to add tag.");
        }
      } catch (error) {
        console.error("Error adding tag:", error.response || error.message);
        this.$message.error("Error occurred while adding tag.");
      }
    },
    deleteTag(id) {
      console.log("Deleting tag with ID:", id); // Debugging output
      this.$confirm("Are you sure you want to delete this tag?", "Warning", {
        confirmButtonText: "Yes",
        cancelButtonText: "No",
        type: "warning",
      })
        .then(async () => {
          try {
            const response = await axios.post(
              "http://localhost:8888/tag/delete",
              new URLSearchParams({
                id: id,
              }),
              { headers: { "Content-Type": "application/x-www-form-urlencoded" } } // Set Content-Type
            );
            if (response.data) {
              this.$message.success("Tag deleted successfully!");
              this.fetchTags(); // Refresh tag list
            } else {
              this.$message.error("Failed to delete tag.");
            }
          } catch (error) {
            console.error("Error deleting tag:", error.response || error.message);
            this.$message.error("Error occurred while deleting tag.");
          }
        })
        .catch(() => {
          this.$message.info("Deletion cancelled.");
        });
    },
  },
};
</script>

<style scoped>
.el-form {
  margin-bottom: 20px;
}
.el-table {
  margin-top: 20px;
}
</style>
