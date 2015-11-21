package gmt.mobileguard.util.entity;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.util.entity
 * Created by Genment at 2015/11/20 00:29.
 */
public class BlackEntity {
    private String _id;
    private String number;
    private int mode;
    private String name;
    private String attribution;
    private int count;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setBlackPhone() {
        this.mode |= 1;
    }

    public void setBlackMessage() {
        this.mode |= 2;
    }

    public boolean getBlackPhone() {
        return (mode & 1) == 1;
    }

    public boolean getBlackMessage() {
        return (mode & 2) == 2;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
