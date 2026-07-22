package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {
    // Students
    @Query("SELECT * FROM students ORDER BY className, name")
    fun getAllStudents(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    suspend fun getStudentById(id: Int): Student?

    @Query("SELECT * FROM students WHERE LOWER(name) LIKE '%' || LOWER(:name) || '%' AND LOWER(fatherName) LIKE '%' || LOWER(:fatherName) || '%' AND className = :className LIMIT 1")
    suspend fun verifyStudent(name: String, fatherName: String, className: String): Student?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    // Teachers
    @Query("SELECT * FROM teachers ORDER BY name")
    fun getAllTeachers(): Flow<List<Teacher>>

    @Query("SELECT * FROM teachers WHERE id = :id LIMIT 1")
    suspend fun getTeacherById(id: Int): Teacher?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: Teacher): Long

    @Delete
    suspend fun deleteTeacher(teacher: Teacher)

    // Teacher Assignments
    @Query("SELECT * FROM teacher_assignments")
    fun getAllTeacherAssignments(): Flow<List<TeacherAssignment>>

    @Query("SELECT * FROM teacher_assignments WHERE teacherId = :teacherId")
    fun getAssignmentsForTeacher(teacherId: Int): Flow<List<TeacherAssignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacherAssignment(assignment: TeacherAssignment)

    @Query("DELETE FROM teacher_assignments WHERE id = :id")
    suspend fun deleteTeacherAssignment(id: Int)

    // Homework & Diary Entries
    @Query("SELECT * FROM homework_entries WHERE className = :className AND subjectName = :subjectName ORDER BY timestamp DESC")
    fun getHomeworkForClassAndSubject(className: String, subjectName: String): Flow<List<HomeworkEntry>>

    @Query("SELECT * FROM homework_entries ORDER BY timestamp DESC")
    fun getAllHomeworkEntries(): Flow<List<HomeworkEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomework(entry: HomeworkEntry)

    @Delete
    suspend fun deleteHomework(entry: HomeworkEntry)

    // Attendance
    @Query("SELECT * FROM attendance_records WHERE studentId = :studentId ORDER BY date DESC")
    fun getAttendanceForStudent(studentId: Int): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records WHERE date = :date")
    fun getAttendanceForDate(date: String): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records")
    fun getAllAttendanceRecords(): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(record: AttendanceRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceList(records: List<AttendanceRecord>)

    // Results
    @Query("SELECT * FROM result_records WHERE studentId = :studentId")
    fun getResultsForStudent(studentId: Int): Flow<List<ResultRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: ResultRecord)

    // Notices
    @Query("SELECT * FROM school_notices ORDER BY isEmergency DESC, id DESC")
    fun getAllNotices(): Flow<List<SchoolNotice>>

    @Query("SELECT * FROM school_notices WHERE category = :category ORDER BY id DESC")
    fun getNoticesByCategory(category: String): Flow<List<SchoolNotice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: SchoolNotice)

    @Delete
    suspend fun deleteNotice(notice: SchoolNotice)

    // Notifications
    @Query("SELECT * FROM school_notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<SchoolNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: SchoolNotification)

    // Gallery
    @Query("SELECT * FROM gallery_items ORDER BY id DESC")
    fun getAllGalleryItems(): Flow<List<SchoolGalleryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGalleryItem(item: SchoolGalleryItem)

    // School Info
    @Query("SELECT * FROM school_info WHERE id = 1 LIMIT 1")
    fun getSchoolInfo(): Flow<SchoolInfo?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSchoolInfo(info: SchoolInfo)
}
