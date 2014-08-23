package colladaEngine;

import java.util.ArrayList;

public class GeometryLibrary {
	ArrayList<Geometry> m_Geometries;
	
	public GeometryLibrary(){
		m_Geometries = new ArrayList<Geometry>(); 
	}
	
	public void visit(VisitDraw vd){
		vd.accept(this);
	}
}
