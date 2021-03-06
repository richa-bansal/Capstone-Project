package com.village.joinalong.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by richa on 6/21/16.
 */
public class ContactAndGroup implements Comparable, Parcelable {
    private String name;
    private String id;
    private String photoUrl;
    private boolean isGroup;
    private boolean isSelected;
    private int type;  //0 for label, 1 for group and 2 for contact

    public ContactAndGroup(String name, String id, String photoUrl, boolean isGroup, int type) {
        this.name = name;
        this.id = id;
        this.photoUrl = photoUrl;
        this.isGroup = isGroup;
        this.isSelected = false;
        this.type = type;
    }

    protected ContactAndGroup(Parcel in) {
        name = in.readString();
        id = in.readString();
        photoUrl = in.readString();
        isGroup = in.readByte() != 0;
        isSelected = in.readByte() != 0;
        type = in.readInt();
    }

    public static final Creator<ContactAndGroup> CREATOR = new Creator<ContactAndGroup>() {
        @Override
        public ContactAndGroup createFromParcel(Parcel in) {
            return new ContactAndGroup(in);
        }

        @Override
        public ContactAndGroup[] newArray(int size) {
            return new ContactAndGroup[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(Object another) {

        if (this.isSelected && !((ContactAndGroup)another).isSelected() )
            return -1;
        else if (!this.isSelected && ((ContactAndGroup)another).isSelected() )
            return 1;
        else if (this.isGroup && !((ContactAndGroup)another).isGroup())
        return -1;
        else if (!this.isGroup && ((ContactAndGroup)another).isGroup() )
            return 1;
        else return this.name.compareTo(((ContactAndGroup)another).getName());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(photoUrl);
        dest.writeByte((byte) (isGroup ? 1 : 0));
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(type);
    }
}
