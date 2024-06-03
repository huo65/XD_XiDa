<script setup lang="ts">
import * as echarts from 'echarts';
import {Calendar, CaretBottom, CaretTop, Warning} from "@element-plus/icons-vue";
import {onMounted, ref} from 'vue'
import myAxios from "../../plugins/my-axios.ts";
import {dayjs} from "element-plus";
import {ElMessage} from 'element-plus'

const avatarUrl = ref('')
const username = ref('')
onMounted(async () => {
  const res = await myAxios.get("/user/admin/current");
  if (res.data.code !== 0) {
    ElMessage.error(res.data.description)
    sessionStorage.clear()
  }
  avatarUrl.value = res.data.data.avatarUrl
  username.value = res.data.data.username
  const pieChart = echarts.init(document.getElementById('pie'));
  pieChart.setOption(pieOption);
  const barChart = echarts.init(document.getElementById('weeklyUser'))
  barChart.setOption(weeklyUserOption)
  const lineChart = echarts.init(document.getElementById('estimateUser'))
  lineChart.setOption(lineOptions)
});
const timeValue = ref(dayjs().add(1, 'month').startOf('month'))
const weeklyUserOption = {
  title: {
    text: '每周用户活跃量',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: 50,
    right: 20,
    bottom: 20
  },
  xAxis: {
    type: 'category',
    data: [
      '周一',
      '周二',
      '周三',
      '周四',
      '周五',
      '周六',
      '周日'
    ],
    axisTick: {
      alignWithLabel: true
    }
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '活跃量',
      data: [13253, 34235, 26321, 12340, 24643, 1322, 1324],
      type: 'bar'
    }
  ]
}
const pieOption = {
  title: {
    text: '用户来源分析',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    data: [
      '直接访问',
      '邮件营销',
      '联盟广告',
      '视频广告',
      '搜索引擎'
    ]
  },
  series: [
    {
      name: '用户来源',
      type: 'pie',
      radius: '55%',
      center: ['50%', '60%'],
      data: [
        {
          value: 335,
          name: '直接访问'
        },
        {
          value: 310,
          name: '邮件营销'
        },
        {
          value: 234,
          name: '联盟广告'
        },
        {
          value: 135,
          name: '视频广告'
        },
        {
          value: 1548,
          name: '搜索引擎'
        }
      ]
    }
  ]
};
const lineOptions = {
  title: {
    text: '月度用户量统计',
    left: 'center'
  },
  xAxis: {
    data: [
      '一月',
      '二月',
      '三月',
      '四月',
      '五月',
      '六月',
      '七月',
      '八月',
      '九月',
      '十月',
      '十一月',
      '十二月'
    ],
    boundaryGap: false,
    axisTick: {
      show: false
    }
  },
  grid: {
    left: 20,
    right: 20,
    bottom: 20,
    top: 80,
    containLabel: true
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross'
    },
    padding: [5, 10]
  },
  yAxis: {
    axisTick: {
      show: false
    }
  },
  legend: {
    data: ['预计', '实际'],
    top: 50
  },
  series: [
    {
      name: '预计',
      smooth: true,
      type: 'line',
      data: [100, 120, 161, 134, 105, 160, 165, 114, 163, 185, 118, 123],
      animationDuration: 2800,
      animationEasing: 'cubicInOut'
    },
    {
      name: '实际',
      smooth: true,
      type: 'line',
      itemStyle: {},
      data: [120, 82, 91, 154, 162, 140, 145, 250, 134, 56, 99, 123],
      animationDuration: 2800,
      animationEasing: 'quadraticOut'
    }
  ]
}
</script>

<template>
  <el-card shadow="never">
    <el-row :gutter="20">
      <el-avatar
          :src="avatarUrl"
          style="width: 70px;height: 70px"
      />
      <span style="margin-left: 15px;line-height: 70px">早安，{{ username }}，祝你开心每一天!</span>
    </el-row>
  </el-card>
  <el-card shadow="never" style="margin-top: 20px">
    <el-row :gutter="20" justify="space-between">
      <el-col :span="6">
        <div class="statistic-card">
          <el-statistic :value="98500">
            <template #title>
              <div style="display: inline-flex; align-items: center">
                每日活跃用户
                <el-tooltip
                    effect="dark"
                    content="一天内登录的用户数"
                    placement="top"
                >
                  <el-icon style="margin-left: 4px" :size="12">
                    <Warning/>
                  </el-icon>
                </el-tooltip>
              </div>
            </template>
          </el-statistic>
          <div class="statistic-footer">
            <div class="footer-item">
              <span>比昨天</span>
              <span class="green">
              24%
              <el-icon>
                <CaretTop/>
              </el-icon>
            </span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="statistic-card">
          <el-statistic :value="693700">
            <template #title>
              <div style="display: inline-flex; align-items: center">
                每月活跃用户
                <el-tooltip
                    effect="dark"
                    content="一个月内登录的用户数"
                    placement="top"
                >
                  <el-icon style="margin-left: 4px" :size="12">
                    <Warning/>
                  </el-icon>
                </el-tooltip>
              </div>
            </template>
          </el-statistic>
          <div class="statistic-footer">
            <div class="footer-item">
              <span>月度环比</span>
              <span class="red">
              12%
              <el-icon>
                <CaretBottom/>
              </el-icon>
            </span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="statistic-card" style="text-align: center">
          <el-countdown format="DD [days] HH:mm:ss" :value="timeValue">
            <template #title>
              <div style="display: inline-flex; align-items: center">
                <el-icon style="margin-right: 4px" :size="12">
                  <Calendar/>
                </el-icon>
                距离下个月
              </div>
            </template>
          </el-countdown>
          <div class="countdown-footer">{{ timeValue.format('YYYY-MM-DD') }}</div>
        </div>
      </el-col>
    </el-row>
  </el-card>

  <el-row :gutter="20" style="margin-top: 20px">
    <el-col :span="8">
      <el-card shadow="never">
        <div id="pie" style="width: 100%;height: 300px"></div>
      </el-card>
    </el-col>
    <el-col :span="16">
      <el-card shadow="never">
        <div id="weeklyUser" style="width: 100%;height: 300px"></div>
      </el-card>
    </el-col>
  </el-row>
  <el-card shadow="never" style="margin-top: 20px">
    <div id="estimateUser" style="width: 100%;height: 300px"></div>
  </el-card>
</template>

<style scoped>
.statistic-card {
  height: 100%;
  padding: 20px;
  border-radius: 4px;
  background-color: var(--el-bg-color-overlay);
}

.statistic-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  font-size: 12px;
  color: var(--el-text-color-regular);
  margin-top: 16px;
}

.statistic-footer .footer-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.statistic-footer .footer-item span:last-child {
  display: inline-flex;
  align-items: center;
  margin-left: 4px;
}

.green {
  color: var(--el-color-success);
}

.red {
  color: var(--el-color-error);
}

.countdown-footer {
  margin-top: 8px;
  text-align: center;
}
</style>
