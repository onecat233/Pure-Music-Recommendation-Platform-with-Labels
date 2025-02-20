<template>
    <el-row class="header-container" justify="space-between" align="middle">
      <el-col>
        <div class="head-wrap">
          Pure Music Recommendation Platform Management
          <el-dropdown @command="handleCommand">
          <div class="avatar-container">
            <img :src="require('@/assets/logo.png')" alt="User Avatar" class="avatar" />
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">Logout</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        </div>
      </el-col>
    </el-row>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router'
  import axios from 'axios';
  import { ElMessage } from 'element-plus'
  
  const router = useRouter()
  
  const handleCommand = async (command) => {
    if (command === 'logout') {
      try {
        const response = await axios.post('http://localhost:8888/admin/logout')
        if (response.data.code === 1) {
          ElMessage.success(response.data.msg)
          localStorage.removeItem('session')
          router.push('/')
        } else {
          ElMessage.error(response.data.msg || 'Logout failed')
        }
      } catch (error) {
        console.error('Logout error:', error)
        ElMessage.error('Network error. Please try again.')
      }
    }
  }

  window.addEventListener('beforeunload', () => {
    localStorage.removeItem('session')
  });
  </script>
  
  <style scoped>
  .header-container {
    background-color: #f0f8ff;
    padding: 10px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .head-wrap {
    display: flex;
    align-items: center;
    font-size: 20px;
    font-weight: bold;
    color: #1e90ff;
  }
  
  .avatar-container {
    width: 50px;
    height: 50px;
    display: flex;
    margin-left: 1000px;
    cursor: pointer;
  }
  
  .avatar {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    object-fit: cover;
    border: 2px solid #1e90ff;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  </style>
  