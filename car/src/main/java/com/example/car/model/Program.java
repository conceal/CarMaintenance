package com.example.car.model;

public class Program {
    private int id;
    private String program_name;
    private String program_url;
    private int store_id;
    private String store_name;
    private int price;
    protected int trading_volume;
    private String details;
    private int score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public String getProgram_url() {
        return program_url;
    }

    public void setProgram_url(String program_url) {
        this.program_url = program_url;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTrading_volume() {
        return trading_volume;
    }

    public void setTrading_volume(int trading_volume) {
        this.trading_volume = trading_volume;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
