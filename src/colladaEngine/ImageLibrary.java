package colladaEngine;

import java.util.ArrayList;

import org.jdom2.Attribute;

public class ImageLibrary {
	ArrayList<Image> m_Images = new ArrayList<Image>();
}

class Image
{
	String  m_Filename;
	String	m_Full_Path;
	String	m_Name;
}