package com.xeno.game.util;

import java.util.concurrent.locks.ReentrantLock;

public class SystemTime
{
	private static long loadTime;
    private static long frozenTime;
    private static int freezeCounter;

    private static long currentUpdateTime;
    private static long prevUpdateTime;

    private static ReentrantLock freezeLock;
    
    public SystemTime()
    {
	    	loadTime = System.currentTimeMillis();
	    	frozenTime = 0;
	    	freezeCounter = 0;
	    	prevUpdateTime = 0;
	    	currentUpdateTime = 0;
	    	freezeLock = new ReentrantLock();
    }

    public static void Freeze()
    {
	    	freezeLock.lock();
	    	try {
	            if (freezeCounter == 0)
	                frozenTime = CurrentTimeMS();
	
	            freezeCounter++;
	        } finally {
	        	freezeLock.unlock();
	        }
    }

    public static void Defrost()
    {
        freezeLock.lock();
        try {
            freezeCounter--;
        } finally {
        	freezeLock.unlock();
        }
    }

    public static void StartUpdate()
    {
        Freeze();
        prevUpdateTime = currentUpdateTime;
        currentUpdateTime = CurrentTimeMS();
    }

    public static void EndUpdate()
    {
        Defrost();
    }

    public static long CurrentTimeMS()
    {
        return (System.currentTimeMillis() - loadTime);
    }

    public static long CurrentFrozenTimeMS()
    {
        if (freezeCounter == 0)
            return CurrentTimeMS();

        return frozenTime;
    }
}