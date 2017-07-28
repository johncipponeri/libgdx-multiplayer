package com.xeno.game.network.common;

public interface ITimeline<TValueType> {
		boolean Ready(); // { get; }

        int ValueCount(); // { get; }

        long getMaxTimelineLength(); // { get; set; }
        
        long setMaxTimelineLength(long t);

        TValueType Get(long t);

        TValueType GetLatest();

        TimelineValue<TValueType> GetLatestValue();

        TValueType GetLatestAuthoritive();

        void Set(long t, TValueType val, boolean authoritive);

        void Clear();

        void ClearNonAuthoritive();
}
