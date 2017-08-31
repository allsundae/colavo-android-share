package com.colavo.android.entity.salon

import java.io.Serializable

/**
 * Created by RUS on 30.07.2016.
 */
class SalonModel(id: String = "",
                 name: String = "",
                 address: String = "",
                 var lastEvent: String = "",
                 var lastEventTime: String = "") : BaseSalon(id, name, address), Serializable