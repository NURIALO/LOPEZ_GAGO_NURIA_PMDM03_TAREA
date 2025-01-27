package com.lopezgagonuria_pmdm.u3;
public class PlayerData {
    private String name;
    public PlayerData(String name, String description) {
        this.name = name;
        this.description = description;
    }
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
