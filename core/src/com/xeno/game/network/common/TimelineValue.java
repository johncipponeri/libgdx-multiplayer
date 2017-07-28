package com.xeno.game.network.common;

public class TimelineValue<TValueType> {
	
	public long Time;

    public boolean Authoritive;

    public TValueType Val;

    public TimelineValue<TValueType> Next;
    public TimelineValue<TValueType> Prev;
    
    public TimelineValue(TValueType value, long time, boolean authoritive) {
		this.Val = value;
		this.Time = time;
		this.Authoritive = authoritive;
	}
}
