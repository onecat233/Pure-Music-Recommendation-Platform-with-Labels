<template>
  <div>
    <el-form class="form-row" :inline="true">
      <el-form-item>
        <SearchBar @search="handleSearch" class="searchbar" />
      </el-form-item>
      <el-form-item>
        <TagFilter @filterByTag="handleFilterByTag" />
      </el-form-item>
      <el-form-item />
    </el-form>
    <el-table :data="songs" border>
      <el-table-column prop="name" label="Song Name"></el-table-column>
      <el-table-column prop="tagDescriptions" label="Tags"></el-table-column>
      <el-table-column label="Actions">
        <template #default="{ row }">
          <el-button size="small" @click="handlePlay(row)">Play</el-button>
          <el-button
            size="small"
            :class="{ favorite: row.favorite }"
            @click="handleFavorite(row)"
          >
            <i :class="row.favorite ? 'fa fa-heart' : 'fa fa-heart-o'"></i>
          </el-button>
          <el-button size="small" @click="startAddingTag(row)">
            Tag
          </el-button>
          <el-select
            v-if="currentEditingSong === row.id"
            v-model="selectedTag"
            placeholder="Select tag"
            @change="handleTagChange(row)"
            class="tag-select"
          >
            <el-option
              v-for="tag in allTags"
              :key="tag.id"
              :label="tag.description"
              :value="tag.id"
            ></el-option>
          </el-select>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import axios from 'axios';
import TagFilter from '@/components/TagFilter.vue';
import SearchBar from '@/components/SearchBar.vue';

export default {
  name: 'SongTable',
  components: { SearchBar, TagFilter },
  data() {
    return {
      songs: [],
      allTags: [],
      currentEditingSong: null,
      selectedTag: null,
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
        
        const favResponse = await axios.get('http://localhost:8888/favour/detail', {
          headers: { 'Content-Type': 'application/json' },
        });

        const favSongs = new Set(favResponse.data.map(fav => fav.songid));

        const response = await axios.get('/song/all', {
          headers: { 'Content-Type': 'application/json' },
        });

        const songs = response.data.map((song) => ({
          ...song,
          url: decodeURIComponent(song.url),
          tagDescriptions: '',
          favorite: favSongs.has(song.id),
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

    startAddingTag(row) {
      this.currentEditingSong = row.id;
      this.selectedTag = null;
    },

    async handleTagChange(song) {
      try {
        const songId = song.id;
        const tagId = this.selectedTag;

        const typeResponse = await axios.post(
          'http://localhost:8888/type/song',
          new URLSearchParams({ songId }),
          { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
        );

        const existingTags = typeResponse.data.map((type) => type.tagid);
        if (existingTags.includes(tagId)) {

          await axios.post(
            'http://localhost:8888/type/update',
            new URLSearchParams({ songId, tagId }),
            { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
          );
        } else {
          await axios.post(
            'http://localhost:8888/type/add',
            new URLSearchParams({ songId, tagId }),
            { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
          );
        }
        song.tagDescriptions = await this.fetchTypesForSong(songId);
        this.currentEditingSong = null;
        this.selectedTag = null;
      } catch (error) {
        console.error('Error updating tag:', error.message);
        alert('Failed to update tag. Check your backend server.');
      }
    },

    async handleFilterByTag(tagId) {
  try {
    const response = await axios.post(
      '/type/songoftag',
      new URLSearchParams({ tagId }),
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      }
    );

    const songDetailsPromises = response.data.map(async (song) => {
      const songResponse = await axios.post(
        '/song/detail',
        new URLSearchParams({ id: song.songid }),
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        }
      );
      const songDetail = songResponse.data[0];

      const tagDescriptions = await this.fetchTypesForSong(song.songid);

      return {
        ...songDetail,
        tagDescriptions,
      };
    });
    this.songs = await Promise.all(songDetailsPromises);
  } catch (error) {
    console.error('Error filtering songs by tag:', error.message);
    alert('Filter by tag failed. Check your backend server.');
  }
},

async fetchTypesForSong(songId) {
  try {
    const typeResponse = await axios.post('/type/song', new URLSearchParams({ songId }), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });

    const types = typeResponse.data;
    if (!types || types.length === 0) {
      return 'No tags';
    }
    types.sort((a, b) => b.agreenum - a.agreenum);
    const topTags = types.slice(0, 3);
    const tagDescriptions = topTags.map((type) => {
      const tag = this.allTags.find(tag => tag.id === type.tagid);
      return tag ? tag.description : 'Unknown';
    });

    return tagDescriptions.join(', ');
  } catch (error) {
    console.error(`Error fetching types for song ${songId}:`, error.message);
    return 'Error loading tags';
  }
},

    async handleSearch(query) {
  try {
    const response = await axios.post(
      '/song/search',
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


    handlePlay(song) {
  this.$emit('handlePlaySong', song.id);  // Ensure to trigger handlePlaySong in the parent component with the song ID
},

async handleFavorite(song) {
  try {
    if (song.favorite) {
      const response = await axios.post('http://localhost:8888/favour/delete', new URLSearchParams({ songId: song.id }), {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      });
      if (response.data.code === 1) {
        this.$set(song, 'favorite', false);
      }
    } else {
      const response = await axios.post('http://localhost:8888/favour/add', new URLSearchParams({ songId: song.id }), {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      });
      if (response.data.code === 1) {
        this.$set(song, 'favorite', true);
      }
    }
  } catch (error) {
    console.error('Error handling favorite:', error.message);
  }
},

  },
};
</script>

<style scoped>
.form-row {
  display: flex;
  align-items: center;
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
.favorite i {
  color: #ff6961 !important;
}
.favorite.el-button:focus {
  outline: none;
  box-shadow: none;
}
.tag-select {
  margin-left: 15px;
  width: 200px;
}
</style>
