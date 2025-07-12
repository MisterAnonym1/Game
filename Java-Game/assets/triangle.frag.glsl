#version 100

// FRAG_SHADER
#ifdef GL_ES
precision mediump float;
#endif

uniform float u_time;
void main() {
    float pulse = 0.5 + 0.5 * sin(u_time + gl_FragCoord.x * 0.05);
    vec3 color = mix(vec3(0.0, 0.4, 1.0), vec3(0.5, 0.9, 1.0), pulse);
    gl_FragColor = vec4(color, 0.15 + 0.15 * pulse);
}