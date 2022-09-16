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

const deletePosts = () => {
  if(confirm("삭제하시겠습니까?")){
    axios.delete(`/api/posts/${props.postId}`).then(() => {
      router.replace({name : "home"})
    });
  }
};

</script>

<template>
  <h2>{{post.title}}</h2>
  <div>{{post.content}}</div>
  <div>
    <el-button type="warning" @click="moveToEdit()">수정</el-button>
    <el-button type="danger" @click="deletePosts()">삭제</el-button>
  </div>
</template>

<style>

</style>