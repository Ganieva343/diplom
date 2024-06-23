package ru.ganieva343.diplom;

public class State {
    private String deviceID; // id устройства
    private String deviceName; // название
    private String deviceType;  // тип
    private int imageResource; // ресурс картинки


    public State(String deviceID, String name, String type, int image){

        this.deviceID=deviceID;
        this.deviceName=name;
        this.deviceType=type;
        this.imageResource=image;
    }

    public String getDeviceID() {
        return this.deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getName() {
        return this.deviceName;
    }

    public void setName(String name) {
        this.deviceName = name;
    }

    public String getType() {
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
