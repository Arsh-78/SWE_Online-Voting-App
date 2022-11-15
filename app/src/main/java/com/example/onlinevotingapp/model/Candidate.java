package com.example.onlinevotingapp.model;

public class Candidate {
    String name;
    String group;
    String position;
    String image;
    String id;
    int count = 0;
    public Candidate(String name, String group, String position, String image,String id) {
        this.name = name;
        this.group = group;
        this.position = position;
        this.image = image;
        this.id= id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
