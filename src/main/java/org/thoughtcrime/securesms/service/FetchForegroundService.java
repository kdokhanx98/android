package org.thoughtcrime.securesms.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.notifications.NotificationCenter;

public final class FetchForegroundService extends Service {
  private static final Object SERVICE_LOCK = new Object();
  private static Intent service;

  public static void start(Context context) {
    GenericForegroundService.createFgNotificationChannel(context);
    synchronized (SERVICE_LOCK) {
      if (service == null) {
        service = new Intent(context, FetchForegroundService.class);
        ContextCompat.startForegroundService(context, service);
      }
    }
  }

  public static void stop(Context context) {
    synchronized (SERVICE_LOCK) {
      if (service != null) {
        context.stopService(service);
        service = null;
      }
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();

    Notification notification = new NotificationCompat.Builder(this, NotificationCenter.CH_GENERIC)
      .setContentTitle(getString(R.string.connectivity_updating))
      .setSmallIcon(R.drawable.notification_permanent)
      .build();

    startForeground(NotificationCenter.ID_FETCH, notification);
  }

  @Override
  public void onDestroy() {
    stopForeground(true);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
