package com.zx.demo.data;

public enum JobStatus {
    PENDING,
    COMPLETED,
    ERROR;

    public static JobStatus fromString(String s) {
        try {
            return JobStatus.valueOf(s);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid job status string: " + s);
            return null;
        }
    }
}
