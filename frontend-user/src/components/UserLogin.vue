<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="title">Login</h2>
      <el-form :model="form" label-width="100px">
        <el-form-item label="Username" :rules="usernameRules">
          <el-input v-model="form.username" placeholder="Enter your username" />
        </el-form-item>

        <el-form-item label="Password" :rules="passwordRules">
          <el-input v-model="form.password" type="password" placeholder="Enter your password" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleLogin" block>Login</el-button>
        </el-form-item>
      </el-form>

      <div class="register-link">
        <span>Don't have an account? </span>
        <el-button type="text" @click="goToRegister">Please register!</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElForm, ElFormItem, ElInput, ElButton, ElCard, ElMessage } from 'element-plus'
import axios from 'axios'

const form = ref({
  username: '',
  password: ''
})

const usernameRules = [
  { required: true, message: 'Username is required', trigger: 'blur' }
]

const passwordRules = [
  { required: true, message: 'Password is required', trigger: 'blur' }
]

const handleLogin = async () => {
  try {
    const response = await axios.post('http://localhost:8888/user/login', new URLSearchParams({
      username: form.value.username,
      password: form.value.password
    }), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    });

    if (response.data.code === 1) {
      localStorage.setItem('session', response.data.session);
      router.push('/home');
      ElMessage.success(response.data.msg);
    } else {
      ElMessage.error(response.data.msg);
    }
  } catch (error) {
    console.error('Login failed:', error.message);
    ElMessage.error('Login failed. Please try again later.');
  }
}

const router = useRouter()
const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f4f4f5;
}

.login-card {
  width: 400px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
}

.register-link {
  text-align: center;
  margin-top: 20px;
}

.register-link span {
  margin-right: 8px;
}
</style>
