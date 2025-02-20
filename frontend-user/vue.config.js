const { defineConfig } = require('@vue/cli-service');
const webpack = require('webpack');

module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        '__VUE_PROD_HYDRATION_MISMATCH_DETAILS__': JSON.stringify(true), // 启用该功能标志
      }),
    ],
  },
  devServer: {
    proxy: {
      '/song': {
        target: 'http://localhost:8888', // 后端地址
        changeOrigin: true
      },
    },
  },
});

