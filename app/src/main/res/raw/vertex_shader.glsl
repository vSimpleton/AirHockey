attribute vec4 a_Position;

// 定义一个4 * 4的矩阵
uniform mat4 u_Matrix;

void main() {
    gl_Position = u_Matrix * a_Position;
}
