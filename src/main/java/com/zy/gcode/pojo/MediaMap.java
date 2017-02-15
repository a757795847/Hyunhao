package com.zy.gcode.pojo;

/**
 * Created by admin5 on 17/2/15.
 */
public class MediaMap {
    private String owner;
    private String type;
    private String mediaId;
    private String path;
    private String isPersistence;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIsPersistence() {
        return isPersistence;
    }

    public void setIsPersistence(String isPersistence) {
        this.isPersistence = isPersistence;
    }
}
