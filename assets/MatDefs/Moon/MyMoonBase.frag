
uniform float m_MoonPhase;
varying vec2 texCoord;

uniform sampler2D m_MoonTex;
uniform sampler2D m_Noise;

// uniform float m_Phase;
uniform float m_Phase;

void main()
{

    float u_maskRadius=0.60;  // ���ְ뾶
    float u_moonRadius=0.45;  // �����뾶
    vec2 u_maskCenter=vec2(0.4,0.5);// ��������
    
    //����������ֵ�ƶ����֣�ʵ������仯
    if(m_Phase<3.5)
    {
      u_maskCenter.x=0.4-m_Phase/3.5;
    }else{
      u_maskCenter.x=1.6-(m_Phase-3.5)/3.5;
     }   
      
    //�õ㵽����Բ�ĵľ���
    float x2_mask = (texCoord.x - u_maskCenter.x) * (texCoord.x - u_maskCenter.x);
    float y2_mask = (texCoord.y - u_maskCenter.y) * (texCoord.y - u_maskCenter.y);
    
    //�õ㵽����Բ�ĵľ���
    float x2_center = (texCoord.x - 0.5) * (texCoord.x - 0.5);
    float y2_center = (texCoord.y - 0.5) * (texCoord.y - 0.5);
    
    //���ֺ�����ʵ��İ뾶��ע�⣬���ֵİ뾶���������εİ뾶����ȵġ�
    float r2_moon = u_moonRadius * u_moonRadius;
    float r2_mask = u_maskRadius * u_maskRadius;
    
    //������ʵ�崦�����ص�������ʵ��������������
    if (x2_mask + y2_mask >= r2_mask && x2_center + y2_center < r2_moon)
    {
        gl_FragColor =  texture2D(m_MoonTex, texCoord);
    }//�����Σ����������Ĺ������������ֵĹ����� 
    else if(x2_mask + y2_mask >= r2_moon && x2_center + y2_center < r2_mask)
    {
        float ratio;
        float r1 = 1.0- (sqrt(x2_center + y2_center) - u_moonRadius) / (u_maskRadius - u_moonRadius);//��������
        float r2 = 1.0- (u_maskRadius - sqrt(x2_mask + y2_mask)) / (u_maskRadius - u_moonRadius);//���ֹ���
        if (x2_mask + y2_mask >= r2_mask && x2_center + y2_center >= r2_moon)
            ratio = r1;
        else if (x2_center + y2_center < r2_moon) 
            ratio = r2;
        else
            ratio = r1 * r2; //�½ǣ�����ط��Ƚ������

        vec4 color = texture2D(m_MoonTex, vec2(0.5, 0.5));//��ɫ����Ϊ����ͼƬ�м��
        color.a = pow(ratio, 3.0);//͸���ȱ仯����
        gl_FragColor = color*0.8;
    }
    else//�����ط�Ϊ����
    {
        gl_FragColor = vec4(0.0);
    }
}


