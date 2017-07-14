package com.xeno.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	private static boolean loaded = false;
	
	public static void dispose() {
		return;
	}
	
	public static Texture loadTexture(String filename) {
        return new Texture(Gdx.files.internal(filename));
	}
        
    public static TextureRegion[] loadFrames(String sheetName, int cols, int rows) {
        Texture sheet = loadTexture(sheetName);
        TextureRegion[][] sheetFrames = TextureRegion.split(sheet, sheet.getWidth() / cols, sheet.getHeight() / rows);
        TextureRegion[] animFrames = new TextureRegion[cols * rows];
 
        int index = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                animFrames[index++] = sheetFrames[r][c];
 
        return animFrames;
    }
 
    public static Animation<TextureRegion> loadAnimation(float fps, TextureRegion[] frames) {
        return new Animation<TextureRegion>(1 / fps, frames);
    }
    
    public static Music loadMusic(String filename) {
    	return Gdx.audio.newMusic(Gdx.files.internal(filename));
    }
    
    public static Sound loadSound(String filename) {
    	return Gdx.audio.newSound(Gdx.files.internal(filename));
    }
}
