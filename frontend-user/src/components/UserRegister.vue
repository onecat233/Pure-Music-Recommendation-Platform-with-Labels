<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2 class="title">Register</h2>
      <el-form :model="form" label-width="100px">
        <!-- Username -->
        <el-form-item label="Username" :rules="usernameRules">
          <el-input v-model="form.username" placeholder="Enter your username" />
        </el-form-item>
  
        <!-- Password -->
        <el-form-item label="Password" :rules="passwordRules">
          <el-input v-model="form.password" type="password" placeholder="Enter your password" />
        </el-form-item>
  
        <!-- Email -->
        <el-form-item label="Email" :rules="emailRules">
          <el-input v-model="form.email" placeholder="Enter your email" />
        </el-form-item>
  
        <!-- Register Button -->
        <el-form-item>
          <el-button type="primary" @click="handleRegister" block>Register</el-button>
        </el-form-item>
      </el-form>
  
      <!-- Login redirect link -->
      <div class="login-link">
        <span>Already have an account? </span>
        <el-button type="text" @click="goToLogin">Please login!</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElForm, ElFormItem, ElInput, ElButton, ElMessage,ElCard } from 'element-plus'
import axios from 'axios'

// Form data
const form = ref({
  username: '',
  password: '',
  email: ''
})

// Validation rules
const usernameRules = [
  { required: true, message: 'Username is required', trigger: 'blur' }
]

const passwordRules = [
  { required: true, message: 'Password is required', trigger: 'blur' }
]

const emailRules = [
  { required: true, message: 'Email is required', trigger: 'blur' },
  { type: 'email', message: 'Please enter a valid email address', trigger: 'blur' }
]

// Register button action
const handleRegister = async () => {
  if (form.value.username && form.value.password && form.value.email) {
    try {
      const response = await axios.post('http://localhost:8888/user/register', new URLSearchParams({
        username: form.value.username,
        password: form.value.password,
        email: form.value.email
      }), {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      });
      if (response.data.code === 1) {
        router.push('/');
        ElMessage.success(response.data.msg);
      } else {
        ElMessage.error(response.data.msg);
      }
    } catch (error) {
      console.error('Registration failed:', error.message);
      ElMessage.error('Registration failed. Please try again later.');
    }
  } else {
    ElMessage.warning('Please fill in all fields');
  }
}
const router = useRouter()
const goToLogin = () => {
  router.push('/')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f4f4f5;
}

.register-card {
  width: 400px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
}

.login-link {
  text-align: center;
  margin-top: 20px;
}

.login-link span {
  margin-right: 8px;
}
</style>
