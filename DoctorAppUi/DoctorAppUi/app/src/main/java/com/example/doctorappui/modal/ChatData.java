package com.example.doctorappui.modal;

public
class ChatData {
    String strName,strLastMsg;
    Integer image;

    public
    ChatData(String strName, String strLastMsg, Integer image) {
        this.strName = strName;
        this.strLastMsg = strLastMsg;
        this.image = image;
    }

    public
    String getStrName() {
        return strName;
    }

    public
    void setStrName(String strName) {
        this.strName = strName;
    }

    public
    String getStrLastMsg() {
        return strLastMsg;
    }

    public
    void setStrLastMsg(String strLastMsg) {
        this.strLastMsg = strLastMsg;
    }

    public
    Integer getImage() {
        return image;
    }

    public
    void setImage(Integer image) {
        this.image = image;
    }
}
