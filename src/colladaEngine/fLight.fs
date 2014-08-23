// Per-vertex Phong lighting model

varying vec3 color;
uniform float diffuseIntensityModifier;

void main(){
	// Turns the varying color into a 4d color and stores it in the built-in output gl_FragColor
	gl_FragColor = vec4(color,1);
}