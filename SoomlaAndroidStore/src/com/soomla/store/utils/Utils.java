/*
 * Copyright (C) 2012 Soomla Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soomla.store.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
//import com.soomla.store.R;
import com.soomla.store.SoomlaConsts;

import java.util.Locale;

public class Utils {

//    /**
//     * Creates a dialog.
//     * @param activity is the activity that's related to the dialog.
//     * @param id is the id of the dialog to return.
//     * @return the dialog to be shown.
//     */
//    public static Dialog createDialog(Activity activity, int id) {
//        int titleId, messageId;
//        switch (id) {
//            case SoomlaConsts.DIALOG_CANNOT_CONNECT_ID:
//                titleId =   R.string.cannot_connect_title;
//                messageId = R.string.cannot_connect_message;
//                break;
//            case SoomlaConsts.DIALOG_BILLING_NOT_SUPPORTED_ID:
//                titleId =   R.string.billing_not_supported_title;
//                messageId = R.string.billing_not_supported_message;
//                break;
//            case SoomlaConsts.DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID:
//                titleId =   R.string.subscriptions_not_supported_title;
//                messageId = R.string.subscriptions_not_supported_message;
//                break;
//            default:
//                Log.d(TAG, "unrecognized dialog id given: " + id);
//                return null;
//        }
//
//        String helpUrl = replaceLanguageAndRegion(activity.getString(R.string.help_url));
//        final Uri helpUri = Uri.parse(helpUrl);
//        final Activity tmpActivity = activity;
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(tmpActivity);
//        builder.setTitle(titleId)
//                .setIcon(android.R.drawable.stat_sys_warning)
//                .setMessage(messageId)
//                .setCancelable(false)
//                .setPositiveButton(android.R.string.ok, null)
//                .setNegativeButton(R.string.learn_more, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, helpUri);
//                        tmpActivity.startActivity(intent);
//                    }
//                });
//        return builder.create();
//    }
//
//    /**
//     * Replaces the language and/or country of the device into the given string.
//     * The pattern "%lang%" will be replaced by the device's language code and
//     * the pattern "%region%" will be replaced with the device's country code.
//     *
//     * @param str the string to replace the language/country within
//     * @return a string containing the local language and region codes
//     */
//    private static String replaceLanguageAndRegion(String str) {
//        // Substitute language and or region if present in string
//        if (str.contains("%lang%") || str.contains("%region%")) {
//            Locale locale = Locale.getDefault();
//            str = str.replace("%lang%", locale.getLanguage().toLowerCase());
//            str = str.replace("%region%", locale.getCountry().toLowerCase());
//        }
//        return str;
//    }
//
//
//    private static final String TAG = "SOOMLA Utils";
}
