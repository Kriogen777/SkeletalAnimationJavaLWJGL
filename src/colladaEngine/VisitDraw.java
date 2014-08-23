package colladaEngine;

public class VisitDraw {
	private COLLADA collada;
	
	public void accept(COLLADA cl) {
		collada = cl;
		cl.getM_GeometryLibrary().visit(this);
	}

	public void accept(GeometryLibrary gl) {
		for(Geometry geo: gl.m_Geometries){
			geo.visit(this);
		}
	}

	public void accept(Geometry geometry) {
		geometry.m_Mesh.visit(this);
	}

	public void accept(Mesh mesh) {
		// uses vertices and triangles which use source
		
	}

}
