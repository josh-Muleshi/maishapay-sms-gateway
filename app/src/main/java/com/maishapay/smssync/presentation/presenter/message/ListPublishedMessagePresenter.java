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

package com.maishapay.smssync.presentation.presenter.message;

import android.support.annotation.NonNull;

import com.addhen.android.raiburari.domain.exception.DefaultErrorHandler;
import com.addhen.android.raiburari.domain.exception.ErrorHandler;
import com.addhen.android.raiburari.domain.usecase.DefaultSubscriber;
import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.addhen.android.raiburari.presentation.presenter.Presenter;
import com.maishapay.smssync.domain.entity.MessageEntity;
import com.maishapay.smssync.domain.usecase.message.ListPublishedMessageUsecase;
import com.maishapay.smssync.presentation.exception.ErrorMessageFactory;
import com.maishapay.smssync.presentation.model.mapper.MessageModelDataMapper;
import com.maishapay.smssync.presentation.view.message.ListMessageView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Maishapay Team <online@maishapay.online>
 */
@ActivityScope
public class ListPublishedMessagePresenter implements Presenter {

    private final ListPublishedMessageUsecase mListPublishedMessageUsecase;

    private final MessageModelDataMapper mMessageModelDataMapper;

    private ListMessageView mListMessageView;


    @Inject
    public ListPublishedMessagePresenter(
            @Named("messagePublishList") ListPublishedMessageUsecase listPublishedMessageUsecase,
            MessageModelDataMapper messageModelDataMapper) {
        mListPublishedMessageUsecase = listPublishedMessageUsecase;
        mMessageModelDataMapper = messageModelDataMapper;
    }

    @Override
    public void resume() {
        loadMessages();
    }

    @Override
    public void pause() {
        // Do nothing
    }

    @Override
    public void destroy() {
        mListPublishedMessageUsecase.unsubscribe();
    }

    public void setView(@NonNull ListMessageView listMessageView) {
        mListMessageView = listMessageView;
    }

    public void loadMessages() {
        mListMessageView.hideRetry();
        mListMessageView.showLoading();
        mListPublishedMessageUsecase.execute(new PublishedMessageSubscriber());
    }

    private void showErrorMessage(ErrorHandler errorHandler) {
        String errorMessage = ErrorMessageFactory.create(mListMessageView.getAppContext(),
                errorHandler.getException());
        mListMessageView.showError(errorMessage);
    }

    private class PublishedMessageSubscriber extends DefaultSubscriber<List<MessageEntity>> {

        @Override
        public void onStart() {
            mListMessageView.hideRetry();
            mListMessageView.showLoading();
        }

        @Override
        public void onCompleted() {
            mListMessageView.hideLoading();
        }

        @Override
        public void onNext(List<MessageEntity> filterList) {
            mListMessageView.hideLoading();
            mListMessageView.showMessages(mMessageModelDataMapper.map(filterList));
        }

        @Override
        public void onError(Throwable e) {
            mListMessageView.hideLoading();
            showErrorMessage(new DefaultErrorHandler((Exception) e));
            mListMessageView.showRetry();
        }
    }
}
