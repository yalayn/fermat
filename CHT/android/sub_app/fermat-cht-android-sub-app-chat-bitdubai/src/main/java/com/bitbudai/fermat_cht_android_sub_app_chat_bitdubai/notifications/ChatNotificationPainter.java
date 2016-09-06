package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.notifications;

import android.widget.RemoteViews;

import com.bitdubai.fermat_android_api.engine.NotificationPainter;

/**
 * Created by Eleazar (eorono@protonmail.com) on 13/04/16.
 */
public class ChatNotificationPainter extends NotificationPainter {

    private String title;
    private String image;
    private String textBody;
    private int icon;


    public ChatNotificationPainter(String title, String textBody, String image, int icon) {
        this.title = title;
        this.image = image;
        this.textBody = textBody;
        this.icon = icon;
    }

    @Override
    public RemoteViews getNotificationView(String code) {
        return null;
    }

    @Override
    public String getNotificationTitle() {
        return title;
    }

    @Override
    public String getNotificationImageText() {
        return image;
    }

    @Override
    public String getNotificationTextBody() {
        return textBody;
    }

    @Override
    public int getIcon() {
        return icon;
    }

    @Override
    public String getActivityCodeResult() {
        return null;
    }

    @Override
    public boolean showNotification() {
        return true;
    }
}



