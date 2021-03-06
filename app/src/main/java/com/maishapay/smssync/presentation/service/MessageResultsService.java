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

package com.maishapay.smssync.presentation.service;

import android.content.Intent;

import com.maishapay.smssync.R;
import com.maishapay.smssync.data.cache.FileManager;
import com.maishapay.smssync.data.message.ProcessMessageResult;

import javax.inject.Inject;

/**
 * @author Henry Addo
 */
public class MessageResultsService extends BaseWakefulIntentService {

    private static final String CLASS_TAG = MessageResultsService.class.getSimpleName();

    @Inject
    ProcessMessageResult mProcessMessageResult;

    @Inject
    FileManager mFileManager;

    public MessageResultsService() {
        super(CLASS_TAG);
    }

    public void onCreate() {
        super.onCreate();
        getComponent().inject(this);
    }

    @Override
    public void executeTask(Intent intent) {
        log(getString(R.string.checking_scheduled_message));
        mFileManager.append(getString(R.string.checking_scheduled_message));
        mProcessMessageResult.processMessageResult();
    }
}
