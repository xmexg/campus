<script setup>
import { onMounted } from 'vue'
import { useSession } from './store/session'

const { refreshUser } = useSession()

onMounted(() => {
  refreshUser()
})
</script>

<template>
  <router-view />
</template>
