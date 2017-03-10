package com.cleanslatetech.floc.models;

import com.cleanslatetech.floc.interfaces.isEmpty;
import com.cleanslatetech.floc.interfaces.isValidEmail;
import com.cleanslatetech.floc.interfaces.isValidPhone;
import com.cleanslatetech.floc.utilities.FormValidator;

/**
 * Created by pimpu on 3/8/2017.
 */

public class MyProfileModel {
    private int Id;
    private String UserName;

    @isEmpty( errorMsg="First name cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String FirstName;

    @isEmpty( errorMsg="Last name cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String LastName;

    @isEmpty( errorMsg="Middle name cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String MiddleName;

    @isEmpty( errorMsg="Please, select gender", errorDisplayType= FormValidator.DisplayType.Toast)
    private String Gender;

    @isValidPhone( errorMsg="Contact number cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String Contact;

    @isValidEmail( errorMsg="Email id is not valid", errorDisplayType= FormValidator.DisplayType.Toast)
    private String EmailId;

    @isEmpty( errorMsg="Profession cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String Profession;

    @isEmpty( errorMsg="City cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String City;

    @isEmpty( errorMsg="State cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String State;

    @isEmpty( errorMsg="Country cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String Country;

    @isEmpty( errorMsg="Pincode cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String PinCode;

    @isEmpty( errorMsg="Profile image cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String ProfilePic;

    @isEmpty( errorMsg="Birthdate cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String BirthDate;

    @isEmpty( errorMsg="BankName cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String BankName;

    @isEmpty( errorMsg="Branch cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String Branch;

    @isEmpty( errorMsg="IFSC cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String IFSC;

    @isEmpty( errorMsg="Account cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String Account;

    @isEmpty( errorMsg="URL cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String URL;

    public MyProfileModel() {
    }

    public MyProfileModel(int Id, String userName, String firstName, String middleName, String lastName, String gender, String contact, String emailId, String profession, String city, String state, String country, String pinCode, String profilePic, String birthDate, String bankName, String branch, String IFSC, String account, String URL) {
        this.Id = Id;
        UserName = userName;
        FirstName = firstName;
        LastName = lastName;
        MiddleName = middleName;
        Gender = gender;
        Contact = contact;
        EmailId = emailId;
        Profession = profession;
        City = city;
        State = state;
        Country = country;
        PinCode = pinCode;
        ProfilePic = profilePic;
        BirthDate = birthDate;
        BankName = bankName;
        Branch = branch;
        this.IFSC = IFSC;
        Account = account;
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "{" +
                "\"Id\":\""+ Id + '\"' +
                ",\"UserName\":\""+ UserName + '\"' +
                ",\"FirstName\":\"" + FirstName + '\"' +
                ",\"LastName\":\""+ LastName + '\"' +
                ",\"MiddleName\":\""+ MiddleName + '\"' +
                ",\"Gender\":\""+ Gender + '\"' +
                ",\"Contact\":\""+ Contact + '\"' +
                ",\"EmailId\":\"" + EmailId + '\"' +
                ",\"Profession\":\""+ Profession + '\"' +
                ",\"City\":\""+ City + '\"' +
                ",\"State\":\"" + State + '\"' +
                ",\"Country\":\"" + Country + '\"' +
                ",\"PinCode\":\"" + PinCode + '\"' +
                ",\"ProfilePic\":\"" + ProfilePic + '\"' +
                ",\"BirthDate\":\""+ BirthDate + '\"' +
                ",\"BankName\":\""+ BankName + '\"' +
                ",\"Branch\":\""+ Branch + '\"' +
                ",\"IFSC\":\"" + IFSC + '\"' +
                ",\"Account\":\"" + Account + '\"' +
                ",\"URL\":\"" + URL + '\"' +
                '}';
    }

    public int getUserId() {
        return Id;
    }

    public void setUserId(int userId) {
        Id = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getProfession() {
        return Profession;
    }

    public void setProfession(String profession) {
        Profession = profession;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getIFSC() {
        return IFSC;
    }

    public void setIFSC(String IFSC) {
        this.IFSC = IFSC;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
