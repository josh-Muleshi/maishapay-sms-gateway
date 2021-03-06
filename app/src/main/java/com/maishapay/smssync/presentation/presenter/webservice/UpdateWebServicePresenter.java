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
import com.maishapay.smssync.domain.usecase.webservice.TestWebServiceUsecase;
import com.maishapay.smssync.domain.usecase.webservice.UpdateWebServiceUsecase;
import com.maishapay.smssync.presentation.exception.ErrorMessageFactory;
import com.maishapay.smssync.presentation.model.WebServiceModel;
import com.maishapay.smssync.presentation.model.mapper.WebServiceModelDataMapper;
import com.maishapay.smssync.presentation.view.webservice.TestWebServiceView;
import com.maishapay.smssync.presentation.view.webservice.UpdateWebServiceView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class UpdateWebServicePresenter implements Presenter {

    private final UpdateWebServiceUsecase mUpdateWebServiceUsecase;

    private final WebServiceModelDataMapper mWebServiceModelDataMapper;
    private final TestWebServiceUsecase mTestWebServiceUsecase;
    private UpdateWebServiceView mUpdateWebServiceView;
    private TestWebServiceView mTestWebServiceView;

    /**
     * Default use case.
     *
     * @param updateWebServiceUsecase   The update deployment use case
     * @param deploymentModelDataMapper The deployment model data mapper
     */
    @Inject
    public UpdateWebServicePresenter(
            @Named("webServiceUpdate") UpdateWebServiceUsecase updateWebServiceUsecase,
            WebServiceModelDataMapper deploymentModelDataMapper,
            @Named("testWebService") TestWebServiceUsecase testWebServiceUsecase) {
        mUpdateWebServiceUsecase = updateWebServiceUsecase;
        mWebServiceModelDataMapper = deploymentModelDataMapper;
        mTestWebServiceUsecase = testWebServiceUsecase;
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
        mUpdateWebServiceUsecase.unsubscribe();
        mTestWebServiceUsecase.unsubscribe();
    }

    public void setView(@NonNull UpdateWebServiceView addWebServiceView) {
        mUpdateWebServiceView = addWebServiceView;
    }

    public void setTestWebServiceView(@NonNull TestWebServiceView testWebServiceView) {
        mTestWebServiceView = testWebServiceView;
    }

    /**
     * Updates {@link WebServiceModel}
     *
     * @param deploymentModel The deployment model to be updated
     */
    public void updateWebService(WebServiceModel deploymentModel) {
        mUpdateWebServiceView.hideRetry();
        mUpdateWebServiceView.showLoading();
        mUpdateWebServiceUsecase.setWebServiceEntity(
                mWebServiceModelDataMapper.map(deploymentModel));
        mUpdateWebServiceUsecase.execute(new DefaultSubscriber<Long>() {
            @Override
            public void onCompleted() {
                mUpdateWebServiceView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mUpdateWebServiceView.hideLoading();
                showErrorMessage(new DefaultErrorHandler((Exception) e));
                mUpdateWebServiceView.showRetry();
            }

            @Override
            public void onNext(Long row) {
                mUpdateWebServiceView.onWebServiceSuccessfullyUpdated(row);
            }
        });
    }

    public void testWebService(WebServiceModel webServiceModel) {
        mTestWebServiceUsecase.setWebServiceEntity(mWebServiceModelDataMapper.map(webServiceModel));
        mTestWebServiceUsecase.execute(new DefaultSubscriber<Boolean>() {
            @Override
            public void onStart() {
                mTestWebServiceView.showLoading();
            }

            @Override
            public void onCompleted() {
                mTestWebServiceView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mTestWebServiceView.hideLoading();
                showErrorMessage(new DefaultErrorHandler((Exception) e));
                mTestWebServiceView.showRetry();
            }

            @Override
            public void onNext(Boolean status) {
                mTestWebServiceView.webServiceTested(status);
            }
        });
    }

    private void showErrorMessage(ErrorHandler errorHandler) {
        String errorMessage = ErrorMessageFactory.create(mUpdateWebServiceView.getAppContext(),
                errorHandler.getException());
        mUpdateWebServiceView.showError(errorMessage);
    }
}