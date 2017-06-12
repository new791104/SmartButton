package com.button.smart.smartbutton.Objects;

/**
 * Created by charlie on 2017/6/3.
 */

public class ButtonItem {
    private String _id;
    private String user;
    private String bid;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private String group;
    private String name;
    private String description;
    private Boolean status;

    public ButtonItem(String _id, String user, String bid, String group, String name, String description, Boolean status) {
        this._id = _id;
        this.user = user;
        this.bid = bid;
        this.group = group;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
