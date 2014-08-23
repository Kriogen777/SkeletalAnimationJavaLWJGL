package colladaEngine;

import java.util.ArrayList;

public class Mesh {
	ArrayList<Source> 		m_Sources;
	Vertices				m_Vertices;
	ArrayList<Triangles>	m_Triangles;
	
	public Mesh() {
		m_Sources = new ArrayList<Source>();
		m_Vertices = new Vertices();
		m_Triangles = new ArrayList<Triangles>();
	}
	
	public void visit(VisitDraw vd){
		vd.accept(this);
	}
}
