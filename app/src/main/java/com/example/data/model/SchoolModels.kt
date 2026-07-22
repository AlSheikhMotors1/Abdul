package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    PARENT_STUDENT,
    TEACHER,
    ADMIN
}

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val fatherName: String,
    val className: String, // e.g. "Class 1", "Class 2", "Play", "Nursery", "Prep"
    val section: String = "A",
    val rollNumber: String,
    val photoUrl: String = "",
    val feeCleared: Boolean = true,
    val feePendingAmount: Double = 0.0,
    val feeDueDate: String = "10th of every month",
    val teacherRemarks: String = "Consistent performance, active participant in class activities."
)

@Entity(tableName = "teachers")
data class Teacher(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val passcode: String = "1234",
    val photoUrl: String = ""
)

@Entity(tableName = "teacher_assignments")
data class TeacherAssignment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val teacherId: Int,
    val className: String,
    val subjectName: String
)

@Entity(tableName = "homework_entries")
data class HomeworkEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val className: String,
    val subjectName: String,
    val contentType: String, // "HOMEWORK", "DAILY_DIARY", "CLASSWORK", "ASSIGNMENT", "TEACHER_NOTES"
    val chapter: String,
    val content: String,
    val authorTeacherName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val dateFormatted: String = "Today"
)

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val date: String,
    val month: String,
    val year: String = "2026",
    val status: String // "PRESENT", "ABSENT", "LEAVE"
)

@Entity(tableName = "result_records")
data class ResultRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val term: String, // "MONTHLY_TEST", "MID_TERM", "FINAL_EXAM"
    val subjectName: String,
    val totalMarks: Int = 100,
    val obtainedMarks: Int,
    val teacherRemarks: String = "Good effort!"
)

@Entity(tableName = "school_notices")
data class SchoolNotice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // "SCHOOL_TIMING", "HOLIDAYS", "NOTICES", "WEEKLY_UPDATES", "MONTHLY_EVENTS", "ANNUAL_FUNCTIONS", "EXAM_SCHEDULE", "PARENT_MEETINGS", "EMERGENCY_NOTICES"
    val title: String,
    val description: String,
    val date: String,
    val isEmergency: Boolean = false
)

@Entity(tableName = "school_notifications")
data class SchoolNotification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val category: String = "General",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "gallery_items")
data class SchoolGalleryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val imageUrl: String,
    val date: String = "2026"
)

@Entity(tableName = "school_info")
data class SchoolInfo(
    @PrimaryKey val id: Int = 1,
    val schoolName: String = "Sunrise School Lodhra",
    val tagline: String = "Excellence in Education - Lodhra, Pakistan",
    val phone: String = "03013662014",
    val whatsapp: String = "03013662014",
    val email: String = "info@sunriseschool.edu.pk",
    val address: String = "Main College Road, Lodhra, Punjab, Pakistan",
    val googleMapsUrl: String = "https://maps.google.com/?q=Lodhra+Pakistan",
    val schoolTiming: String = "Mon - Sat: 07:45 AM - 01:30 PM",
    val bannerText: String = "Welcome to Sunrise School Lodhra - Admissions Open for Session 2026-27!",
    val principalName: String = "Muhammad Aftab Ali",
    val principalMessage: String = "At Sunrise School Lodhra, we cultivate knowledge, discipline, and character to prepare bright leaders for Pakistan's future."
)
