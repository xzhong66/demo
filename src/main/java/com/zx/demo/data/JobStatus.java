package com.zx.demo.data;

public enum JobStatus {
    PENDING,
    COMPLETED,
    ERROR;

    /***
     * Convert string to enum
     * @param s
     * @return enum type
     */
    public static JobStatus fromString(String s) {
        try {
            return JobStatus.valueOf(s);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid job status string: " + s);
            return null;
        }
    }
}
