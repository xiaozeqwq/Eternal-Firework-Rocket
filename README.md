此模组添加了四个物品，分别是：
一级永恒烟花火箭；
二级永恒烟花火箭；
三级永恒烟花火箭；
终极永恒烟花火箭
飞行时间分别是：1、2、3、5。
如名称所说的一样，这四种烟花火箭并不会被消耗，但是一至三级增加了冷却时间，所有等级都不可堆叠。
冷却时间 = 飞行时间 - 偏移量（默认 0.5）。
一至三级可在末地城宝箱中获得，终极永恒烟花火箭获取方式见其资料页，所有等级永恒烟花火箭都在创造模式物品栏中不可见。
配置文件位于：/config/eternal_firework_rocket.json。
默认如下：
{
  "chanceFlight1": 0.1,
  "chanceFlight2": 0.01,
  "chanceFlight3": 0.001,
  "cooldownOffset": 0.5,
  "adminOnly": false
}
其中，"chanceFlight1"、"chanceFlight2"、"chanceFlight3" 分别对应了一、二、三等级的烟花火箭在宝箱中出现的概率（百分比）。
"cooldownOffset" 即计算冷却时间的偏移量。"adminOnly"，是否仅管理员可用。
