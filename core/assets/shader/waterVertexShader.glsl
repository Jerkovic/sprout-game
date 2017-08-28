attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
uniform vec2 waveData; //allows to make waaaves
varying vec4 vColor;
varying vec2 vTexCoord;
 
void main() {
    vColor = a_color;
    vTexCoord = a_texCoord0;
    vec4 newPos = vec4(a_position.x + waveData.y * sin(waveData.x+a_position.x+a_position.y), a_position.y + waveData.y * cos(waveData.x+a_position.x+a_position.y), a_position.z, a_position.w);
    gl_Position = u_projTrans * newPos;
}