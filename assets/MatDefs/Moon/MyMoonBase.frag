
uniform float m_MoonPhase;
varying vec2 texCoord;

uniform sampler2D m_MoonTex;
uniform sampler2D m_Noise;

// uniform float m_Phase;
uniform float m_Phase;

void main()
{

    float u_maskRadius=0.60;  // 遮罩半径
    float u_moonRadius=0.45;  // 月亮半径
    vec2 u_maskCenter=vec2(0.4,0.5);// 遮罩中心
    
    //根据月相数值移动遮罩，实现月相变化
    if(m_Phase<3.5)
    {
      u_maskCenter.x=0.4-m_Phase/3.5;
    }else{
      u_maskCenter.x=1.6-(m_Phase-3.5)/3.5;
     }   
      
    //该点到遮罩圆心的距离
    float x2_mask = (texCoord.x - u_maskCenter.x) * (texCoord.x - u_maskCenter.x);
    float y2_mask = (texCoord.y - u_maskCenter.y) * (texCoord.y - u_maskCenter.y);
    
    //该点到月亮圆心的距离
    float x2_center = (texCoord.x - 0.5) * (texCoord.x - 0.5);
    float y2_center = (texCoord.y - 0.5) * (texCoord.y - 0.5);
    
    //遮罩和月亮实体的半径，注意，遮罩的半径和月亮光晕的半径是相等的。
    float r2_moon = u_moonRadius * u_moonRadius;
    float r2_mask = u_maskRadius * u_maskRadius;
    
    //（月亮实体处理）像素点在月亮实体内且在遮罩外
    if (x2_mask + y2_mask >= r2_mask && x2_center + y2_center < r2_moon)
    {
        gl_FragColor =  texture2D(m_MoonTex, texCoord);
    }//（光晕）点在月亮的光晕中且在遮罩的光晕中 
    else if(x2_mask + y2_mask >= r2_moon && x2_center + y2_center < r2_mask)
    {
        float ratio;
        float r1 = 1.0- (sqrt(x2_center + y2_center) - u_moonRadius) / (u_maskRadius - u_moonRadius);//月亮光晕
        float r2 = 1.0- (u_maskRadius - sqrt(x2_mask + y2_mask)) / (u_maskRadius - u_moonRadius);//遮罩光晕
        if (x2_mask + y2_mask >= r2_mask && x2_center + y2_center >= r2_moon)
            ratio = r1;
        else if (x2_center + y2_center < r2_moon) 
            ratio = r2;
        else
            ratio = r1 * r2; //月角，这个地方比较难理解

        vec4 color = texture2D(m_MoonTex, vec2(0.5, 0.5));//颜色定义为月亮图片中间的
        color.a = pow(ratio, 3.0);//透明度变化更快
        gl_FragColor = color*0.8;
    }
    else//其他地方为背景
    {
        gl_FragColor = vec4(0.0);
    }
}


