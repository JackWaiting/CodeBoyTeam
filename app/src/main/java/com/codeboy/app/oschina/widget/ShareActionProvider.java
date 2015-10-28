/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.codeboy.app.oschina.widget;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.View;

/**
 * Create Date 2014/8/16<br>
 * Author LiWenLong<br>
 * Update Date 2014/8/16 23:40<br>
 * Last Update longlong<br>
 * Description
 */
public class ShareActionProvider extends ActionProvider {

    private final Context mContext;

    public ShareActionProvider(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

}
