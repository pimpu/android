package info.alchemistdigital.e_carrier.model;

/**
 * Created by user on 1/16/2016.
 */
public class Enquired_Details_Item {
    private String beginning;
    private String enquiryDate;
    private String  driverData;
    private int enquiryId;

    public String getBeginning() {
        return beginning;
    }

    public void setBeginning(String desination) {
        this.beginning = desination;
    }

    public String getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(String enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public int getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(int enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getDriverData() {
        return driverData;
    }

    public void setDriverData(String driverData) {
        this.driverData = driverData;
    }
}
