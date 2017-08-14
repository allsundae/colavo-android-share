package com.colavo.android.interactors.session

import com.colavo.android.entity.query.session.SessionQuery
import com.colavo.android.entity.session.User
import com.colavo.android.interactors.UseCase
import com.colavo.android.repositories.session.SessionRepository
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by RUS on 01.08.2016.
 */
@UseCase
class Register @Inject constructor(sessionRepository: SessionRepository) : SessionUseCase(sessionRepository) {

    fun execute(email: String, name: String, password: String, subscriber: Subscriber<User>)
            = super.execute(SessionQuery.Register(email, name, password), subscriber)

}