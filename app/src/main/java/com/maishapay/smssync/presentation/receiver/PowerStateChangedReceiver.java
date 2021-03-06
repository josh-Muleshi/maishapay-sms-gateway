/*
 * Copyright (c) 2018 - 2019 Maishapay
 * All rights reserved
 * Contact: contact@maishapay.online
 * Website: http://www.maishapay.online
 * GNU Lesser General Public License Usage
 * This file may be used under the terms of the GNU Lesser
 * General Public License version 3 as published by the Free Software
 * Foundation and appearing in the file LICENSE.LGPL included in the
 * packaging of this file. Please review the following information to
 * ensure the GNU Lesser General Public License version 3 requirements
 * will be met: http://www.gnu.org/licenses/lgpl.html.
 *
 * If you have questions regarding the use of this file, please contact
 * Maishapay developers at contact@maishapay.online.
 */

package com.maishapay.smssync.presentation.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.maishapay.smssync.R;
import com.maishapay.smssync.data.PrefsFactory;
import com.maishapay.smssync.data.cache.FileManager;
import com.maishapay.smssync.presentation.App;
import com.maishapay.smssync.presentation.presenter.AlertPresenter;
import com.maishapay.smssync.presentation.service.AutoSyncScheduledService;
import com.maishapay.smssync.presentation.service.CheckTaskService;
import com.maishapay.smssync.presentation.util.Utility;

/**
 * The manifest Receiver is used to detect changes in battery state. When the system broadcasts a
 * "Battery Low" warning we turn off and stop all enabled services. When the system broadcasts
 * "Battery OK" to indicate the battery has returned to an okay state, we start all enabled
 * services
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public class PowerStateChangedReceiver extends BroadcastReceiver {

    private Intent mSmsSyncAutoSyncServiceIntent;

    private Intent mSmsSyncTaskCheckServiceIntent;

    @Override
    public void onReceive(final Context context, Intent intent) {
        boolean mBatteryLow = intent.getAction().equals(Intent.ACTION_BATTERY_LOW);
        boolean batteryOkay = intent.getAction().equals(Intent.ACTION_BATTERY_OKAY);

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        final int percentage = Utility.calculateBatteryLevel(level, scale);
        PrefsFactory prefsFactory = App.getAppComponent().prefsFactory();
        FileManager fileManager = App.getAppComponent().fileManager();
        AlertPresenter alertPresenter = App.getAppComponent().alertPresenter();
        if (mBatteryLow) {
            // is smssync service enabled
            fileManager.append(context.getString(R.string.battery_low));
            if (prefsFactory.serviceEnabled().get()) {
                Utility.clearAll(context);
                // Stop the service that pushes pending messages
                if (prefsFactory.enableAutoSync().get()) {
                    mSmsSyncAutoSyncServiceIntent = new Intent(context,
                            AutoSyncScheduledService.class);
                    context.stopService(mSmsSyncAutoSyncServiceIntent);
                }
                // Stop the service that checks for tasks
                if (prefsFactory.enableTaskCheck().get()) {
                    mSmsSyncTaskCheckServiceIntent = new Intent(context, CheckTaskService.class);
                    context.stopService(mSmsSyncTaskCheckServiceIntent);
                }
            }

            new Thread(() -> alertPresenter.lowBatteryLevelRequest(percentage)).start();
        }

        if (batteryOkay) {
            fileManager.append(context.getString(R.string.battery_okay));
            // is smssync enabled
            if (prefsFactory.serviceEnabled().get()) {

                // clear all notifications
                Utility.clearNotify(context);

                if (prefsFactory.enableAutoSync().get()) {
                    mSmsSyncAutoSyncServiceIntent = new Intent(context,
                            AutoSyncScheduledService.class);
                    AutoSyncScheduledService
                            .sendWakefulWork(context, mSmsSyncAutoSyncServiceIntent);
                }

                if (prefsFactory.enableTaskCheck().get()) {
                    mSmsSyncTaskCheckServiceIntent = new Intent(context, CheckTaskService.class);
                    CheckTaskService.sendWakefulWork(context, mSmsSyncTaskCheckServiceIntent);
                }
            }
        }
    }
}
