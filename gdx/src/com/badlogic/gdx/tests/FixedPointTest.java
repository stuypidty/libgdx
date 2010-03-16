package com.badlogic.gdx.tests;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.RenderListener;
import com.badlogic.gdx.backends.desktop.JoglApplication;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

public class FixedPointTest implements RenderListener
{
	public static void main( String[] argv )
	{
		JoglApplication app = new JoglApplication( "Fixed Point Test", 480, 320, false);
		app.getGraphics().setRenderListener( new FixedPointTest() );
	}

	Texture tex;
	Texture tex2;
	IntBuffer vertices;
	final int BYTES_PER_VERTEX = (4+3+2+2)*4;	
	float angle = 0;
	float angleIncrement = 0.1f;
	
	@Override
	public void dispose(Application application) 
	{	
		
	}

	@Override
	public void render(Application app) 
	{			
		GL10 gl = app.getGraphics().getGL10();
		gl.glClearColor( 0.7f, 0.7f, 0.7f, 1 );
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glLoadIdentity();
		gl.glRotatef( angle, 0, 0, 1 );
		angle+=angleIncrement;
		gl.glEnable(GL10.GL_TEXTURE_2D);		
		
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);		
		vertices.position(0);
		gl.glColorPointer(4, GL10.GL_FIXED, BYTES_PER_VERTEX, vertices );
		
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glClientActiveTexture( GL10.GL_TEXTURE0 );
		gl.glActiveTexture( GL10.GL_TEXTURE0 );
		tex.bind();
		vertices.position(4);			
		gl.glTexCoordPointer( 2, GL10.GL_FIXED, BYTES_PER_VERTEX, vertices );
		
		gl.glClientActiveTexture( GL10.GL_TEXTURE1 );
		gl.glActiveTexture( GL10.GL_TEXTURE1 );
		tex2.bind();
		vertices.position(6);			
		gl.glTexCoordPointer( 2, GL10.GL_FIXED, BYTES_PER_VERTEX, vertices );
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		vertices.position(8);
		gl.glVertexPointer( 3, GL10.GL_FIXED, BYTES_PER_VERTEX, vertices );	
		
		gl.glDrawArrays( GL10.GL_TRIANGLES, 0, 3);				
	}

	@Override
	public void surfaceCreated(Application application) 
	{
		ByteBuffer buffer = ByteBuffer.allocateDirect( BYTES_PER_VERTEX * 3 );
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asIntBuffer();					
				
		int[] verts = { 							
				  fp(1), fp(0), fp(0), fp(1),
				  fp(0), fp(1),
				  fp(0), fp(1),
				  fp(-0.5f), fp(-0.5f), fp(0),
				  
				  fp(0), fp(1), fp(0), fp(1),
				  fp(1), fp(1),
				  fp(1), fp(1),
				  fp(0.5f), fp(-0.5f), fp(0),
				   
				  fp(0), fp(0), fp(1), fp(1),
				  fp(0.5f), fp(0),
				  fp(0.5f), fp(0),
				  fp(0), fp(0.5f), fp(0),
				 };
		vertices.put(verts);
		vertices.flip();	
		
		Pixmap pixmap = application.getGraphics().newPixmap(256, 256, Format.RGBA8888 );
		pixmap.setColor(1, 1, 1, 1 );
		pixmap.fill();
		pixmap.setColor(0, 0, 0, 1 );
		pixmap.drawLine(0, 0, 256, 256);
		pixmap.drawLine(256, 0, 0, 256);		
		tex = application.getGraphics().newTexture( pixmap, TextureFilter.MipMap, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge, false );
		
		pixmap = application.getGraphics().newPixmap( 256, 256, Format.RGBA8888 );
		pixmap.setColor( 1, 1, 1, 1 );
		pixmap.fill();
		pixmap.setColor( 0, 0, 0, 1 );
		pixmap.drawLine( 128, 0, 128, 256 );
		tex2 = application.getGraphics().newTexture( pixmap, TextureFilter.MipMap, TextureFilter.Linear, TextureWrap.ClampToEdge, TextureWrap.ClampToEdge, false );
		
		application.getGraphics().getGL10().glDisable( GL10.GL_TEXTURE_2D );
	}

	@Override
	public void surfaceChanged(Application app, int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	private static int fp( float value )
	{
		return (int)(value * 65536);
	}
}
