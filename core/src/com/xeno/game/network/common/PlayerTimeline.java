package com.xeno.game.network.common;

public class PlayerTimeline extends Timeline<PlayerState>{

	// LinearInterpolation(long t);
	@Override
	public PlayerState Interpolate(long t)
    {
        var prev = ValueBefore(t);
        var next = ValueAfter(t);

        var duration = next.Time - prev.Time;
        var current = t - prev.Time;

        var result = new PlayerState(prev.Val);

        var diffX = next.Val.X - prev.Val.X;
        var newX = AnimationAlgorithms.Animate(AnimationType.Linear, current, prev.Val.X, diffX, duration);

        var diffY = next.Val.Y - prev.Val.Y;
        var newY = AnimationAlgorithms.Animate(AnimationType.Linear, current, prev.Val.Y, diffY, duration);

        result.X = newX;
        result.Y = newY;

        if (prev.Val.Attack > 0)
        {
            var attack = prev.Val.Attack + (int)current;

            if (attack > 250)
                attack = 0;

            result.Attack = attack;
        }

        return result;
    }

	// Fallback(long t);
	@Override
    public PlayerState Extrapolate(long t)
    {
        var prev = ValueBefore(t);

        return new PlayerState(prev.Val);
    }

}
