<template>
  <div class="common-layout">
    <el-container class="full-height">
      <el-header class="header">
        <vheader />
      </el-header>

      <el-container>
        <el-aside width="200px" class="aside">
          <asidemenu @menu-selected="handleMenuSelect" />
        </el-aside>
        <el-container class="content-area">
          <el-main class="main">
            <component :is="currentComponent" @handlePlaySong="handlePlaySong"/>
          </el-main>
          <el-footer v-if="showFooterMenu" class="footer">
            <FooterMenu ref="footerMenu" />
          </el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import AsideMenu from '@/components/AsideMenu';
import HeaderMenu from '@/components/HeaderMenu';
import FooterMenu from '@/components/FooterMenu';
import SongTable from '@/components/SongTable.vue';
import TagTable from '@/components/TagTable.vue';
import UserTable from '@/components/UserTable.vue';

export default {
  name: 'HomePage',
  components: {
    asidemenu: AsideMenu,
    vheader: HeaderMenu,
    SongTable,
    TagTable,
    UserTable,
    FooterMenu,
  },
  data() {
    return {
      currentComponent: 'UserTable',
      showFooterMenu: false,
    };
  },
  methods: {
    handleMenuSelect(menuIndex) {
      switch (menuIndex) {
        case '1':
          this.currentComponent = 'UserTable';
          this.showFooterMenu = false;
          break;
        case '2':
          this.currentComponent = 'SongTable';
          this.showFooterMenu = true;
          break;
        case '3':
          this.currentComponent = 'TagTable';
          this.showFooterMenu = false;
          break;
        default:
          this.currentComponent = 'UserTable';
          this.showFooterMenu = false;
      }
    },
    handlePlaySong(id) {
      this.$refs.footerMenu.playSongById(id); 
    },
  },
};
</script>

<style scoped>
.full-height {
  height: 100%;
}

.header {
  background-color: #f5f5f5;
  height: 60px;
  text-align: center;
}

.aside {
  background-color: #eef1f6;
  height: calc(100vh - 60px);
  overflow-y: auto;
  border-right: 1px solid #ddd;
}

.content-area {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.main {
  flex: 1;
  padding: 20px;
  background-color: #fff;
  overflow-y: auto;
}

.footer {
  background-color: #f5f5f5;
  text-align: center;
}
</style>
