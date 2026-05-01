package edu.sdccd.cisc191.model;

public class Player {
    private String name;

    public Player(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            this.name = "Player";
        } else {
            this.name = name;
        }
    }
}
