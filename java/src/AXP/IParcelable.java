package AXP;

public interface IParcelable
{
    int ReadFromParcel(Parcel parcel);

    int WriteToParcel(Parcel parcel);
}