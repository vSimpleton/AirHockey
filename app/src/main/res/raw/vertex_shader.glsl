attribute vec4 a_Position;
attribute vec4 a_Color;

varying vec4 v_Color;

// 定义一个4 * 4的矩阵
uniform mat4 u_Matrix;

void main() {
    gl_Position = u_Matrix * a_Position;
    gl_PointSize = 10.0;

    v_Color = a_Color;
}
