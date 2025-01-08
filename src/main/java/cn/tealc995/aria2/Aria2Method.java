package cn.tealc995.aria2;

public enum Aria2Method {

    TELL_ACTIVE("aria2.tellActive"),
    ADD_URI("aria2.addUri");


    private final String value;

    Aria2Method(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
