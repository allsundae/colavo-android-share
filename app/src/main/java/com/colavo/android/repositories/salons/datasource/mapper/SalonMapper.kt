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

        fun createSalonWithEventAndUser(baseSalon: BaseSalon, eventEntity: EventEntity?, user: User?): SalonModel {
            val salonModel = SalonModel()
            salonModel.id = baseSalon.id
            salonModel.name = baseSalon.name
            salonModel.address = baseSalon.address

            if(eventEntity != null) {
                salonModel.lastEvent = eventEntity.text
                if(!eventEntity.time.equals("")) {
                    salonModel.lastEventTime = DateTime.parse(eventEntity.time).toChatTime()
                }
            }
            if(user != null) {
                if(user.name != null)
                    salonModel.address = user.name //todo address lastEventUser
            }
            return salonModel
        }

        fun transformFromEntity(salonEntity: SalonEntity): SalonModel {
            val salonModel = SalonModel()
            salonModel.id = salonEntity.id
            salonModel.name = salonEntity.name
            salonModel.address = salonEntity.address
            return salonModel
        }

    }

}