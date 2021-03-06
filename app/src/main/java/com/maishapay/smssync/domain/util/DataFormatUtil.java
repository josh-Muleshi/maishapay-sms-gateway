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

package com.maishapay.smssync.domain.util;

/**
 * @author Maishapay Team <online@maishapay.online>
 */

import android.util.Xml;

import com.maishapay.smssync.domain.entity.HttpNameValuePair;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * Class: DataFormatUtil Description: Serialize sync data in appropriate format. Author: Salama
 * A.B.
 * <devaksal@gmail.com>
 */
public class DataFormatUtil {

    public static String makeJSONString(List<HttpNameValuePair> pairs) throws JSONException {
        JSONObject obj = new JSONObject();

        for (HttpNameValuePair pair : pairs) {
            obj.put(pair.getName(), pair.getValue());
        }

        return obj.toString();
    }

    public static String makeXMLString(List<HttpNameValuePair> pairs, String parentNode,
                                       String charset)
            throws IOException {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        serializer.setOutput(writer);
        serializer.startDocument(charset, true);
        serializer.startTag("", parentNode);
        for (HttpNameValuePair pair : pairs) {
            serializer.startTag("", pair.getName());
            serializer.text(pair.getValue());
            serializer.endTag("", pair.getName());
        }
        serializer.endTag("", parentNode);
        serializer.endDocument();
        return writer.toString();
    }
}
