package colladaEngine;

import java.util.ArrayList;

public class Node {
	Node Parent;
	ArrayList<Node> children = new ArrayList<Node>();
	String id;
	String name;
	String SID;
	float [] matrix = new float[16];
	
	public Node(Node Parent){
		this.Parent = Parent;
	}
}
