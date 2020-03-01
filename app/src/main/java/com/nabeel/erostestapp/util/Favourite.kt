package com.nabeel.erostestapp.util

import com.nabeel.erostestapp.model.Result

interface Favourite {
    fun add(result: Result)

    fun remove(rank: Int)
}