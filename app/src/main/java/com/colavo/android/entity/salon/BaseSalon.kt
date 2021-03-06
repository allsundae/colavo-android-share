package com.colavo.android.entity.salon

import java.io.Serializable

/**
 * Created by RUS on 30.07.2016.
 */
abstract class BaseSalon(
        var id: String,
        open var name: String,
        open var address: String,
        open var owner_uid: String
) : Serializable