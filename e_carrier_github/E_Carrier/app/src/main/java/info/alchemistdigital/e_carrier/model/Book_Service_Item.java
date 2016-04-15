package info.alchemistdigital.e_carrier.model;

/**
 * Created by user on 12/24/2015.
 */
public class Book_Service_Item {
    private String fromArea;
    private String toArea;
    private String date;
    private int bookingId;

    public Book_Service_Item() {

    }

    public Book_Service_Item(String fromArea, String toArea, String date, int bookingId) {
        this.fromArea = fromArea;
        this.toArea = toArea;
        this.date = date;
        this.bookingId = bookingId;
    }

    public String getFromArea() {
        return fromArea;
    }

    public void setFromArea(String fromArea) {
        this.fromArea = fromArea;
    }

    public String getToArea() {
        return toArea;
    }

    public void setToArea(String toArea) {
        this.toArea = toArea;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public int getBookingId() { return bookingId; }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
}
