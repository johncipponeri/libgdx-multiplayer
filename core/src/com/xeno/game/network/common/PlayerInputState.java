package com.xeno.game.network.common;

public class PlayerInputState {
	
    public long Identifier;

    public boolean upPressed;
    public boolean rightPressed;
    public boolean downPressed;
    public boolean leftPressed;
    
	public PlayerInputState() {
		
	}
    
    public boolean MovementPressed() {
        return (upPressed || rightPressed || downPressed || leftPressed);
    }
	
    // TODO:
	public PlayerInputState readFromPacket() {
		return null;
	}
	
	// TODO:
	public void writeToPacket() {
		
	}
}
