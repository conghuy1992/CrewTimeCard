package timecard.dazone.com.dazonetimecard.dtos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dat on 7/27/2016.
 */
public class BeaconDTO implements Parcelable {
    private String BeaconUUID;
    private int BeaconMajor;
    private int BeaconMinor;
    private int PointNo;
    private int ModUserNo;
    private String ModDate;
    private int LocationNo;
    private String OfficeName;
    private double OfficeLatitude;
    private double OfficeLongitude;
    private int OfficeErrorRange;
    private String OfficeDescription;

    public BeaconDTO() {
    }

    @Override
    public String toString() {
        return "BeaconDTO{" +
                "BeaconUUID='" + BeaconUUID + '\'' +
                ", BeaconMajor=" + BeaconMajor +
                ", BeaconMinor=" + BeaconMinor +
                ", PointNo=" + PointNo +
                ", ModUserNo=" + ModUserNo +
                ", ModDate='" + ModDate + '\'' +
                ", LocationNo=" + LocationNo +
                ", OfficeName='" + OfficeName + '\'' +
                ", OfficeLatitude=" + OfficeLatitude +
                ", OfficeLongitude=" + OfficeLongitude +
                ", OfficeErrorRange=" + OfficeErrorRange +
                ", OfficeDescription='" + OfficeDescription + '\'' +
                '}';
    }

    public String getBeaconUUID() {
        return BeaconUUID;
    }

    public void setBeaconUUID(String beaconUUID) {
        BeaconUUID = beaconUUID;
    }

    public int getBeaconMajor() {
        return BeaconMajor;
    }

    public void setBeaconMajor(int beaconMajor) {
        BeaconMajor = beaconMajor;
    }

    public int getBeaconMinor() {
        return BeaconMinor;
    }

    public void setBeaconMinor(int beaconMinor) {
        BeaconMinor = beaconMinor;
    }

    public int getPointNo() {
        return PointNo;
    }

    public void setPointNo(int pointNo) {
        PointNo = pointNo;
    }

    public int getModUserNo() {
        return ModUserNo;
    }

    public void setModUserNo(int modUserNo) {
        ModUserNo = modUserNo;
    }

    public String getModDate() {
        return ModDate;
    }

    public void setModDate(String modDate) {
        ModDate = modDate;
    }

    public int getLocationNo() {
        return LocationNo;
    }

    public void setLocationNo(int locationNo) {
        LocationNo = locationNo;
    }

    public String getOfficeName() {
        return OfficeName;
    }

    public void setOfficeName(String officeName) {
        OfficeName = officeName;
    }

    public double getOfficeLatitude() {
        return OfficeLatitude;
    }

    public void setOfficeLatitude(double officeLatitude) {
        OfficeLatitude = officeLatitude;
    }

    public double getOfficeLongitude() {
        return OfficeLongitude;
    }

    public void setOfficeLongitude(double officeLongitude) {
        OfficeLongitude = officeLongitude;
    }

    public int getOfficeErrorRange() {
        return OfficeErrorRange;
    }

    public void setOfficeErrorRange(int officeErrorRange) {
        OfficeErrorRange = officeErrorRange;
    }

    public String getOfficeDescription() {
        return OfficeDescription;
    }

    public void setOfficeDescription(String officeDescription) {
        OfficeDescription = officeDescription;
    }

    public static Creator<BeaconDTO> getCREATOR() {
        return CREATOR;
    }

    protected BeaconDTO(Parcel in) {
        BeaconUUID = in.readString();
        BeaconMajor = in.readInt();
        BeaconMinor = in.readInt();
        PointNo = in.readInt();
        ModUserNo = in.readInt();
        ModDate = in.readString();
        LocationNo = in.readInt();
        OfficeName = in.readString();
        OfficeLatitude = in.readDouble();
        OfficeLongitude = in.readDouble();
        OfficeErrorRange = in.readInt();
        OfficeDescription = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(BeaconUUID);
        dest.writeInt(BeaconMajor);
        dest.writeInt(BeaconMinor);
        dest.writeInt(PointNo);
        dest.writeInt(ModUserNo);
        dest.writeString(ModDate);
        dest.writeInt(LocationNo);
        dest.writeString(OfficeName);
        dest.writeDouble(OfficeLatitude);
        dest.writeDouble(OfficeLongitude);
        dest.writeInt(OfficeErrorRange);
        dest.writeString(OfficeDescription);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BeaconDTO> CREATOR = new Creator<BeaconDTO>() {
        @Override
        public BeaconDTO createFromParcel(Parcel in) {
            return new BeaconDTO(in);
        }

        @Override
        public BeaconDTO[] newArray(int size) {
            return new BeaconDTO[size];
        }
    };
}
