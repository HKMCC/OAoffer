<template>
  <div id="app">
    <div class="sidebar">
      <card></card>
      <list></list>
    </div>
    <div class="main">
      <message></message>
      <userText></userText>
    </div>
  </div>
</template>

<script>
import card from '../../components/Chat/card'
import list from '../../components/Chat/list'
import message from '../../components/Chat/message'
import userText from '../../components/Chat/usertext'

export default {
  name: 'FriendChat',
  data () {
    return {

    }
  },
  mounted:function() {
    this.$store.dispatch('initData');
  },
  components:{
    card,
    list,
    message,
    userText
  }
}
</script>

<style lang="scss" scoped>
#app {
  margin: 20px 100px;
  //margin: 20px auto;
  width: 800px;
  height: 600px;
  overflow: hidden;
  border-radius: 10px;
  border: 1px solid #c8c9c9;
  .sidebar, .main {
    height: 100%;
  }
  .sidebar {
    float: left;
    color: #f4f4f4;
    background-color: #2e3238;
    width: 200px;
  }
  .main {
    position: relative;
    overflow: hidden;
    background-color: #eee;
  }
}
</style>
