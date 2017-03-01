package com.cleanslatetech.floc.models;

import java.util.Date;

/**
 * Created by pimpu on 2/26/2017.
 */

public class EventsModel {
    private int EventId;
    private int EventCreatorId;
    private String EventName;
    private int EventCategory ;
    private String EventDescription;
    private String EventPicture;
    private String EventStartDate;
    private String EventStartHour;
    private String EventStartMin;
    private String EventEndDate;
    private String EventEndHour;
    private String EventEndMin;
    private int EventPriceType;
    private String EventPrice;
    private int EventMembers;
    private String EventCity;
    private String EventArea;
    private String EventAddress;
    private String EventState;
    private String EventCountry;
    private String EventUrl;
    private String EventReason;
    private String EventPublish;
    private String ManagementService;
    private String ConciergeServices;
    private int EventStatus;
    private boolean IsExclusive;

    public EventsModel() {
    }

    public EventsModel(int eventId, int eventCreatorId, String eventName, int eventCategory, String eventDescription, String eventPicture, String eventStartDate, String eventStartHour, String eventStartMin, String eventEndDate, String eventEndHour, String eventEndMin, int eventPriceType, String eventPrice, int eventMembers, String eventCity, String eventArea, String eventAddress, String eventState, String eventCountry, String eventUrl, String eventReason, String eventPublish, String managementService, String conciergeServices, int eventStatus, boolean isExclusive) {
        EventId = eventId;
        EventCreatorId = eventCreatorId;
        EventName = eventName;
        EventCategory = eventCategory;
        EventDescription = eventDescription;
        EventPicture = eventPicture;
        EventStartDate = eventStartDate;
        EventStartHour = eventStartHour;
        EventStartMin = eventStartMin;
        EventEndDate = eventEndDate;
        EventEndHour = eventEndHour;
        EventEndMin = eventEndMin;
        EventPriceType = eventPriceType;
        EventPrice = eventPrice;
        EventMembers = eventMembers;
        EventCity = eventCity;
        EventArea = eventArea;
        EventAddress = eventAddress;
        EventState = eventState;
        EventCountry = eventCountry;
        EventUrl = eventUrl;
        EventReason = eventReason;
        EventPublish = eventPublish;
        ManagementService = managementService;
        ConciergeServices = conciergeServices;
        EventStatus = eventStatus;
        IsExclusive = isExclusive;
    }

    @Override
    public String toString() {
        return "{" +
                "\"EventId\":\""+ EventId + '\"' +
                ",\"EventCreatorId\":\""+ EventCreatorId + '\"' +
                ",\"EventName\":\""+ EventName + '\"' +
                ",\"EventCategory\":\""+ EventCategory + '\"' +
                ",\"EventDescription\":\""+ EventDescription + '\"' +
                ",\"EventPicture\":\""+ EventPicture + '\"' +
                ",\"EventStartDate\":\""+ EventStartDate + '\"' +
                ",\"EventStartHour\":\""+ EventStartHour + '\"' +
                ",\"EventStartMin\":\""+ EventStartMin + '\"' +
                ",\"EventEndDate\":\""+ EventEndDate + '\"' +
                ",\"EventEndHour\":\""+ EventEndHour + '\"' +
                ",\"EventEndMin\":\""+ EventEndMin + '\"' +
                ",\"EventPriceType\":\"" + EventPriceType + '\"' +
                ",\"EventPrice\":\""+ EventPrice + '\"' +
                ",\"EventMembers\":\"" + EventMembers + '\"' +
                ",\"EventCity\":\""+ EventCity + '\"' +
                ",\"EventArea\":\""+ EventArea + '\"' +
                ",\"EventAddress\":\""+ EventAddress + '\"' +
                ",\"EventState\":\""+ EventState + '\"' +
                ",\"EventCountry\":\""+ EventCountry + '\"' +
                ",\"EventUrl\":\""+ EventUrl + '\"' +
                ",\"EventReason\":\""+ EventReason + '\"' +
                ",\"EventPublish\":\""+ EventPublish + '\"' +
                ",\"ManagementService\":\""+ ManagementService + '\"' +
                ",\"ConciergeServices\":\""+ ConciergeServices + '\"' +
                ",\"EventStatus\":\""+ EventStatus + '\"' +
                ",\"IsExclusive\":\""+ IsExclusive + '\"' +
                '}';
    }

    public int getEventId() {
        return EventId;
    }

    public void setEventId(int eventId) {
        EventId = eventId;
    }

    public int getEventCreatorId() {
        return EventCreatorId;
    }

    public void setEventCreatorId(int eventCreatorId) {
        EventCreatorId = eventCreatorId;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public int getEventCategory() {
        return EventCategory;
    }

    public void setEventCategory(int eventCategory) {
        EventCategory = eventCategory;
    }

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public String getEventPicture() {
        return EventPicture;
    }

    public void setEventPicture(String eventPicture) {
        EventPicture = eventPicture;
    }

    public String getEventStartDate() {
        return EventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        EventStartDate = eventStartDate;
    }

    public String getEventStartHour() {
        return EventStartHour;
    }

    public void setEventStartHour(String eventStartHour) {
        EventStartHour = eventStartHour;
    }

    public String getEventStartMin() {
        return EventStartMin;
    }

    public void setEventStartMin(String eventStartMin) {
        EventStartMin = eventStartMin;
    }

    public String getEventEndDate() {
        return EventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        EventEndDate = eventEndDate;
    }

    public String getEventEndHour() {
        return EventEndHour;
    }

    public void setEventEndHour(String eventEndHour) {
        EventEndHour = eventEndHour;
    }

    public String getEventEndMin() {
        return EventEndMin;
    }

    public void setEventEndMin(String eventEndMin) {
        EventEndMin = eventEndMin;
    }

    public int getEventPriceType() {
        return EventPriceType;
    }

    public void setEventPriceType(int eventPriceType) {
        EventPriceType = eventPriceType;
    }

    public String getEventPrice() {
        return EventPrice;
    }

    public void setEventPrice(String eventPrice) {
        EventPrice = eventPrice;
    }

    public int getEventMembers() {
        return EventMembers;
    }

    public void setEventMembers(int eventMembers) {
        EventMembers = eventMembers;
    }

    public String getEventCity() {
        return EventCity;
    }

    public void setEventCity(String eventCity) {
        EventCity = eventCity;
    }

    public String getEventArea() {
        return EventArea;
    }

    public void setEventArea(String eventArea) {
        EventArea = eventArea;
    }

    public String getEventAddress() {
        return EventAddress;
    }

    public void setEventAddress(String eventAddress) {
        EventAddress = eventAddress;
    }

    public String getEventState() {
        return EventState;
    }

    public void setEventState(String eventState) {
        EventState = eventState;
    }

    public String getEventCountry() {
        return EventCountry;
    }

    public void setEventCountry(String eventCountry) {
        EventCountry = eventCountry;
    }

    public String getEventUrl() {
        return EventUrl;
    }

    public void setEventUrl(String eventUrl) {
        EventUrl = eventUrl;
    }

    public String getEventReason() {
        return EventReason;
    }

    public void setEventReason(String eventReason) {
        EventReason = eventReason;
    }

    public String getEventPublish() {
        return EventPublish;
    }

    public void setEventPublish(String eventPublish) {
        EventPublish = eventPublish;
    }

    public String getManagementService() {
        return ManagementService;
    }

    public void setManagementService(String managementService) {
        ManagementService = managementService;
    }

    public String getConciergeServices() {
        return ConciergeServices;
    }

    public void setConciergeServices(String conciergeServices) {
        ConciergeServices = conciergeServices;
    }

    public int getEventStatus() {
        return EventStatus;
    }

    public void setEventStatus(int eventStatus) {
        EventStatus = eventStatus;
    }

    public boolean getIsExclusive() {
        return IsExclusive;
    }

    public void setIsExclusive(boolean exclusive) {
        IsExclusive = exclusive;
    }
}
