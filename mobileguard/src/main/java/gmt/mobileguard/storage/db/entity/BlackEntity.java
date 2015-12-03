package gmt.mobileguard.storage.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.storage.db.entity
 * Created by Genment at 2015/11/20 00:29.
 */
public class BlackEntity implements Parcelable {
    private int _id;
    private String number;
    private int mode;
    private String description;
    private String attribution;
    private int count;

    public BlackEntity() {
    }

    protected BlackEntity(Parcel in) {
        _id = in.readInt();
        number = in.readString();
        mode = in.readInt();
        description = in.readString();
        attribution = in.readString();
        count = in.readInt();
    }

    public static final Creator<BlackEntity> CREATOR = new Creator<BlackEntity>() {
        @Override
        public BlackEntity createFromParcel(Parcel in) {
            return new BlackEntity(in);
        }

        @Override
        public BlackEntity[] newArray(int size) {
            return new BlackEntity[size];
        }
    };

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

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(number);
        dest.writeInt(mode);
        dest.writeString(description);
        dest.writeString(attribution);
        dest.writeInt(count);
    }

}
