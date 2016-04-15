package info.alchemistdigital.e_carrier.model;

/**
 * Created by user on 2/6/2016.
 */
public class Email_Account_Item {
    private String type;
    private String name;

    public Email_Account_Item(String type, String name) {
        this.setType(type);
        this.setName(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
