/***
 Copyright (c) 2017 CommonsWare, LLC
 Licensed under the Apache License, Version 2.0 (the "License"); you may not
 use this file except in compliance with the License. You may obtain a copy
 of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 by applicable law or agreed to in writing, software distributed under the
 License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 OF ANY KIND, either express or implied. See the License for the specific
 language governing permissions and limitations under the License.

 Covered in detail in the book _Android's Architecture Components_
 https://commonsware.com/AndroidArch
 */

package com.commonsware.android.todo.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import io.reactivex.Single;

class FilterModeRepository {
  private static final String PREF_MODE="filterMode";
  private static volatile FilterModeRepository INSTANCE=null;
  private SharedPreferences prefs=null;

  synchronized static FilterModeRepository get() {
    if (INSTANCE==null) {
      INSTANCE=new FilterModeRepository();
    }

    return(INSTANCE);
  }

  Single<FilterMode> load(Context ctxt) {
    final Context app=ctxt.getApplicationContext();

    return(Single.create(e -> {
      synchronized(this) {
        if (prefs==null) {
          prefs=app.getSharedPreferences(getClass().getCanonicalName(),
            Context.MODE_PRIVATE);
        }
      }

      e.onSuccess(FilterMode.forValue(prefs.getInt(PREF_MODE, 0)));
    }));
  }

  @SuppressLint("ApplySharedPref")
  void save(FilterMode mode) {
    prefs.edit().putInt(PREF_MODE, mode.getValue()).commit();
  }
}
