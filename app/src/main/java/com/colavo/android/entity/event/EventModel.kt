package com.colavo.android.entity.event

import org.joda.time.DateTime

/**
 * Created by RUS on 02.08.2016.
 */
data class EventModel(var id: String,
                      var text: String,
                      var userName: String,
                      var time: String,
                      //var time: DateTime,
                      var isMine: Boolean)