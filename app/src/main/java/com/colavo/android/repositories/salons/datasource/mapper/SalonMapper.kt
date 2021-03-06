package com.colavo.android.repositories.salons.datasource.mapper

import com.colavo.android.entity.event.EventEntity
import com.colavo.android.entity.salon.BaseSalon
import com.colavo.android.entity.salon.SalonEntity
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.entity.session.User
import com.colavo.android.utils.toChatTime
import org.joda.time.DateTime

/**
 * Created by RUS on 30.07.2016.
 */
class SalonMapper {

    companion object {

        fun createSalonWithEventAndUser(baseSalon: BaseSalon, user: User?): SalonModel { //eventEntity: EventEntity?,
            val salonModel = SalonModel()
            salonModel.id = baseSalon.id
            salonModel.name = baseSalon.name
            salonModel.address = baseSalon.address
            salonModel.owner_uid = baseSalon.owner_uid
//todo

            if (user != null) {
                    salonModel.owner = user.name //todo address lastEventUser
            }
            return salonModel

        }

            fun transformFromEntity(salonEntity: SalonEntity, user: User?): SalonModel {
                val salonModel = SalonModel()
                salonModel.id = salonEntity.id
                salonModel.name = salonEntity.name
                salonModel.address = salonEntity.address
                salonModel.owner_uid = salonEntity.owner_uid

                if (user != null) {
                    if (true)
                        salonModel.owner = user.name //todo address lastEventUser
                }
                return salonModel
            }


    }

}