package colladaEngine;

public class Geometry {
	Mesh m_Mesh;
	String id;
	String name;
	
	public Geometry(){
		m_Mesh = new Mesh();
	}
	
	public void visit(VisitDraw vd){
		vd.accept(this);
	}
}
