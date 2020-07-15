# 机场三维场景平台
## 项目来源
研究生期间课题内容，已完成大部分，后续会继续更新。
## 技术选型
- jme开源3d引擎
- xml解析（dom4j）
- java swing
- GLSL着色器语言
### 主要实现功能
- XPlane模型加载器
- 基于粒子系统的冰雹、雨、雪天气
- 动态天空（包括太阳移动、月相变化）
- OSM开源地图中机场路网解析
- 航空器轨迹处理（主要为地图匹配）
- 基于轨迹数据的动画生成
- 简单的小地图
- 多界面的机场三维场景漫游
### 项目结构
针对项目中文件夹及文件进行说明，部分未使用的会在后续补充说明或移除。
```
├── assets                // 资源文件夹（主要包括模型文件、图片、着色器）
│   ├── MatDefs  
│       ├── Clouds        // 云的着色器
│       ├── MiniMap       // 小地图的着色器
│       ├── Moon          // 月亮的着色器（月相变化）
│       ├── Sky           // 天空的基本着色器
│       ├── Stars         // 星星的着色器
│       ├── Sky.frag      // 天空的片元着色器
│       ├── Sky.j3md      // 天空的jme材质定义文件
│       ├── Sky.vert      // 天空的顶点着色器
│   ├── Models            // 飞机和地面车辆的模型
│       ├── aircraft      
│       ├── vehicles   
│   ├── Textures          // 纹理图片
│       ├── MiniMap       // 小地图的纹理图片
│       ├── Moon          // 月亮的纹理图片
│       ├── Noise         // 云层的噪声纹理图片
│       ├── Sky           // 静态天空的纹理图片（动态天空中不使用）
│       ├── Weather       // 冰雹、雨、雪的纹理图片
│   ├── XplaneModel       // 编写XPlane模型加载器使用的测试模型
│   ├── ZBAA              // XPlane北京南郊机场的场景文件（包括模型文件和图片等）
│   ├── ZBTJ              // XPlane天津滨海机场的场景文件（包括模型文件和图片等）
│   └── ZSSS              // XPlane上海虹桥机场的场景文件（包括模型文件和图片等）
├── bin                   
├── src                   // 源代码
├── track                 // 解码后的ADS-B轨迹数据
├── xml                   //
│   ├── AirportInfo.xml   //机场场景配置文件
│   ├── AirportInfo2.xml 
│   ├── roadPoint.xml     // 天津滨海机场路网数据
│   ├── roadTest.xml      // 天津滨海机场路网数据（测试）
│   ├── roadmap.xml       // 天津滨海机场路网数据
└── README.md
```
### 环境依赖
- jMonkeyEngine 3.2.4 stable<https://github.com/jMonkeyEngine/jmonkeyengine>
- jmathplot.jar<https://github.com/yannrichet/jmathplot>
- dom4j-2.1.1.jar<https://github.com/dom4j/dom4j>
