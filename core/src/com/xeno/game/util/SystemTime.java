package com.xeno.game.util;

import java.util.concurrent.locks.ReentrantLock;

public class SystemTime
{
    private static long _frozenTime;
    private static int _freezeCounter;

    private static long _currentUpdateTime;
    private static long _prevUpdateTime;

    private static ReentrantLock _freezeLock;
    
    public SystemTime()
    {
    	_frozenTime = 0;
    	_freezeCounter = 0;
    	_prevUpdateTime = 0;
    	_currentUpdateTime = 0;
    	_freezeLock = new ReentrantLock();
    }

    public static void Freeze()
    {
    	_freezeLock.lock();
    	try {
            if (_freezeCounter == 0)
                _frozenTime = CurrentTimeMS();

            _freezeCounter++;
        } finally {
        	_freezeLock.unlock();
        }
    }

    public static void Defrost()
    {
        _freezeLock.lock();
        try {
            _freezeCounter--;
        } finally {
        	_freezeLock.unlock();
        }
    }

    public static void StartUpdate()
    {
        Freeze();
        _prevUpdateTime = _currentUpdateTime;
        _currentUpdateTime = CurrentTimeMS();
    }

    public static void EndUpdate()
    {
        Defrost();
    }

    public static long CurrentTimeMS()
    {
        return (System.currentTimeMillis());
    }

    public static long CurrentFrozenTimeMS()
    {
        if (_freezeCounter == 0)
            return CurrentTimeMS();

        return _frozenTime;
    }
}