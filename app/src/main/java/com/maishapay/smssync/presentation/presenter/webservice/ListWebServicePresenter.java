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
import com.maishapay.smssync.domain.entity.WebServiceEntity;
import com.maishapay.smssync.domain.usecase.webservice.ListWebServiceUsecase;
import com.maishapay.smssync.presentation.exception.ErrorMessageFactory;
import com.maishapay.smssync.presentation.model.mapper.WebServiceModelDataMapper;
import com.maishapay.smssync.presentation.view.webservice.ListWebServiceView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
public class ListWebServicePresenter implements
        Presenter {

    private final ListWebServiceUsecase mUsecase;

    private final WebServiceModelDataMapper mWebServiceModelDataMapper;

    private ListWebServiceView mListWebServiceView;

    /**
     * Default constructor
     *
     * @param usecase                   The list webService use case
     * @param webServiceModelDataMapper The webService model data mapper
     */
    @Inject
    public ListWebServicePresenter(@Named("webServiceList") ListWebServiceUsecase usecase,
                                   WebServiceModelDataMapper webServiceModelDataMapper) {
        mUsecase = usecase;
        mWebServiceModelDataMapper = webServiceModelDataMapper;
    }

    @Override
    public void resume() {
        loadWebServices();
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        mUsecase.unsubscribe();
    }

    public void setView(@NonNull ListWebServiceView listWebServiceView) {
        mListWebServiceView = listWebServiceView;
    }

    /**
     * Gets webService list from storage
     */
    public void loadWebServices() {
        mListWebServiceView.hideRetry();
        mListWebServiceView.showLoading();
        mUsecase.execute(new DefaultSubscriber<List<WebServiceEntity>>() {
            @Override
            public void onCompleted() {
                mListWebServiceView.hideLoading();
            }

            @Override
            public void onNext(List<WebServiceEntity> webServiceList) {
                mListWebServiceView.hideLoading();
                mListWebServiceView.renderWebServiceList(
                        mWebServiceModelDataMapper.map(webServiceList));
            }

            @Override
            public void onError(Throwable e) {
                mListWebServiceView.hideLoading();
                showErrorMessage(new DefaultErrorHandler((Exception) e));
                mListWebServiceView.showRetry();
            }
        });
    }

    private void showErrorMessage(ErrorHandler errorHandler) {
        String errorMessage = ErrorMessageFactory.create(mListWebServiceView.getAppContext(),
                errorHandler.getException());
        mListWebServiceView.showError(errorMessage);
    }
}
