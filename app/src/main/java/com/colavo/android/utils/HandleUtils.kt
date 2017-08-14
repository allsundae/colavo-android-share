package com.colavo.android.utils

import com.colavo.android.entity.query.BaseQuery
import com.colavo.android.entity.query.Handle
import com.colavo.android.repositories.BaseRepository
import rx.Observable

/**
 * Created by RUS on 13.07.2016.
 */
class HandleUtils {

    companion object {

        fun registerHandlers(repository: BaseRepository, dataSource: Any) {
            for(clazz in dataSource.javaClass.interfaces) {
                for(method in clazz.declaredMethods) {
                    val handle = method.getAnnotation(Handle::class.java)
                    repository.queryHandlers.put(handle.value.java, object : BaseRepository.QueryHandler {
                        override fun <T> handleQuery(query: BaseQuery): Observable<T> = method.invoke(dataSource, query) as Observable<T>
                    })
                }
            }
        }

    }

}