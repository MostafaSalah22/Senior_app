package com.project.domain.repo

interface DataStoreRepoInterface {

    suspend fun saveToDataStore(key:String, value:String)
    suspend fun readFromDataStore(key:String): String?
}