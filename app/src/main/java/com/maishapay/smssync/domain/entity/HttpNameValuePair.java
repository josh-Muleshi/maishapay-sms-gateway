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

package com.maishapay.smssync.domain.entity;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class HttpNameValuePair {

    private String mName;

    private String mValue;

    public HttpNameValuePair(String name, String value) {
        mName = name;
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    public String getName() {
        return mName;
    }
}
