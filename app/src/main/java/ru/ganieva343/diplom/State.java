package ru.ganieva343.diplom;

public class State {
    private String deviceName; // название
    private String deviceType;  // тип
    private int imageResource; // ресурс картинки

    public State(String name, String type, int image){

        this.deviceName=name;
        this.deviceType=type;
        this.imageResource=image;
    }

    public String getName() {
        return this.deviceName;
    }

    public void setName(String name) {
        this.deviceName = name;
    }

    public String deviceType() {
        return this.deviceType;
    }

    public void setType(String type) {
        this.deviceType = type;
    }

    public int getImageResource() {
        return this.imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
