<template>
  <div>
    <!--  User Form -->
    <el-form :model="newUser" label-width="120px">
      <el-form-item label="Username">
        <el-input v-model="newUser.username" placeholder="Enter username" style="width: 200px"></el-input>
      </el-form-item>
      <el-form-item label="Password">
        <el-input v-model="newUser.password" placeholder="Enter password" type="password" style="width: 200px"></el-input>
      </el-form-item>
      <el-form-item label="User email">
        <el-input v-model="newUser.email" placeholder="Enter email" style="width: 300px"></el-input>
        <el-button type="primary" @click="addUser" style="margin-left: 20px">Add</el-button>
      </el-form-item>
    </el-form>

    <!-- User Table -->
    <el-table :data="users" border>
      <el-table-column prop="username" label="Username"></el-table-column>
      <el-table-column prop="email" label="Email"></el-table-column>
      <el-table-column label="Actions">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="openUpdateDialog(row)">Update</el-button>
          <el-button size="small" type="danger" @click="deleteUser(row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Update User Dialog -->
    <el-dialog title="Update User" v-model="isUpdateDialogVisible">
      <el-form :model="editUser" label-width="120px">
        <el-form-item label="Username">
          <el-input v-model="editUser.username"></el-input>
        </el-form-item>
        <el-form-item label="Password">
          <el-input v-model="editUser.password" type="password"></el-input>
        </el-form-item>
        <el-form-item label="Email">
          <el-input v-model="editUser.email"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="isUpdateDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="updateUser">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      users: [], // User list
      newUser: {
        username: "",
        password: "",
        email: "",
      },
      editUser: {}, // User being edited
      isUpdateDialogVisible: false, // Control update dialog visibility
    };
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await axios.get("http://localhost:8888/user/all");
        this.users = response.data;
      } catch (error) {
        this.$message.error("Failed to fetch users.");
      }
    },
    async addUser() {
      if (!this.newUser.username||!this.newUser.password||!this.newUser.email) {
        this.$message.error("Any item cannot be empty.");
        return;
      }
      try {
        await axios.post(
          "http://localhost:8888/user/register",
          new URLSearchParams(this.newUser),
          { headers: { "Content-Type": "application/x-www-form-urlencoded" } }
        );
        this.$message.success("User added successfully!");
        this.newUser = { username: "", password: "", email: "" };
        this.fetchUsers();
      } catch (error) {
        this.$message.error("Failed to add user.");
      }
    },
    openUpdateDialog(user) {
      this.editUser = { ...user }; // Clone the selected user data
      this.isUpdateDialogVisible = true;
    },
    async updateUser() {
      try {
        await axios.post(
          "http://localhost:8888/user/update",
          new URLSearchParams(this.editUser),
          { headers: { "Content-Type": "application/x-www-form-urlencoded" } }
        );
        this.$message.success("User updated successfully!");
        this.isUpdateDialogVisible = false;
        this.fetchUsers();
      } catch (error) {
        this.$message.error("Failed to update user.");
      }
    },
    async deleteUser(id) {
      try {
        await axios.post(
          "http://localhost:8888/user/delete",
          new URLSearchParams({ id }),
          { headers: { "Content-Type": "application/x-www-form-urlencoded" } }
        );
        this.$message.success("User deleted successfully!");
        this.fetchUsers();
      } catch (error) {
        this.$message.error("Failed to delete user.");
      }
    },
  },
  mounted() {
    this.fetchUsers();
  },
};
</script>
