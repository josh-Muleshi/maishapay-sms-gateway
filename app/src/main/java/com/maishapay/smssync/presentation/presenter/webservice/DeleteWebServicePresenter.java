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

package com.maishapay.smssync.presentation.presenter.webservice;

import android.support.annotation.NonNull;

import com.addhen.android.raiburari.domain.exception.DefaultErrorHandler;
import com.addhen.android.raiburari.domain.exception.ErrorHandler;
import com.addhen.android.raiburari.domain.usecase.DefaultSubscriber;
import com.addhen.android.raiburari.presentation.presenter.Presenter;
import com.maishapay.smssync.domain.usecase.webservice.DeleteWebServiceUsecase;
import com.maishapay.smssync.presentation.exception.ErrorMessageFactory;
import com.maishapay.smssync.presentation.view.webservice.DeleteWebServiceView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class DeleteWebServicePresenter implements Presenter {

    private final DeleteWebServiceUsecase mDeleteWebServiceUsecase;

    private DeleteWebServiceView mDeleteWebServiceView;

    /**
     * Default constructor
     *
     * @param deleteWebServiceUsecase The delete deployment use case
     */
    @Inject
    public DeleteWebServicePresenter(
            @Named("webServiceDelete") DeleteWebServiceUsecase deleteWebServiceUsecase) {
        mDeleteWebServiceUsecase = deleteWebServiceUsecase;
    }

    @Override
    public void resume() {
        // Do nothing
    }

    @Override
    public void pause() {
        // Do nothing
    }

    @Override
    public void destroy() {
        mDeleteWebServiceUsecase.unsubscribe();
    }

    public void setView(@NonNull DeleteWebServiceView deleteWebServiceView) {
        mDeleteWebServiceView = deleteWebServiceView;
    }

    /**
     * Deletes a web service from db
     *
     * @param webServiceId The deployment ID to be used for deletion
     */
    public void deleteWebService(Long webServiceId) {
        mDeleteWebServiceUsecase.setWebServiceEntityId(webServiceId);
        mDeleteWebServiceUsecase.execute(new DefaultSubscriber<Long>() {
            @Override
            public void onCompleted() {
                mDeleteWebServiceView.hideLoading();
            }

            @Override
            public void onNext(Long row) {
                mDeleteWebServiceView.onWebServiceDeleted();
            }

            @Override
            public void onError(Throwable e) {
                mDeleteWebServiceView.hideLoading();
                showErrorMessage(new DefaultErrorHandler((Exception) e));
            }
        });

    }

    private void showErrorMessage(ErrorHandler errorHandler) {
        String errorMessage = ErrorMessageFactory.create(mDeleteWebServiceView.getAppContext(),
                errorHandler.getException());
        mDeleteWebServiceView.showError(errorMessage);
    }
}
