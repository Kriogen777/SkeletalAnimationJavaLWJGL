package colladaEngine;

public class Technique {
	String sid;
	String name;
	// asset
	// image
	// newparam
	// constant
	// lambert
	Phong m_Phong = new Phong(); // acts as blinn as well
}

class Phong{
	// In all of these ignore the texture/param children - too hard for now
	float [] emission; // found in its color child
	float [] ambient; // found in its color child
	float [] diffuse; // found in its color child - will likely use an OpenGL default
	float [] specular; // found in its color child
	float shininess; // found in its text value
	float [] reflective; // found in its color child
	float reflectivity; // found in its text value
	float [] transparent; // found in its color child
	String transparent_Opaque; // found in its opaque attribute
	float transparency;  // found in its text value
	float indexOfRefraction;  // found in its text value
}