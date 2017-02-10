#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 vColor;
varying vec2 vTexCoord;

//our texture samplers
uniform sampler2D u_texture; //diffuse map
uniform sampler2D u_lightmap;   //light map

//resolution of screen
uniform vec2 resolution; 

void main() {
	vec2 lighCoord = (gl_FragCoord.xy / resolution.xy);
	vec4 Light = texture2D(u_lightmap, lighCoord);
	
	gl_FragColor = vColor * Light;
}