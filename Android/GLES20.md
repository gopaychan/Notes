##### 1. 学习资料
1. [Android 官方OpenGl ES指南](https://developer.android.com/training/graphics/opengl/draw?hl=zh-cn)
2. [OpenGL学习网站](https://learnopengl-cn.github.io/)

##### 2. 基本概念
1. 顶点着色器（Vertex Shader）：顶点着色器是GPU上运行的小程序，由名字可以知道，通过它来处理顶点，他用于渲染图形顶点的OpenGL ES图形代码。顶点着色器可用来修改图形的位置，颜色，纹理坐标，不过不能用来创建新的顶点坐标。
2. 片段着色器（Fragment Shader )：用于呈现与颜色或纹理的形状的面的OpenGL ES代码。
3. 项目（Program）：包含要用于绘制一个或多个形状着色器的OpenGL ES的对象。
4. uniform变量：是外部程序传递给（vertex和fragment）shader的变量，因此它是application通过函数glUniform**（）函数赋值的。在（vertex和fragment）shader程序内部，uniform变量就像是C语言里面的常量（const ），它不能被shader程序修改。（shader只能用，不能改）。如果uniform变量在vertex和fragment两者之间声明方式完全一样，则它可以在vertex和fragment共享使用。（相当于一个被vertex和fragment shader共享的全局变量）
5. attribute变量是只能在vertex shader中使用的变量。（它不能在fragment shader中声明attribute变量，也不能被fragment shader中使用）。一般用attribute变量来表示一些顶点的数据，如：顶点坐标，法线，纹理坐标，顶点颜色等。
在application中，一般用函数glBindAttribLocation（）来绑定每个attribute变
6. varying变量是vertex和fragment shader之间做数据传递用的。一般vertex shader修改varying变量的值，然后fragment shader使用该varying变量的值。因此varying变量在vertex和fragment shader二者之间的声明必须是一致的。application不能使用此变量。
7. 如果用GLSL ES编写的着色器，浮点精确度规定如下：
    1. highp：32位浮点格式，适合用于顶点变换，但性能最慢。
    2. mediump：16位浮点格式，适用于纹理UV坐标和比highp 大约快两倍
    3. lowp：10位的顶点格式，适合对颜色，照明计算和其它高性能操作，速度大约是highp 的4倍
    ---
    4. 指定默认精度（放在Vertex和Fragment shader源码的开始处）：
        > precision mediump float;
    5. 指定变量精度（放在数据类型之前）：
        > highp vec4 position;  
8. GL_TRIANGLES：每三个顶之间绘制三角形，之间不连接；
9. GL_TRIANGLE_FAN：以V0V1V2,V0V2V3,V0V3V4，……的形式绘制三角形；
10. GL_TRIANGLE_STRIP：顺序在每三个顶点之间均绘制三角形。这个方法可以保证从相同的方向上所有三角形均被绘制。以V0V1V2,V1V2V3,V2V3V4……的形式绘制三角形；