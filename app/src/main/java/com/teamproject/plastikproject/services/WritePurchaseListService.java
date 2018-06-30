package com.teamproject.plastikproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import com.teamproject.plastikproject.helpers.ContentHelper;
import com.teamproject.plastikproject.model.PurchaseListModel;
import com.teamproject.plastikproject.utils.AlarmUtils;

/**
 * Created by rage on 5/15/15.
 */
public class WritePurchaseListService extends IntentService {
    public static final String LIST_EXTRA = "PurchaseList";

    public WritePurchaseListService() {
        super("WritePurchaseListService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        PurchaseListModel purchaseList = (PurchaseListModel) intent.getExtras().getSerializable(LIST_EXTRA);
        if (purchaseList != null) {
            if (purchaseList.getDbId() > 0) {
                ContentHelper.updatePurchaseList(getApplicationContext(), purchaseList);
            } else {
                Uri uri = ContentHelper.insertPurchaseList(getApplicationContext(), purchaseList);
                purchaseList.setDbId(Long.parseLong(uri.getLastPathSegment()));

                if (purchaseList.getTimeAlarm() > System.currentTimeMillis()
                        && purchaseList.getDbId() > 0) {
                    AlarmUtils alarmUtils = new AlarmUtils(getApplicationContext());
                    alarmUtils.setListAlarm(purchaseList);
                }
            }
        }
    }
}
