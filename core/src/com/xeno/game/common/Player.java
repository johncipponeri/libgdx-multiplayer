package com.xeno.game.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.xeno.game.MainGame;
import com.xeno.game.network.common.PlayerInputState;
import com.xeno.game.network.common.PlayerState;
import com.xeno.game.network.common.PlayerTimeline;
import com.xeno.game.network.common.TimelineValue;
import com.xeno.game.util.SystemTime;

public class Player {

	public Vector2 position;
	private Rectangle bounds;
	private int id;
	
	// Timeline requirements
	public PlayerState CurrentState;
	private PlayerTimeline Timeline;
	private HashMap<Long, PlayerInputState> _queuedInput;
	public long SimulationDelay;
	//
	
	public boolean Ready()
    {
        return Timeline.Ready();
    }
	
	public Player(int x, int y, int id) {
		this.id = id;
		
		position = new Vector2(x, y);
		bounds = new Rectangle(position.x, position.y, 32, 32);
		
		// timeline stuff
		CurrentState = new PlayerState();
		Timeline = new PlayerTimeline();
		_queuedInput = new HashMap<Long, PlayerInputState>();
		SimulationDelay = 50;
	}
	
	public void update(float delta) {
		bounds.setPosition(position);
	}
	
	public void draw(SpriteBatch batch) {
		if (MainGame.DEBUGGING) {
			ShapeRenderer renderer = new ShapeRenderer();
			renderer.setProjectionMatrix(batch.getProjectionMatrix());
			renderer.setAutoShapeType(true);
			
			renderer.begin();
			renderer.rect(CurrentState.X, CurrentState.Y, bounds.width, bounds.height);
			renderer.end();
		}
	}
	
	public static Player getPlayerById(int id, ArrayList<Player> players) {
		Iterator<Player> iter = players.iterator();

		while (iter.hasNext()) {
		    Player p = iter.next();

		    if (p.getId() == id)
				return p;
		}
		
		return null;
	}
	
	public static Player getPlayerById(int id, CopyOnWriteArrayList<Player> players) {
		Iterator<Player> iter = players.iterator();

		while (iter.hasNext()) {
		    Player p = iter.next();

		    if (p.getId() == id)
				return p;
		}
		
		return null;
	}
	
	public void UpdateFrame(float delta)
    {
        if (!Timeline.Ready())
            return;

        //CurrentState = GetState(SystemTime.CurrentFrozenTimeMS() - SimulationDelay);
        CurrentState = GetState(SystemTime.CurrentFrozenTimeMS() - SimulationDelay);
    }
	
	public Vector2 GetPosition()
    {
        if (!Timeline.Ready())
            return new Vector2(0, 0);

        return new Vector2(CurrentState.X, CurrentState.Y);
    }

    public PlayerState GetState(long time)
    {
        if (!Timeline.Ready())
            return null;

        return Timeline.Get(time);
    }

    public PlayerState GetLatestState()
    {
        if (!Timeline.Ready())
            return null;

        return Timeline.GetLatest();
    }

    public TimelineValue<PlayerState> GetLatestStateValue()
    {
        if (!Timeline.Ready())
            return null;

        return Timeline.GetLatestValue();
    }

    public PlayerState GetLatestAuthoritiveState()
    {
        if (!Timeline.Ready())
            return null;

        return Timeline.GetLatestAuthoritive();
    }

    public void SetState(long time, PlayerState state, boolean authoritive)
    {
        Timeline.Set(time, state, authoritive);
    }

    public void UpdateState(PlayerInputState input, long time, boolean authoritive, boolean repeat)
    {
        PlayerState state = Timeline.Ready() ? new PlayerState(GetLatestState()) : new PlayerState();

        if (input.MovementPressed())
        {
            ProcessMovement(input, state);
        }
        else
        {
            state.Moving = false;
        }

        SetState(time, state, authoritive);
    }

    public void UpdateState(PlayerInputState input)
    {
        UpdateState(input, SystemTime.CurrentFrozenTimeMS(), true, false);
    }

    public void QueueInputPrediction(PlayerInputState input)
    {
        if (_queuedInput.containsKey(input.Identifier))
            _queuedInput.remove((input.Identifier));

        _queuedInput.put(new Long(input.Identifier), input);
        System.out.println("Put: " + input.Identifier + "; New total is " + _queuedInput.size());
    }

    public void ProcessQueuedInput()
    {
        Timeline.ClearNonAuthoritive();

        ArrayList<PlayerInputState> ordered = new ArrayList<PlayerInputState>();
        _queuedInput.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> ordered.add(x.getValue()));
        
        for (PlayerInputState input : ordered)
        {
            UpdateState(input, input.Identifier, false, true);
        }
    }

    public void ValidateInput(long identifier)
    {
        ArrayList<Long> keys = new ArrayList<>();
        
        if (_queuedInput.containsKey(identifier))
        {
            for (long id : _queuedInput.keySet())
            {
                if (id <= identifier)
                    keys.add(id);
            }

            for (long key : keys)
            {
                _queuedInput.remove(key);
            }
        }

        ProcessQueuedInput();
    }

    private void ProcessMovement(PlayerInputState input, PlayerState state)
    {
        state.Moving = true;

        Vector2 offset = new Vector2(0, 0);
        int velocity = 6;
        
        if(input.leftPressed)
        	offset.x -= velocity;
        else if (input.rightPressed)
        	offset.x += velocity;
        
        if(input.upPressed)
        	offset.y += velocity;
        else if (input.downPressed)
        	offset.y -= velocity;
        
        Vector2 modifiedOffset = CheckCollision(state, offset);

        state.X += modifiedOffset.x;
        state.Y += modifiedOffset.y;
        
        System.out.println("X: " + state.X + ", Y: " + state.Y);
    }

    private Vector2 CheckCollision(PlayerState state, Vector2 offset)
    {
        /*
        var playerRect = GetHitbox((int)(state.X + offset.X), (int)(state.Y + offset.Y));
         

        Map.DeselectHitboxes();

        foreach (var hitbox in Layer.Elevation.Hitboxes)
        {
            var hitboxRect = new FloatRect(hitbox.Left, hitbox.Top, hitbox.Width, hitbox.Height);

            if (playerRect.Intersects(hitboxRect))
            {
                Map.SelectHitbox(hitbox);
                return new Vector2f(0, 0);
            }
        }
        */

        return offset;
    }
	
	public void setPosition(int x, int y) {
		position.set(x, y);
	}

	public int getX() {
		return (int) position.x;
	}

	public void setX(int x) {
		this.position.x = x;
	}

	public int getY() {
		return (int) position.y;
	}

	public void setY(int y) {
		this.position.y = y;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}