<script setup lang="ts">
  import {ref} from "vue";

  const count = ref(0);
</script>

<template>
  <p>테스트</p>
  <button @click="count += 1">누르세요</button>
  <p>{{count}}</p>
</template>

<style>

</style>