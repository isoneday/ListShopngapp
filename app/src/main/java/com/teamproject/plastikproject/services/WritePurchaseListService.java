package com.teamproject.plastikproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.teamproject.plastikproject.helpers.ContentHelper;
import com.teamproject.plastikproject.model.PurchaseListModelbar;
import com.teamproject.plastikproject.plastik.helper.SessionManager;
import com.teamproject.plastikproject.utils.AlarmUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        PurchaseListModelbar purchaseList = (PurchaseListModelbar) intent.getExtras().getSerializable(LIST_EXTRA);
        if (purchaseList != null) {
            if (purchaseList.getIdUser() > 0) {
                ContentHelper.updatePurchaseList(getApplicationContext(), purchaseList);
            } else {
                Uri uri = ContentHelper.insertPurchaseList(getApplicationContext(), purchaseList);
//                purchaseList.getIdUser(uri.getLastPathSegment());
  purchaseList.getIdUser();

                Date currentDate = new Date(System.currentTimeMillis());
                DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                System.out.println("Milliseconds to Date using Calendar:"
                        + df.format(cal.getTime()));

                Timestamp sq = new Timestamp(cal.getTime().getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Log.d("currentdatetimestampe",sdf.format(sq)); //this will print wit
               // desc =listNameEdit.getText().toString();
              String  waktusekarang = sdf.format(sq);
                SessionManager manager =new SessionManager(getApplicationContext());
                String waktuset =manager.getTime();

                long sekarang = Long.parseLong(waktusekarang);
                long set =Long.parseLong(waktuset);
                if (set > sekarang
                        && purchaseList.getIdUser() > 0) {
                    AlarmUtils alarmUtils = new AlarmUtils(getApplicationContext());
                    alarmUtils.setListAlarm(purchaseList);
                }
            }
        }
    }
}
