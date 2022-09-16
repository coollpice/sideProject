<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter();

const title = ref("")
const content = ref("")


/**
 * cors 해결을 위해 /api 를 앞에 붙여준다. vite.config.ts 에서 proxy 를 사용함.
 */
const write = function () {
  axios.post("/api/posts", {
    title: title.value,
    content: content.value
  }).then(() => {
    router.replace({name : "home"})
  });
};

</script>

<template>

  <div class="mt-1">
    <el-input v-model="title" placeholder="제목을 입력하세요."/>
  </div>

  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15"/>
  </div>

  <div class="mt-2">
    <el-button type="primary" @click="write()">글작성</el-button>
  </div>

</template>

<style>

</style>