此模组添加了两个物品，分别是：
永恒烟花火箭；
终极永恒烟花火箭。

永恒烟花火箭的飞行时间使用原版烟花数据 `Fireworks.Flight` 实现，取值范围为 1-3，对应飞行 1、2、3 秒。
在 1.20.5 及以上该数据为 `minecraft:fireworks` 物品组件，1.20.5 以下为物品 NBT 的 `Fireworks.Flight`。
如名称所说的一样，这两种烟花火箭并不会被消耗，但是永恒烟花火箭增加了冷却时间，且不可堆叠。
冷却时间 = 飞行时间 - 偏移量（默认 0.5）。
永恒烟花火箭可在末地城宝箱中获得，掉落时飞行时间为 1、2、3 的概率分别由配置项决定；终极永恒烟花火箭获取方式见其资料页。所有永恒烟花火箭都在创造模式物品栏中不可见。
配置文件位于：/config/eternal_firework_rocket.json。
默认如下：
{
  "chanceFlight1": 0.1,
  "chanceFlight2": 0.01,
  "chanceFlight3": 0.001,
  "cooldownOffset": 0.5,
  "adminOnly": false
}
其中，"chanceFlight1"、"chanceFlight2"、"chanceFlight3" 分别对应了掉落物飞行时间为 1、2、3 的烟花火箭在宝箱中出现的概率（百分比）。
"cooldownOffset" 即计算冷却时间的偏移量。"adminOnly"，是否仅管理员可用。
