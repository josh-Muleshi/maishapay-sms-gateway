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

package com.maishapay.smssync.presentation.util;

/**
 * Created by Kamil Kalfas(kkalfas@soldevelo.com) on 20.05.14.
 */
public class TimeFrequencyUtil {

    public static final String DEFAULT_TIME_FREQUENCY = "00:05";

    private static final int ONE_HOUR = 3600000;

    private static final int ONE_MINUTE = 60000;

    private TimeFrequencyUtil() {
    }

    public static long calculateInterval(String time) {
        String[] pieces = time.split(":");
        return Integer.parseInt(pieces[0]) * ONE_HOUR +
                Integer.parseInt(pieces[1]) * ONE_MINUTE;
    }
}
