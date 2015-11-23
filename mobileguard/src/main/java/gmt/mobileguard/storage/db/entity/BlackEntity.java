package gmt.mobileguard.storage.db.entity;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.storage.db.entity
 * Created by Genment at 2015/11/20 00:29.
 */
public class BlackEntity {
    private int _id;
    private String number;
    private int mode;
    private String name;
    private String attribution;
    private int count;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getAttribution() {
        return attribution == null ? "" : attribution;
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

    public void setBlackPhone(boolean newMode) {
        if (newMode)
            this.mode |= 0b0001; // add
        else
            this.mode &= 0b0010; // remove
    }

    public void setBlackMessage(boolean newMode) {
        if (newMode)
            this.mode |= 0b0010; // add
        else
            this.mode &= 0b0001; // remove
    }

    public boolean getBlackPhone() {
        return (mode & 0b0001) == 0b0001;
    }

    public boolean getBlackMessage() {
        return (mode & 0b0010) == 0b0010;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getName() {
        return name == null ? "" : name;
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
