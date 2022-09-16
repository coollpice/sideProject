<script setup lang="ts">
import axios from "axios";
import {defineProps, onMounted, ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true
  },
});

const post = ref({
  id : 0,
  title : "",
  content : ""
});

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((response) => {
      post.value = response.data;
  });
});

const moveToEdit = () => {
  router.push({name : "edit", params : { postId : props.postId}});
};

</script>

<template>
  <h2>{{post.title}}</h2>
  <div>{{post.content}}</div>
  <div><el-button type="warning" @click="moveToEdit()">수정</el-button></div>
</template>

<style>

</style>