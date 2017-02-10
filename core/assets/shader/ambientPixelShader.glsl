#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 vColor;
varying vec2 vTexCoord;

//texture samplers
uniform sampler2D u_texture; //diffuse map

//additional parameters for the shader
uniform LOWP vec4 ambientColor;

void main() {
	vec4 diffuseColor = texture2D(u_texture, vTexCoord);
	vec3 ambient = ambientColor.rgb * ambientColor.a;
	
	vec3 final = vColor * diffuseColor.rgb * ambient;
	gl_FragColor = vec4(final, diffuseColor.a);
}