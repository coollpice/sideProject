<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";
import axios from "axios";

const router = useRouter();

const posts = ref([]);

const getPosts = function () {
  axios.get("/api/posts?page=1&size=10", ).then((response) => {
    response.data.forEach( (post: any) => {
        posts.value.push(post)
    });
  });
};

getPosts();

</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div>
        <router-link :to="{name : 'read', params : {postId : post.id }}">{{post.title}}</router-link>   <!--  index.ts 에 등록된 view 이름 추가 -->
      </div>
      <div>
        {{post.content}}
      </div>
    </li>
  </ul>

</template>

<style scoped>
li {
  margin-bottom: 1rem;
}

li:last-child {
  margin-bottom: 0;
}
</style>