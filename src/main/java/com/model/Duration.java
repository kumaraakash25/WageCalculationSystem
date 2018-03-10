package com.model;

/**
 * Shift duration in hours 24 hour format.
 */
public enum Duration {
    /**
     * Null.
     */
    NULL(0),
    /**
     * Regular work hours.
     */
    STANDARD_WORK_HOURS(8),
    /**
     * Duration for overtime in first 3 hours.
     */
    FIRST_OVERTIME_DURATION(3),
    /**
     * Duration for the overtime in the next 1 hour.
     */
    SECOND_OVERTIME_DURATION(1),
    /**
     * Evening shift start time in hours.
     */
    EVENING_SHIFT_START_HOUR(19),
    /**
     * Evening shift end time in hours.
     */
    EVENING_SHIFT_END_HOUR(6);

    /**
     * Time duration in hours.
     */
    private final float duration;

    public float getDuration() {
        return duration;
    }

    Duration(final float duration) {
        this.duration = duration;
    }
}
