package com.cleanslatetech.floc.utilities;

import org.json.JSONArray;

/**
 * Created by pimpu on 3/4/2017.
 */

public interface InterfaceFlocDescTopics {
    void onClickLike(JSONArray jsonArrayAllLike);
    void onClickReview(JSONArray jsonArrayAllReviews);
    void onClickBooking(JSONArray jsonArrayAllBooking);
    void getChatData(JSONArray jsonArrayAllChats);
}
