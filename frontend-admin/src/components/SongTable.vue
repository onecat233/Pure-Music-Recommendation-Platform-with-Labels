<template>
  <div>
    <el-form class="form-row" :inline="true">
      <el-form-item>
        <SearchBar @search="handleSearch" class="searchbar" />
      </el-form-item>

    </el-form>

    <!-- New Add Song Form -->
    <el-form ref="addSongForm" :model="newSong" label-width="120px" class="form-row">
      <el-form-item label="Song Name">
        <el-input v-model="newSong.name" placeholder="Enter song name"></el-input>
      </el-form-item>
      <el-form-item label="Song File">
        <el-upload
          ref="songUpload"
          :before-upload="validateFileType"
          :on-change="handleAddFileChange"
          :auto-upload="false"
        >
          <el-button type="primary">Upload MP3</el-button>
        </el-upload>
        <el-button type="success" @click="addSong" style="margin-left: 40px;margin-bottom: 10px;">Add Song</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="songs" border>
      <el-table-column prop="name" label="Song Name"></el-table-column>
      <el-table-column prop="tagDescriptions" label="Tags"></el-table-column>
      <el-table-column label="Actions">
        <template #default="{ row }">
          <el-button size="small" @click="handlePlay(row)">Play</el-button>
          <el-button size="small" type="primary" @click="openUpdateDialog(row)">Update</el-button>
          <el-button size="small" type="danger" @click="deleteSong(row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="Update Song" v-model="isUpdateDialogVisible">
      <el-form :model="editSong" label-width="120px">
        <el-form-item label="Song Name">
          <el-input v-model="editSong.name"></el-input>
        </el-form-item>
        <el-form-item label="Song File">
          <el-upload
            ref="upload"
            :before-upload="validateFileType"
            :on-change="handleUpdateFileChange"
            :auto-upload="false"
          >
            <el-button type="primary">Choose File</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="isUpdateDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="updateSong">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import SearchBar from '@/components/SearchBar.vue';

export default {
  name: 'SongTable',
  components: { SearchBar},
  data() {
    return {
      songs: [],
      allTags: [],
      editSong: {
        name: '',
        id: null,
      },
      newSong: {
        name: '',
        file: null,
      },
      uploadFile: null,
      isUpdateDialogVisible: false,
    };
  },
  mounted() {
    this.initData();
  },
  methods: {
    async initData() {
      await this.fetchTags();
      await this.fetchSongs();
    },
    async fetchTags() {
      try {
        const response = await axios.get('http://localhost:8888/tag/all');
        this.allTags = response.data;
      } catch (error) {
        console.error('Error fetching tags:', error.message);
        alert('Failed to fetch tags. Check your backend server.');
      }
    },
    async fetchSongs() {
      try {
        const response = await axios.get('http://localhost:8888/song/all', {
          headers: { 'Content-Type': 'application/json' },
        });
        const songs = response.data.map((song) => ({
          ...song,
          url: decodeURIComponent(song.url),
          tagDescriptions: '',
        }));
        for (const song of songs) {
          song.tagDescriptions = await this.fetchTypesForSong(song.id);
        }
        this.songs = songs;
      } catch (error) {
        console.error('Error fetching songs:', error.message);
        alert('Failed to fetch songs. Check your backend server.');
      }
    },
    async fetchTypesForSong(songId) {
      try {
        const typeResponse = await axios.post('http://localhost:8888/type/song', new URLSearchParams({ songId }), {
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        });
        const types = typeResponse.data;
        if (!types || types.length === 0) {
          return 'No tags';
        }
        types.sort((a, b) => b.agreenum - a.agreenum);
        const tagDescriptions = types.map((type) => {
          const tag = this.allTags.find((tag) => tag.id === type.tagid);
          return tag ? tag.description : 'Unknown';
        });
        return tagDescriptions.join(', ');
      } catch (error) {
        console.error(`Error fetching types for song ${songId}:`, error.message);
        return 'Error loading tags';
      }
    },
    validateFileType(file) {
      const isMP3 = file.type === 'audio/mpeg';
      if (!isMP3) {
        this.$message.error('Only MP3 files are allowed!');
      }
      return isMP3;
    },
    handleAddFileChange(file) {
      this.newSong.file = file.raw;
      console.log("Add file selected:", this.newSong.file);
    },
    handleUpdateFileChange(file) {
      this.editSong.uploadFile = file.raw;
      console.log("Update file selected:", this.editSong.uploadFile);
    },
    async addSong() {
      if (!this.newSong.file) {
        this.$message.error('Please upload a file before adding a song.');
        return;
      }
      const formData = new FormData();
      formData.append('name', this.newSong.name);
      formData.append('file', this.newSong.file);
      try {
        const response = await axios.post('http://localhost:8888/song/add', formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        });
        if (response.data.code === 1) {
          this.$message.success(response.data.msg);
          this.newSong.name = '';
          this.newSong.file = null;
          this.$refs.songUpload.clearFiles();
          this.fetchSongs();
        } else {
          this.$message.error(response.data.msg);
        }
      } catch (error) {
        this.$message.error('Failed to add song.');
        console.error('Error adding song:', error.message);
      }
    },
    async updateSong() {
      const formData = new FormData();
      formData.append('id', this.editSong.id);
      formData.append('name', this.editSong.name);
      if (this.editSong.uploadFile) {
        formData.append('file', this.editSong.uploadFile);
      }
      console.log(this.editSong.name);
      console.log(this.editSong.uploadFile);
      try {
        const response = await axios.post('http://localhost:8888/song/update', formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        });
        if (response.data.code === 1) {
          this.isUpdateDialogVisible = false;
          this.fetchSongs();
          this.$message.success('Song updated successfully!');
        } else {
          this.isUpdateDialogVisible = false;
          this.fetchSongs();
        }
      } catch (error) {
        this.isUpdateDialogVisible = false;
        this.fetchSongs();
        console.error('Error adding song:', error.message);
      }
    },
    async deleteSong(id) {
      try {
        await axios.post(
          'http://localhost:8888/song/delete',
          new URLSearchParams({ id }),
          { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
        );
        this.$message.success('Song deleted successfully!');
        this.fetchSongs();
      } catch (error) {
        this.$message.error('Failed to delete song.');
      }
    },
    openUpdateDialog(song) {
      this.editSong = { name: song.name, id: song.id };
      this.uploadFile = null;
      this.isUpdateDialogVisible = true;
    },

async handleSearch(query) {
  try {
    const response = await axios.post(
      'http://localhost:8888/song/search',
      new URLSearchParams({ keyword: query }),
      {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      }
    );

    const songs = response.data.map((song) => ({
      ...song,
      url: decodeURIComponent(song.url),
      tagDescriptions: '',
    }));
    for (const song of songs) {
      song.tagDescriptions = await this.fetchTypesForSong(song.id);
    }

    this.songs = songs;
  } catch (error) {
    console.error('Error searching songs:', error.message);
    alert('Search failed. Check your backend server.');
  }
},
  },
};
</script>

<style scoped>
.form-row {
  display: flex;
  margin-bottom: 20px;
}
.el-form-item {
  flex: 1;
  margin-right: 10px;
}
.el-form-item:last-child {
  margin-right: 0;
}
.searchbar {
  height: 40px;
  margin-bottom: 20px;
}
</style>
