package colladaEngine;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class ColladaReader {
	COLLADA cFile = new COLLADA();
	
	
	/**
	 * @param loadFile
	 */
	public ColladaReader(File loadFile){
		load(loadFile);
	}
	
	public COLLADA load(File loadFile) {
		try {
			SAXBuilder reader = new SAXBuilder();
			Document document = reader.build(loadFile);
			
			Element root = document.getRootElement();
			
			// <firstname>yong</firstname> -> node.getChildText("firstname") -> yong
			// <block x="0" y="4" type="AIR" /> -> node.getAttributeValue("x") -> 0
			
			for (Element library : root.getChildren()) {
				// Images
				if(library.getName().equals("library_images")){
					for(Element images: library.getChildren()){
						if(images.getName().equals("image")){
							Image im = new Image();
							im.m_Filename = images.getAttributeValue("id");
							im.m_Name = images.getAttributeValue("name");
							im.m_Full_Path = images.getChild("init_from",null).getText();
							cFile.getM_ImageLibrary().m_Images.add(im);
						}
					}
				}
				
				// Materials
				if(library.getName().equals("library_materials")){
					for(Element mats: library.getChildren()){
						if(mats.getName().equals("material")){
							Material mat = new Material();
							mat.m_id = mats.getAttributeValue("id");
							mat.m_name = mats.getAttributeValue("name");
							mat.m_url = mats.getChild("instance_effect",null).getAttributeValue("url");
							cFile.getM_MaterialLibrary().m_Materials.add(mat);
						}
					}
				}
				
				// Effects
				if(library.getName().equals("library_effects")){
					for(Element efct: library.getChildren()){
						if(efct.getName().equals("effect")){
							Effect tempEffect = new Effect();
							tempEffect.id = efct.getAttributeValue("id");
							tempEffect.name = efct.getAttributeValue("name");
							Element tempPCommon = efct.getChild("profile_COMMON",null);
							if(tempPCommon!=null){
								Element tempTech = tempPCommon.getChild("technique",null);
								if(tempTech != null){
									tempEffect.m_Profile_COMMON.m_Technique.sid = tempTech.getAttributeValue("sid");
									Element tempPhong = tempTech.getChild("phong",null);
									tempPhong = tempPhong==null? efct.getChild("blin",null):tempPhong;
									if(tempPhong!=null){
										for(Element child: tempPhong.getChildren()){
											if(child.getName().equals("emission")){
												tempEffect.m_Profile_COMMON.m_Technique.m_Phong.emission = stringToFloatArray(child.getChildText("color",null));
											}
											if(child.getName().equals("ambient")){
												tempEffect.m_Profile_COMMON.m_Technique.m_Phong.ambient = stringToFloatArray(child.getChildText("color",null));
											}
											if(child.getName().equals("diffuse")){
												tempEffect.m_Profile_COMMON.m_Technique.m_Phong.diffuse = stringToFloatArray(child.getChildText("color",null));
											}
											if(child.getName().equals("specular")){
												tempEffect.m_Profile_COMMON.m_Technique.m_Phong.specular = stringToFloatArray(child.getChildText("color",null));
											}
											if(child.getName().equals("shininess")){
												tempEffect.m_Profile_COMMON.m_Technique.m_Phong.shininess = Float.parseFloat(child.getChildText("float",null));
											}
											if(child.getName().equals("reflective")){
												tempEffect.m_Profile_COMMON.m_Technique.m_Phong.reflective = stringToFloatArray(child.getChildText("color",null));
											}
											if(child.getName().equals("reflectivity")){
												tempEffect.m_Profile_COMMON.m_Technique.m_Phong.reflectivity = Float.parseFloat(child.getChildText("float",null));
											}
											if(child.getName().equals("transparent")){
												tempEffect.m_Profile_COMMON.m_Technique.m_Phong.transparent = stringToFloatArray(child.getChildText("color",null));
												tempEffect.m_Profile_COMMON.m_Technique.m_Phong.transparent_Opaque = child.getAttributeValue("opaque");
											}
											if(child.getName().equals("transparency")){
												tempEffect.m_Profile_COMMON.m_Technique.m_Phong.transparency = Float.parseFloat(child.getChildText("float",null));
											}
											if(child.getName().equals("index_of_refraction")){
												tempEffect.m_Profile_COMMON.m_Technique.m_Phong.indexOfRefraction = Float.parseFloat(child.getChildText("float",null));
											}
										}
										
										tempEffect.m_Profile_COMMON.m_Technique.m_Phong.ambient = null;
									}
									else{
										System.err.println("No phong or blin found.");
									}
								}
								else{
									System.err.println("No technique found.");
								}
							}
							else{
								System.err.println("No profile_COMMON found.");
							}
						}
					}
					// Add to library
				}
				
				// Geometries
				//System.out.println(library.getName());
				if(library.getName().equals("library_geometries")){
					// Set up geometry
					Geometry tempGeom = new Geometry();
					
					Element geom = library.getChild("geometry", null);
					tempGeom.id = geom.getAttributeValue("id");
					tempGeom.name = geom.getAttributeValue("name");
					
					
					// Get and loop through mesh
					Element mesh = geom.getChild("mesh",null);
					for(Element meshElement: mesh.getChildren()){
						if(meshElement.getName().equals("source")){
							// Source element handling
							Source tempSource = new Source();
							tempSource.m_ID = meshElement.getAttributeValue("id");
							
							Element valArray = meshElement.getChild("float_array",null);
							if(valArray != null){
								tempSource.m_Float_Array.m_ID = valArray.getAttributeValue("id");
								tempSource.m_Float_Array.m_Count = Integer.parseInt(valArray.getAttributeValue("count"));
								String [] arrayValues = valArray.getText().split(" ");
								for(String s: arrayValues){
									tempSource.m_Float_Array.m_Floats.add(Float.parseFloat(s));
									//System.out.println(s);
								}
							}else{
								valArray = meshElement.getChild("NAME_array",null);
								tempSource.m_Name_Array.m_ID = valArray.getAttributeValue("id");
								tempSource.m_Name_Array.m_Count = Integer.parseInt(valArray.getAttributeValue("count"));
								String [] arrayValues = valArray.getText().split(" ");
								tempSource.m_Name_Array.m_Names.addAll(Arrays.asList(arrayValues));
							}
							
							Element tc = meshElement.getChild("technique_common",null);
							Technique_Common tempTechnique = new Technique_Common();
							Element accessor = tc.getChild("accessor",null);
							tempTechnique.m_Accessor.m_Source = accessor.getAttributeValue("source");
							tempTechnique.m_Accessor.m_Count = Integer.parseInt(accessor.getAttributeValue("count"));
							tempTechnique.m_Accessor.m_Stride = Integer.parseInt(accessor.getAttributeValue("stride"));
							for(Element param: accessor.getChildren()){
								Param tempParam = new Param();
								tempParam.m_Name = param.getAttributeValue("name");
								tempParam.m_Type = param.getAttributeValue("type");
								tempTechnique.m_Accessor.m_Params.add(tempParam);
							}
							
							//add the tech
							tempSource.m_Technique_Common = tempTechnique;
							// add the source to the mesh
							tempGeom.m_Mesh.m_Sources.add(tempSource);
						}
						if(meshElement.getName().equals("vertices")){
							// Vertices element handling
							tempGeom.m_Mesh.m_Vertices.m_ID = meshElement.getAttributeValue("id");
							Input tempInput = new Input();
							Element input = meshElement.getChild("input",null);
							tempInput.m_Semantic = input.getAttributeValue("semantic");
							tempInput.m_Source = input.getAttributeValue("source");
							tempGeom.m_Mesh.m_Vertices.m_Inputs.add(tempInput);
						}
						if(meshElement.getName().equals("triangles")){
							// Triangles element handling
							Triangles tempTriangles = new Triangles();
							tempTriangles.m_Count = Integer.parseInt(meshElement.getAttributeValue("count"));
							tempTriangles.m_Material = meshElement.getAttributeValue("material"); // From library_materials
							
							for(Element input: meshElement.getChildren("input")){
								Input tempInput = new Input();
								tempInput.m_Semantic = input.getAttributeValue("semantic");
								tempInput.m_Source = input.getAttributeValue("source");
								tempInput.m_Offset = input.getAttributeValue("offset");
								tempInput.m_Set = input.getAttributeValue("set");
								tempTriangles.m_Inputs.add(tempInput);
							}
							Element p = meshElement.getChild("p",null);
							String [] indices = p.getText().split(" ");
							for(String s: indices){
								tempTriangles.m_P.m_Indices.add(Integer.parseInt(s));
							}
							tempGeom.m_Mesh.m_Triangles.add(tempTriangles);
						}
					}
					// add the geometry object
					cFile.getM_GeometryLibrary().m_Geometries.add(tempGeom);
				}
				
				// Visual Scenes - Skeleton
				if(library.getName().equals("library_visual_scenes")){
					Element visualS = library.getChild("visual_scene",null);
					boolean foundRootName = false;
					boolean foundRoot = false;
					for(Element node: visualS.getChildren("node",null)){
						if(node.getChild("instance_controller",null) != null){
							cFile.getM_VisualSceneLibrary().m_RootNode.id = node.getChild("instance_controller",null).getChildText("skeleton",null).replace("#", ""); // Root node ID
							foundRootName = true;
						}
						if(foundRootName){ // Let's build up the skeleton
							// look for the root with one layer deep search
							Element childNode = node.getChild("node",null);
							if(node.getAttributeValue("id").equals("cFile.getM_VisualSceneLibrary().m_RootNode.id")){
								addChildNode(cFile.getM_VisualSceneLibrary().m_RootNode, node);
								foundRoot = true;
							}
							else if(childNode != null && childNode.getAttributeValue("id").equals(cFile.getM_VisualSceneLibrary().m_RootNode.id)){
								addChildNode(cFile.getM_VisualSceneLibrary().m_RootNode, childNode);
								foundRoot = true;
							}
						}
					}
					if(!foundRootName){
						System.err.println("Can't find root name");
					}
					if(!foundRoot){
						System.err.println("Can't find root node: " + cFile.getM_VisualSceneLibrary().m_RootNode.id);
					}
				} // end visual scene
				
				// Reading Skinning Info
				if(library.getName().equals("library_controllers")){
					// According to our assumptions we only have one mesh and one skeleton. 
					// So we will have only one <controller> node in the list of children of <library_controllers>.
					Element controllerElement = library.getChild("controller",null);
					Element skinElement = controllerElement.getChild("skin",null);
					
				} // end skinning info
				
				// Reading Animation Info
				
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cFile;
	}
	
	private float [] stringToFloatArray(String source){
		if(source == null)return null;
		
		String [] temp = source.split(" ");
		float [] output = new float[temp.length];
		for(int i=0;i<temp.length;i++){
			output[i] = Float.parseFloat(temp[i]);
		}
		return output;
	}
	
	private void addChildNode(Node parent, Element node){
		// add this node to the parent
		parent.id = node.getAttributeValue("id");
		// System.out.println("Added child " + parent.id);
		parent.name = node.getAttributeValue("name");
		parent.SID = node.getAttributeValue("sid");
		parent.matrix = stringToFloatArray(node.getChildText("matrix",null));
		
		// for all element node children of node call this message
		// create a new node add it to parent's children and make their parent = parent
		for(Element child: node.getChildren("node", null)){
			Node childNode = new Node(parent);
			addChildNode(childNode, child);
			parent.children.add(childNode);
		}
	}
}
