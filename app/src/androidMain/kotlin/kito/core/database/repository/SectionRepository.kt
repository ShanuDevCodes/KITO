package com.kito.core.database.repository

import com.kito.core.database.dao.SectionDAO
import com.kito.core.database.entity.SectionEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SectionRepository @Inject constructor(
    private val sectionDao: SectionDAO
) {
    suspend fun insertSection(sectionEntity: List<SectionEntity>) =
        sectionDao.insertSection(sectionEntity)

    suspend fun deleteSection(sectionEntity: SectionEntity) =
        sectionDao.deleteSection(sectionEntity)

    suspend fun deleteAllSection() =
        sectionDao.deleteAllSection()
}
