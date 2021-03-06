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

package com.maishapay.smssync.data.repository.datasource.webservice;

import com.maishapay.smssync.data.entity.SyncUrl;

import java.util.List;

import rx.Observable;

/**
 * All different source providers must implement this interface to provide deployment data
 *
 * @author Maishapay Team <online@maishapay.online>
 */
public interface WebServiceDataSource {

    /**
     * Get an {@link Observable} which will emit a List of {@link SyncUrl}.
     *
     * @return Observable that emits list of {@link SyncUrl}
     */
    Observable<List<SyncUrl>> getWebServiceList();

    /**
     * Get an {@link Observable} which will emit a {@link SyncUrl} by its id.
     *
     * @param deploymentId The id to retrieve user data.
     * @return Observable that emits a {@link SyncUrl}
     */
    Observable<SyncUrl> getWebService(Long deploymentId);

    /**
     * Gets an {@link Observable} which will emit a {@link SyncUrl} by its status.
     *
     * @param status The deployment status to be used for retrieving a deployment
     * @return Observable that emits a list of {@link SyncUrl}
     */
    Observable<List<SyncUrl>> getByStatus(SyncUrl.Status status);

    /**
     * Gets a list of {@link SyncUrl} by its status.
     *
     * @param status The deployment status to be used for retrieving a deployment
     * @return A list of {@link SyncUrl}
     */
    List<SyncUrl> syncGetByStatus(SyncUrl.Status status);

    /**
     * Adds an {@link SyncUrl} to storage and then returns an {@link Observable} for all
     * subscribers
     * to react to it.
     *
     * @param deployment The deployment to be added
     * @return The row affected
     */
    Observable<Long> addWebService(SyncUrl deployment);

    /**
     * Adds an {@link SyncUrl} to storage and then returns an {@link Observable} for all
     * subscribers
     * to react to it.
     *
     * @param deployment The deployment to be added
     * @return The row affected
     */
    Observable<Long> updateWebService(SyncUrl deployment);

    /**
     * Deletes an {@link SyncUrl} from storage and then returns an {@link Observable} for
     * all
     * subscribers to react to it.
     *
     * @param deploymentId The deployment to be deleted
     * @return The row affected
     */
    Observable<Long> deleteWebService(Long deploymentId);

    List<SyncUrl> get(final SyncUrl.Status status);

    List<SyncUrl> listWebServices();
}
