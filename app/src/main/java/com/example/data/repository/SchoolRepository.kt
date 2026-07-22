package com.example.data.repository

import com.example.data.dao.SchoolDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class SchoolRepository(private val dao: SchoolDao) {

    // Students
    val allStudents: Flow<List<Student>> = dao.getAllStudents()

    suspend fun getStudentById(id: Int): Student? = dao.getStudentById(id)

    suspend fun verifyStudent(name: String, fatherName: String, className: String): Student? {
        return dao.verifyStudent(name.trim(), fatherName.trim(), className.trim())
    }

    suspend fun insertStudent(student: Student): Long = dao.insertStudent(student)
    suspend fun updateStudent(student: Student) = dao.updateStudent(student)
    suspend fun deleteStudent(student: Student) = dao.deleteStudent(student)

    // Teachers
    val allTeachers: Flow<List<Teacher>> = dao.getAllTeachers()
    suspend fun getTeacherById(id: Int): Teacher? = dao.getTeacherById(id)
    suspend fun insertTeacher(teacher: Teacher): Long = dao.insertTeacher(teacher)
    suspend fun deleteTeacher(teacher: Teacher) = dao.deleteTeacher(teacher)

    // Assignments
    val allTeacherAssignments: Flow<List<TeacherAssignment>> = dao.getAllTeacherAssignments()
    fun getAssignmentsForTeacher(teacherId: Int): Flow<List<TeacherAssignment>> =
        dao.getAssignmentsForTeacher(teacherId)

    suspend fun insertTeacherAssignment(assignment: TeacherAssignment) =
        dao.insertTeacherAssignment(assignment)

    suspend fun deleteTeacherAssignment(id: Int) = dao.deleteTeacherAssignment(id)

    // Homework
    fun getHomeworkForClassAndSubject(className: String, subjectName: String): Flow<List<HomeworkEntry>> =
        dao.getHomeworkForClassAndSubject(className, subjectName)

    val allHomeworkEntries: Flow<List<HomeworkEntry>> = dao.getAllHomeworkEntries()

    suspend fun insertHomework(entry: HomeworkEntry) {
        dao.insertHomework(entry)
        // Also send automatic notification to parents
        dao.insertNotification(
            SchoolNotification(
                title = "New ${entry.contentType.replace("_", " ")} Added",
                message = "${entry.authorTeacherName} updated ${entry.subjectName} for ${entry.className}: ${entry.chapter}",
                category = "Homework"
            )
        )
    }

    suspend fun deleteHomework(entry: HomeworkEntry) = dao.deleteHomework(entry)

    // Attendance
    val allAttendanceRecords: Flow<List<AttendanceRecord>> = dao.getAllAttendanceRecords()

    fun getAttendanceForStudent(studentId: Int): Flow<List<AttendanceRecord>> =
        dao.getAttendanceForStudent(studentId)

    fun getAttendanceForDate(date: String): Flow<List<AttendanceRecord>> =
        dao.getAttendanceForDate(date)

    suspend fun insertAttendance(record: AttendanceRecord) = dao.insertAttendance(record)

    suspend fun saveAttendanceList(records: List<AttendanceRecord>, absentStudentNames: List<String>) {
        dao.insertAttendanceList(records)
        if (absentStudentNames.isNotEmpty()) {
            val namesStr = absentStudentNames.joinToString(", ")
            dao.insertNotification(
                SchoolNotification(
                    title = "📢 Attendance Alert",
                    message = "Student(s) marked absent today: $namesStr. Please contact school if leave was requested.",
                    category = "Attendance"
                )
            )
        }
    }

    // Results
    fun getResultsForStudent(studentId: Int): Flow<List<ResultRecord>> =
        dao.getResultsForStudent(studentId)

    suspend fun insertResult(result: ResultRecord) = dao.insertResult(result)

    // Notices
    val allNotices: Flow<List<SchoolNotice>> = dao.getAllNotices()
    fun getNoticesByCategory(category: String): Flow<List<SchoolNotice>> =
        dao.getNoticesByCategory(category)

    suspend fun insertNotice(notice: SchoolNotice) {
        dao.insertNotice(notice)
        if (notice.isEmergency) {
            dao.insertNotification(
                SchoolNotification(
                    title = "🚨 EMERGENCY NOTICE",
                    message = notice.title + ": " + notice.description,
                    category = "Emergency"
                )
            )
        }
    }

    suspend fun deleteNotice(notice: SchoolNotice) = dao.deleteNotice(notice)

    // Notifications
    val allNotifications: Flow<List<SchoolNotification>> = dao.getAllNotifications()
    suspend fun sendPushNotification(title: String, message: String, category: String) {
        dao.insertNotification(
            SchoolNotification(
                title = title,
                message = message,
                category = category
            )
        )
    }

    // Gallery
    val allGalleryItems: Flow<List<SchoolGalleryItem>> = dao.getAllGalleryItems()
    suspend fun insertGalleryItem(item: SchoolGalleryItem) = dao.insertGalleryItem(item)

    // School Info
    val schoolInfo: Flow<SchoolInfo?> = dao.getSchoolInfo()
    suspend fun updateSchoolInfo(info: SchoolInfo) = dao.updateSchoolInfo(info)
}
