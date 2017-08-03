package com.xeno.game.network.common;

public class PlayerTimeline extends Timeline<PlayerState>{
	
	public PlayerTimeline() {
		super();
	}
	
	// float currentTime, float startValue, float valueChange, float duration)
	public static float LinearTween(float t, float b, float c, float d)
    {
        return c * t / d + b;
    }

	// LinearInterpolation(long t);
	@Override
	public PlayerState Interpolate(long t)
    {
		TimelineValue<PlayerState> prev = ValueBefore(t);
        TimelineValue<PlayerState> next = ValueAfter(t);

        long duration = next.Time - prev.Time;
        long current = t - prev.Time;

        PlayerState result = new PlayerState(prev.Val);

        float diffX = next.Val.X - prev.Val.X;
        float newX = LinearTween(current, prev.Val.X, diffX, duration);

        float diffY = next.Val.Y - prev.Val.Y;
        float newY = LinearTween(current, prev.Val.Y, diffY, duration);

        result.X = newX;
        result.Y = newY;

        return result;
    }

	// Fallback(long t);
	@Override
    public PlayerState Extrapolate(long t)
    {
        TimelineValue<PlayerState> prev = ValueBefore(t);

        return new PlayerState(prev.Val);
    }

}
