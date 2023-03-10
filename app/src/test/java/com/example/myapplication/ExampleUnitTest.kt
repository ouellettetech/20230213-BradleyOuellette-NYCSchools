package com.example.myapplication

import com.example.myapplication.Data.SchoolSATInfo
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val TestUnknownSchoolName = "Unknown School"
    val TestExampleSchoolName = "My High School"
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun toStringforSchool_isCorrect() {

        var schoolInfo: SchoolSATInfo = SchoolSATInfo()

        assertEquals(TestUnknownSchoolName,schoolInfo.toString())
        schoolInfo.school_name = TestExampleSchoolName
        assertEquals(TestExampleSchoolName, schoolInfo.school_name)
    }
}