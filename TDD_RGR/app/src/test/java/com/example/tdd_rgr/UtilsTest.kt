package com.example.tdd_rgr

import org.junit.Test
import org.junit.Assert.*

class UtilsTest {

    @Test
    fun testNullQuery() {
        val nullResult = getSearchUrl(null)
        if (nullResult == null) {
            println("success")
        } else {
            println("error")
        }
    }

    fun getSearchUrl(query: String?): String? {
        return null
    }
}