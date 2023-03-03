package com.example.newsapp.ui

//to hide navigation drawer from fragment you want to hide
interface DrawerLocker {
    fun setDrawerLocked(shouldLock: kotlin.Boolean)
}