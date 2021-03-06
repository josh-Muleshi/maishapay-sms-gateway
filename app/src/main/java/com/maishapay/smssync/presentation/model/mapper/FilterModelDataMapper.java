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

package com.maishapay.smssync.presentation.model.mapper;

import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;
import com.maishapay.smssync.domain.entity.FilterEntity;
import com.maishapay.smssync.presentation.model.FilterModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Henry Addo
 */
@ActivityScope
public class FilterModelDataMapper {

    @Inject
    public FilterModelDataMapper() {
        // Do nothing
    }

    /**
     * Maps {@link FilterModel} to {@link FilterEntity}
     *
     * @param filter The {@link FilterModel} to be
     *               mapped
     * @return The {@link FilterModel} entity
     */
    public FilterEntity map(FilterModel filter) {
        FilterEntity filterEntity = null;

        if (filter != null) {
            filterEntity = new FilterEntity();
            filterEntity._id = filter._id;
            filterEntity.setPhoneNumber(filter.getPhoneNumber());
            filterEntity.setStatus(FilterEntity.Status.valueOf(filter.getStatus().name()));
        }

        return filterEntity;
    }

    public FilterModel map(FilterEntity filterEntity) {
        FilterModel filter = null;

        if (filterEntity != null) {
            filter = new FilterModel();
            filter._id = filterEntity._id;
            filter.setPhoneNumber(filterEntity.getPhoneNumber());
            filter.setStatus(FilterModel.Status.valueOf(filterEntity.getStatus().name()));
        }
        return filter;
    }

    /**
     * Maps a list {@link FilterModel} into a list of {@link FilterEntity}.
     *
     * @param filterList List to be mapped.
     * @return {@link FilterModel}
     */
    public List<FilterModel> map(List<FilterEntity> filterList) {
        List<FilterModel> filterModelList = new ArrayList<>();
        FilterModel filterModel;
        for (FilterEntity filter : filterList) {
            filterModel = map(filter);
            if (filterModel != null) {
                filterModelList.add(filterModel);
            }
        }
        return filterModelList;
    }

    public FilterModel.Status map(FilterEntity.Status status) {
        return FilterModel.Status.valueOf(status.name());
    }

    public FilterEntity.Status map(FilterModel.Status status) {
        return FilterEntity.Status.valueOf(status.name());
    }
}
