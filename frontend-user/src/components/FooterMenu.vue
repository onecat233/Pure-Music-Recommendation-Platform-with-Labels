<template>
  <div class="footer-container">
    <div class="song-info">
      <span class="song-name">{{ currentSong?.name || 'No song playing' }}</span>
    </div>

    <div class="controls">
      <button @click="togglePlayPause" class="icon-button">
        <i :class="isPlaying ? 'fa fa-pause' : 'fa fa-play'" />
      </button>
    </div>

    <div class="progress-bar-container">
      <el-progress
        :percentage="progress"
        status="text"
        style="flex-grow: 1;"
      ></el-progress>
    </div>

    <audio ref="audioPlayer" @timeupdate="updateProgress" @ended="songEnded"></audio>
  </div>
</template>

<script>
import axios from "axios";
import mitt from "mitt";

export default {
  name: "FooterMenu",
  data() {
    return {
      currentSong: null,
      progress: 0,
      isPlaying: false,
      audioPlayer: null,
      emitter: mitt(),
    };
  },
  mounted() {
    this.audioPlayer = this.$refs.audioPlayer;
    this.emitter.on("playById", this.playSongById);
  },
  methods: {
    async playSongById(songId) {
      try {
        const response = await axios.post(
          "http://localhost:8888/song/detail",
          new URLSearchParams({ id: songId }),
          {
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
          }
        );
        const song = response.data[0];
        if (song && song.url) {
          this.playSong(song);
        } else {
          alert("Song not found or invalid data.");
        }
      } catch (error) {
        console.error("Error fetching song details:", error.message);
        alert("Failed to fetch song details. Check your backend server.");
      }
    },
    playSong(song) {
      this.currentSong = song;
      this.progress = 0;
      this.isPlaying = true;

      const fullUrl = `http://localhost:8888${song.url}`;
      this.audioPlayer.src = fullUrl;
      this.audioPlayer.play();
    },
    togglePlayPause() {
      if (this.isPlaying) {
        this.audioPlayer.pause();
        this.isPlaying = false;
      } else {
        this.audioPlayer
          .play()
          .then(() => {
            this.isPlaying = true;
          })
          .catch((error) => {
            console.error("Error during play:", error.message);
          });
      }
    },
    updateProgress() {
      if (this.audioPlayer.duration) {
        this.progress = (this.audioPlayer.currentTime / this.audioPlayer.duration) * 100;
      }
    },
    songEnded() {
      this.progress = 100;
      this.currentSong = null;
      this.isPlaying = false;
    },
  },
};
</script>

<style scoped>
.footer-container {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  background-color: #f4f4f5;
  border-top: 1px solid #ddd;
}

.song-info {
  flex-shrink: 0;
  margin-right: 20px;
  font-size: 16px;
  font-weight: bold;
}

.song-name {
  color: #007BFF;
}

.progress-bar-container {
  flex-grow: 1;
}

.controls {
  margin-right: 20px;
}

.icon-button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 20px;
  color: #007BFF;
}

.icon-button:hover {
  color: #0056b3;
}
</style>
