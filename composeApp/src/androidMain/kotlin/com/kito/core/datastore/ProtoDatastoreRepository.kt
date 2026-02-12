package com.kito.core.datastore

import android.content.Context
import kotlinx.collections.immutable.PersistentList
class ProtoDatastoreRepository(
    private val context: Context
) {
    private val dataStore = ProtoDataStoreProvider.get(context)

    val studentSectionsFlow = dataStore.data

    suspend fun setSections(
        list: PersistentList<StudentSectionDatastore>
    ) {
        dataStore.updateData {
            it.copy(list = list.toList())
        }
    }

    suspend fun setRollNo(rollNo: String) {
        dataStore.updateData {
            it.copy(rollNo = rollNo)
        }
    }

    suspend fun clearAll() {
        dataStore.updateData {
            ProtoDataStoreDTO()
        }
    }
}
