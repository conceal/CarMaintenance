package com.example.car.model;

public class MaintenanceInfo {
    private int id;
    private int user_id;
    private String program_name;
    private int maintenance_time;
    private int time_interval;
    private String last_time;
    private String next_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public int getMaintenance_time() {
        return maintenance_time;
    }

    public void setMaintenance_time(int maintenance_time) {
        this.maintenance_time = maintenance_time;
    }

    public int getTime_interval() {
        return time_interval;
    }

    public void setTime_interval(int time_interval) {
        this.time_interval = time_interval;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public String getNext_time() {
        return next_time;
    }

    public void setNext_time(String next_time) {
        this.next_time = next_time;
    }
}
