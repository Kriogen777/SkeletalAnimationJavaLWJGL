package colladaEngine;

public class Source {
	String				m_ID;
	Float_Array			m_Float_Array;
	Name_Array			m_Name_Array;
	Technique_Common	m_Technique_Common;
	
	public Source() {
		m_ID = "";
		m_Float_Array = new Float_Array();
		m_Name_Array = new Name_Array();
		m_Technique_Common = new Technique_Common();
	}
}
