package com.cleanslatetech.floc.models;

import com.cleanslatetech.floc.interfaces.isEmpty;
import com.cleanslatetech.floc.utilities.FormValidator;

/**
 * Created by pimpu on 4/18/2017.
 */

public class ChannelModel {
    @isEmpty( errorMsg="platform type cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String PlatformType;

    @isEmpty( errorMsg="name cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String ChannelName;

    @isEmpty( errorMsg="owner cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String ChannelOwner;

    @isEmpty( errorMsg="password cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String ChannelPassword;

    private String CreateDate;

    @isEmpty( errorMsg="about cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String ChannelAbout;

    @isEmpty( errorMsg="image cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String ChannelImage;

    @isEmpty( errorMsg="type cannot be null", errorDisplayType= FormValidator.DisplayType.Toast)
    private String ChannelType;

    private int ChannelStatus;

    public ChannelModel() {
    }

    public ChannelModel(String channelName, String channelOwner, String channelAbout,
                        String platformType, String channelType, String channelPassword, int channelStatus,
                        String channelImage) {
        ChannelName = channelName;
        ChannelOwner = channelOwner;
        ChannelAbout = channelAbout;
        PlatformType = platformType;
        ChannelType = channelType;
        ChannelPassword = channelPassword;
        ChannelStatus = channelStatus;
        ChannelImage = channelImage;
    }

    @Override
    public String toString() {
        return "{" +
                "\"ChannelName\":\""+ ChannelName + '\"' +
                ", \"ChannelOwner\":\""+ ChannelOwner + '\"' +
                ", \"CreateDate\":\""+ CreateDate + '\"' +
                ", \"ChannelAbout\":\"" + ChannelAbout + '\"' +
                ", \"ChannelImage\":\"" + ChannelImage + '\"' +
                ", \"PlatformType\":\"" +PlatformType + '\"' +
                ", \"ChannelType\":\"" +ChannelType + '\"' +
                ", \"ChannelPassword\":\"" + ChannelPassword + '\"' +
                ", \"ChannelStatus\":" + ChannelStatus +
                '}';
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    public String getChannelOwner() {
        return ChannelOwner;
    }

    public void setChannelOwner(String channelOwner) {
        ChannelOwner = channelOwner;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getChannelAbout() {
        return ChannelAbout;
    }

    public void setChannelAbout(String channelAbout) {
        ChannelAbout = channelAbout;
    }

    public String getChannelImage() {
        return ChannelImage;
    }

    public void setChannelImage(String channelImage) {
        ChannelImage = channelImage;
    }

    public String getPlatformType() {
        return PlatformType;
    }

    public void setPlatformType(String platformType) {
        PlatformType = platformType;
    }

    public String getChannelType() {
        return ChannelType;
    }

    public void setChannelType(String channelType) {
        ChannelType = channelType;
    }

    public String getChannelPassword() {
        return ChannelPassword;
    }

    public void setChannelPassword(String channelPassword) {
        ChannelPassword = channelPassword;
    }

    public int getChannelStatus() {
        return ChannelStatus;
    }

    public void setChannelStatus(int channelStatus) {
        ChannelStatus = channelStatus;
    }
}
