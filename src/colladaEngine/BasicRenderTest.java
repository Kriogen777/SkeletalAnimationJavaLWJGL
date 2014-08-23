package colladaEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;

import utility.BufferTools;
import utility.EulerCamera;
import utility.Model;
import utility.Model.Face;
import utility.OBJLoader;
import utility.ShaderLoader;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class BasicRenderTest {

	private static EulerCamera cam;
	private static int 
		shaderProgram, 
		vboVertexHandle, 
		vboNormalHandle;
	private static Model m;
	private static boolean running = true;
	private static final String VERTEX_SHADER_LOCATION = "src/colladaEngine/vLight.vs"; // vertex shader
    private static final String FRAGMENT_SHADER_LOCATION = "src/colladaEngine/fLight.fs"; // fragment shader
    
    public static void main(String []args){
    	setUpDisplay();
    	setUpVBOs();
    	setUpCamera();
    	setUpShaders();
    	setUpLighting();
    	
    	while(running){
    		render();
    		checkInput();
    		Display.update();
            Display.sync(60);
    	}
    	cleanUp();
        System.exit(0);
    }
    
    private static void cleanUp(){
    	glDeleteProgram(shaderProgram);
    	//VBO Destruction
    	glDeleteBuffers(vboVertexHandle);
    	glDeleteBuffers(vboNormalHandle);
    	    	
    	Display.destroy();
    }

	private static void checkInput() {
		if(Mouse.isGrabbed()){
    		cam.processMouse(1, 80, -80);
    	}
    	cam.processKeyboard(16,5,5,5);
    	
    	while(Keyboard.next()){
        	//full screen minecraft style
			if(Keyboard.isKeyDown(Keyboard.KEY_F11)){
				try{
					if(Display.isFullscreen()){
						Display.setFullscreen(false);
						Display.setDisplayMode(new DisplayMode(640,480));
					}else{
						Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
					}
					glViewport(0, 0, Display.getWidth(), Display.getHeight());
					
				}catch(LWJGLException e){
					e.printStackTrace();
				}
			}
			// quit
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				if(!Mouse.isGrabbed() || Display.isFullscreen()){
					running = false;
				}else{
					//let the mouse go, same as right click
					Mouse.setGrabbed(false);
				}
			}
    	}
    	
    	if(Mouse.isButtonDown(0)){
    		Mouse.setGrabbed(true);
    	} else if (Mouse.isButtonDown(1)) {
    		Mouse.setGrabbed(false);
    	}
    	
    	if(Display.isCloseRequested()){
    		running = false;
    	}
	}

	private static void render() {
		// clear and transform
		glLoadIdentity();
		cam.applyTranslations();
		
		// set up
		glLight(GL_LIGHT0, GL_POSITION, BufferTools.asFlippedFloatBuffer(cam.x(),cam.y(),cam.z(),1f));
		
		// drawing
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glDrawArrays(GL_TRIANGLES, 0, m.getFaces().size()*3); // mode, start index, count - no of vertices
	}

	private static void setUpLighting() {
		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, BufferTools.asFlippedFloatBuffer(new float [] {0.05f, 0.05f, 0.05f, 1f}));
		glLight(GL_LIGHT0, GL_POSITION,	BufferTools.asFlippedFloatBuffer(new float[]{0,0,0,1}));
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glMaterialf(GL_FRONT, GL_SHININESS, 120);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
		glColor3f(0.4f, 0.27f, 0.17f);
	}

	private static void setUpShaders() {
		shaderProgram = ShaderLoader.loadShaderPair(VERTEX_SHADER_LOCATION, FRAGMENT_SHADER_LOCATION);
		glUseProgram(shaderProgram);
	}

	private static void setUpCamera() {
		cam = new EulerCamera((float) Display.getWidth() / (float) Display.getHeight(), -2.19f, 1.36f,
                11.45f);
        cam.setFieldOfView(60);
        cam.applyPerspectiveMatrix();
	}

	private static void setUpVBOs() {
		int [] vbos;
		try{
			m = OBJLoader.loadModel(new File("src/lesson27/house.obj"));
			// swap this for a collada read then convert to model
			m = colladaToModel(new ColladaReader(new File("C:\\Users\\Kriogen\\Dropbox\\Programs\\Eclipse\\JGame\\src\\colladaEngine\\model\\astroBoy_walk_Max.dae")).cFile);
			vbos = OBJLoader.createVBO(m);
			vboVertexHandle = vbos[0];
			vboNormalHandle = vbos[1];
			glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
			glVertexPointer(3, GL_FLOAT, 0,0L);
			glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
			glNormalPointer(GL_FLOAT, 0,0L);
			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_NORMAL_ARRAY);
		}catch(FileNotFoundException e){
			e.printStackTrace();
			cleanUp();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			cleanUp();
			System.exit(1);
		}
	}

	private static void setUpDisplay() {
		try{
			Display.setDisplayMode(new DisplayMode(640,480));
			Display.setVSyncEnabled(true);
			Display.setTitle("COLLADA Demo");
			Display.create();
		}catch(LWJGLException e){
			System.err.println("Display unable to initialize properly.");
			Display.destroy();
			System.exit(1);
		}
	}
	
	private static Model colladaToModel(COLLADA c){
		
		return null;
	}
}

