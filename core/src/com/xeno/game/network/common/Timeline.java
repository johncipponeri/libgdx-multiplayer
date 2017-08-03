package com.xeno.game.network.common;

import java.util.concurrent.locks.ReentrantLock;

public abstract class Timeline<TValueType> implements ITimeline<TValueType> {
	

    private TimelineValue<TValueType> _firstValue;
    private TimelineValue<TValueType> _lastValue;

    private ReentrantLock _lock;

    private long _maxTimelineLength = 1000;


    public Timeline()
    {
        _firstValue = null;
        _lastValue = null;
        _lock = new ReentrantLock();
    }
    
    public boolean Ready()
    {
        return _firstValue != null;
    }

    public int ValueCount()
    {
        int count = 0;
        TimelineValue<TValueType> v = _firstValue;

        while (v != null)
        {
            v = v.Next;
            count++;
        }

        return count;
    }

    public long getMaxTimelineLength()
    {
       return _maxTimelineLength;
    }
    
    public void setMaxTimelineLength(long value)
    {
        _maxTimelineLength = value;
    }

    protected TimelineValue<TValueType> ValueAt(long t)
    {
    	TimelineValue<TValueType> v = _lastValue;
        while (v != null)
        {
            if (v.Time == t)
                return v;

            if (v.Time < t)
                return null;

            v = v.Prev;
        }

        return v;
    }

    protected TimelineValue<TValueType> ValueBefore(long t)
    {
    	TimelineValue<TValueType> latestValueBefore = _lastValue;
        while (latestValueBefore != null)
        {
            if (latestValueBefore.Time < t)
                return latestValueBefore;

            latestValueBefore = latestValueBefore.Prev;
        }

        return _firstValue;
    }

    protected TimelineValue<TValueType> ValueAfter(long t)
    {
    	TimelineValue<TValueType> earliestTimeAfter = _lastValue;
        while (earliestTimeAfter != null)
        {
            if (earliestTimeAfter.Time < t)
                return earliestTimeAfter.Next;

            earliestTimeAfter = earliestTimeAfter.Prev;
        }

        return _firstValue;
    }

    public TValueType Get(long t)
    {
        TValueType result;

        _lock.lock();
        try {
        	TimelineValue<TValueType> v = ValueAt(t);

            if (v != null)
            {
                result = v.Val;
            }
            else
            {
                if (_firstValue.Time < t && _lastValue.Time > t)
                {
                    result = Interpolate(t);
                }
                else if (_firstValue.Time > t)
                {
                    result = _firstValue.Val;
                }
                else
                {
                	result = Extrapolate(t);
                }
            }
        } finally {
        	_lock.unlock();
        }

        return result;
    }

    public TValueType GetLatest()
    {
        return _lastValue.Val;
    }

    public TimelineValue<TValueType> GetLatestValue()
    {
        return _lastValue;
    }

    public TValueType GetLatestAuthoritive()
    {
    	TimelineValue<TValueType> result = _lastValue;
    	
        while (!result.Authoritive)
        {
            // If the previous value is null then we've reached the start of the timeline.
            // Just return the last value.
            if (result.Prev != null)
                result = result.Prev;
            else
                return GetLatest();
        }

        return result.Val;
    }

    public void Set(long t, TValueType val, boolean authoritive)
    {
    	TimelineValue<TValueType> v = new TimelineValue<TValueType>(val, t, authoritive);

        _lock.lock();
        try {
            if (_lastValue == null)
            {
                _firstValue = v;
                v.Prev = null;
                _lastValue = v;
            }
            else
            {
                if (_lastValue.Time > t)
                    _lastValue = ValueBefore(t);

                _lastValue.Next = v;
                v.Prev = _lastValue;
                _lastValue = v;
            }

            if (_firstValue != null)
            {
                while (_firstValue.Time < t - getMaxTimelineLength())
                {
                    _firstValue = _firstValue.Next;
                    _firstValue.Prev = null;
                }
            }
        } finally {
        	_lock.unlock();
        }
    }

    public void Clear()
    {
        _lock.lock();
        try
        {
            _firstValue = null;
            _lastValue = null;
        } finally {
            _lock.unlock();
        }
    }

    public void ClearNonAuthoritive()
    {
    	
        _lock.lock();
        
        try {
        	TimelineValue<TValueType> state = _lastValue;

            while (!state.Authoritive)
            {
                state = state.Prev;

                // Reached the start of the timeline -- simply clear first and last values.
                if (state == null)
                {
                    Clear();
                    return;
                }

                state.Next = null;
            }

            _lastValue = state;
        } finally {
        	_lock.unlock();
        }
    }
	
    public abstract TValueType Interpolate(long t);
    
    public abstract TValueType Extrapolate(long t);
	
}
