package com.colavo.android.entity.salon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by RUS on 17.07.2016.
 */
class SalonEntity(id: String = "",
                  @SerializedName("name") @Expose override var name: String = "",
                  @SerializedName("lastEventId") @Expose var lastEventId: String? = null) : BaseSalon(id, name), Serializable