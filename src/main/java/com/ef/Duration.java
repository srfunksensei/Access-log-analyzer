package com.ef;

/**
 * Enum describing duration
 * 
 * @author mb
 *
 */
public enum Duration {

    HOURLY("HOURLY"), DAILY("DAILY");

    private final String label;

    Duration(final String label) {
        this.label = label.toUpperCase();
    }

    public static Duration findByLabel(String byLabel) {
        for (final Duration type : Duration.values()) {
            if (type.label.equalsIgnoreCase(byLabel.trim())) {
                return type;
            }
        }
        
        return null;
    }

}
