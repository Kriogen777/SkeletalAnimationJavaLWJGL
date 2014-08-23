package colladaEngine;

public class COLLADA {
	private ImageLibrary m_ImageLibrary = new ImageLibrary();
	private MaterialLibrary m_MaterialLibrary = new MaterialLibrary();
	private EffectLibrary m_EffectLibrary = new EffectLibrary();
	private GeometryLibrary m_GeometryLibrary = new GeometryLibrary();
	private VisualSceneLibrary m_VisualSceneLibrary = new VisualSceneLibrary();
	
	public COLLADA(){
		m_GeometryLibrary = new GeometryLibrary();
	}
	
	public ImageLibrary getM_ImageLibrary() {
		return m_ImageLibrary;
	}

	public void setM_ImageLibrary(ImageLibrary m_ImageLibrary) {
		this.m_ImageLibrary = m_ImageLibrary;
	}

	public MaterialLibrary getM_MaterialLibrary() {
		return m_MaterialLibrary;
	}

	public void setM_MaterialLibrary(MaterialLibrary m_MaterialLibrary) {
		this.m_MaterialLibrary = m_MaterialLibrary;
	}

	public EffectLibrary getM_EffectLibrary() {
		return m_EffectLibrary;
	}

	public void setM_EffectLibrary(EffectLibrary m_EffectLibrary) {
		this.m_EffectLibrary = m_EffectLibrary;
	}

	public GeometryLibrary getM_GeometryLibrary() {
		return m_GeometryLibrary;
	}

	public void setM_GeometryLibrary(GeometryLibrary m_GeometryLibrary) {
		this.m_GeometryLibrary = m_GeometryLibrary;
	}
	
	public VisualSceneLibrary getM_VisualSceneLibrary() {
		return m_VisualSceneLibrary;
	}

	public void setM_VisualSceneLibrary(VisualSceneLibrary m_VisualSceneLibrary) {
		this.m_VisualSceneLibrary = m_VisualSceneLibrary;
	}

	public void visit(VisitDraw vd){
		vd.accept(this);
	}
	
}
