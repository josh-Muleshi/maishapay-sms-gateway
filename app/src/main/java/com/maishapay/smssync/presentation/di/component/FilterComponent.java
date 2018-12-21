/*
 * Copyright (c) 2010 - 2015 Ushahidi Inc
 * All rights reserved
 * Contact: team@ushahidi.com
 * Website: http://www.ushahidi.com
 * GNU Lesser General Public License Usage
 * This file may be used under the terms of the GNU Lesser
 * General Public License version 3 as published by the Free Software
 * Foundation and appearing in the file LICENSE.LGPL included in the
 * packaging of this file. Please review the following information to
 * ensure the GNU Lesser General Public License version 3 requirements
 * will be met: http://www.gnu.org/licenses/lgpl.html.
 *
 * If you have questions regarding the use of this file, please contact
 * Ushahidi developers at team@ushahidi.com.
 */

package com.maishapay.smssync.presentation.di.component;

import com.addhen.android.raiburari.presentation.di.module.ActivityModule;
import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.maishapay.smssync.domain.usecase.filter.DeleteFilterUsecase;
import com.maishapay.smssync.presentation.di.module.FilterModule;
import com.maishapay.smssync.presentation.presenter.filter.AddFilterPresenter;
import com.maishapay.smssync.presentation.presenter.filter.ListFilterPresenter;
import com.maishapay.smssync.presentation.presenter.webservice.UpdateWebServiceKeywordsPresenter;
import com.maishapay.smssync.presentation.view.ui.fragment.AddPhoneNumberFilterFragment;
import com.maishapay.smssync.presentation.view.ui.fragment.FilterFragment;

import dagger.Component;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, FilterModule.class})
public interface FilterComponent extends AppActivityComponent {

    void inject(FilterFragment filterFragments);

    void inject(AddPhoneNumberFilterFragment filterFragment);

    ListFilterPresenter listFilterPresenter();

    AddFilterPresenter addFilterPresenter();

    DeleteFilterUsecase deleteFilterPresenter();

    UpdateWebServiceKeywordsPresenter updateWebServiceKeywordsPrestenter();
}