<template>
  <div class="message">
    <el-popover placement="bottom" :width="310" trigger="click">
      <template #reference>
        <i class="iconfont icon-xiaoxi toolBar-icon"></i>
      </template>
      <el-tabs v-model="activeName">
        <el-tab-pane label="通知(0)" name="first">
          <div class="message-empty">
            <img src="@/assets/images/notData.png" alt="notData" />
            <div>暂无通知</div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="消息(0)" name="second">
          <div class="message-empty">
            <img src="@/assets/images/notData.png" alt="notData" />
            <div>暂无消息</div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="待办(0)" name="third">
          <div class="message-empty">
            <img src="@/assets/images/notData.png" alt="notData" />
            <div>暂无待办</div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-popover>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
const activeName = ref("first");
</script>

<style scoped lang="scss">
.message-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 260px;
  line-height: 45px;
}
</style>
