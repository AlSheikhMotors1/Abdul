package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.SunriseDatabase
import com.example.data.model.*
import com.example.data.repository.SchoolRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SchoolViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SchoolRepository
    val schoolInfo: StateFlow<SchoolInfo>
    val allNotices: StateFlow<List<SchoolNotice>>
    val allNotifications: StateFlow<List<SchoolNotification>>
    val allGalleryItems: StateFlow<List<SchoolGalleryItem>>
    val allStudents: StateFlow<List<Student>>
    val allTeachers: StateFlow<List<Teacher>>
    val allTeacherAssignments: StateFlow<List<TeacherAssignment>>
    val allHomeworkEntries: StateFlow<List<HomeworkEntry>>
    val allAttendanceRecords: StateFlow<List<AttendanceRecord>>

    // Current Active Role & User Session
    private val _currentRole = MutableStateFlow(UserRole.PARENT_STUDENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _verifiedStudent = MutableStateFlow<Student?>(null)
    val verifiedStudent: StateFlow<Student?> = _verifiedStudent.asStateFlow()

    private val _activeTeacher = MutableStateFlow<Teacher?>(null)
    val activeTeacher: StateFlow<Teacher?> = _activeTeacher.asStateFlow()

    // Verification Form State
    var parentInputStudentName = MutableStateFlow("Ali Raza")
    var parentInputFatherName = MutableStateFlow("Tariq Mahmood")
    var parentInputClass = MutableStateFlow("Class 1")
    var verificationError = MutableStateFlow<String?>(null)

    // Selection State
    val selectedClass = MutableStateFlow("Class 1")
    val selectedSubject = MutableStateFlow("Science")
    val selectedContentType = MutableStateFlow("HOMEWORK") // HOMEWORK, DAILY_DIARY, CLASSWORK, ASSIGNMENT, TEACHER_NOTES

    init {
        val dao = SunriseDatabase.getDatabase(application).schoolDao()
        repository = SchoolRepository(dao)

        schoolInfo = repository.schoolInfo.map { it ?: SchoolInfo() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SchoolInfo())

        allNotices = repository.allNotices
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allNotifications = repository.allNotifications
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allGalleryItems = repository.allGalleryItems
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allStudents = repository.allStudents
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allTeachers = repository.allTeachers
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allTeacherAssignments = repository.allTeacherAssignments
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allHomeworkEntries = repository.allHomeworkEntries
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allAttendanceRecords = repository.allAttendanceRecords
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Auto verify default student for initial friendly state
        verifyStudentDefault()
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
        if (role == UserRole.TEACHER && _activeTeacher.value == null) {
            // Auto login first teacher as example
            viewModelScope.launch {
                val teachers = allTeachers.value
                if (teachers.isNotEmpty()) {
                    _activeTeacher.value = teachers.first()
                }
            }
        }
    }

    private fun verifyStudentDefault() {
        viewModelScope.launch {
            val student = repository.verifyStudent("Ali Raza", "Tariq Mahmood", "Class 1")
            if (student != null) {
                _verifiedStudent.value = student
            }
        }
    }

    fun verifyStudentForm(context: Context) {
        val name = parentInputStudentName.value.trim()
        val father = parentInputFatherName.value.trim()
        val cls = parentInputClass.value.trim()

        if (name.isEmpty() || father.isEmpty()) {
            verificationError.value = "Please enter Student Name and Father's Name."
            return
        }

        viewModelScope.launch {
            val student = repository.verifyStudent(name, father, cls)
            if (student != null) {
                _verifiedStudent.value = student
                verificationError.value = null
                Toast.makeText(context, "Verification Successful! Welcome ${student.name}", Toast.LENGTH_SHORT).show()
            } else {
                verificationError.value = "No matching student found for $name, $father in $cls. Please check details or contact school."
            }
        }
    }

    fun selectStudentForParent(student: Student) {
        _verifiedStudent.value = student
        parentInputStudentName.value = student.name
        parentInputFatherName.value = student.fatherName
        parentInputClass.value = student.className
        verificationError.value = null
    }

    fun loginTeacher(teacher: Teacher) {
        _activeTeacher.value = teacher
        _currentRole.value = UserRole.TEACHER
    }

    // Homework for selected Class and Subject
    fun getHomeworkForSelectedClassAndSubject(): Flow<List<HomeworkEntry>> {
        return repository.getHomeworkForClassAndSubject(selectedClass.value, selectedSubject.value)
    }

    // Attendance for verified student
    fun getAttendanceForVerifiedStudent(): Flow<List<AttendanceRecord>> {
        val s = _verifiedStudent.value ?: return flowOf(emptyList())
        return repository.getAttendanceForStudent(s.id)
    }

    fun saveDailyAttendance(
        records: List<AttendanceRecord>,
        absentStudentNames: List<String>,
        className: String,
        date: String,
        context: Context
    ) {
        viewModelScope.launch {
            repository.saveAttendanceList(records, absentStudentNames)
            Toast.makeText(
                context,
                "Attendance saved for $className on $date! (${records.count { it.status == "PRESENT" }} Present, ${records.count { it.status == "ABSENT" }} Absent)",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Results for verified student
    fun getResultsForVerifiedStudent(): Flow<List<ResultRecord>> {
        val s = _verifiedStudent.value ?: return flowOf(emptyList())
        return repository.getResultsForStudent(s.id)
    }

    // Homework Upload (Teachers & Admin)
    fun uploadHomework(
        className: String,
        subjectName: String,
        contentType: String,
        chapter: String,
        content: String,
        authorTeacherName: String,
        context: Context
    ) {
        if (chapter.isBlank() || content.isBlank()) {
            Toast.makeText(context, "Please fill in chapter and description.", Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            repository.insertHomework(
                HomeworkEntry(
                    className = className,
                    subjectName = subjectName,
                    contentType = contentType,
                    chapter = chapter,
                    content = content,
                    authorTeacherName = authorTeacherName,
                    dateFormatted = "Today"
                )
            )
            Toast.makeText(context, "$contentType saved! Automatically available to all $className students.", Toast.LENGTH_LONG).show()
        }
    }

    // Teacher Management (Admin)
    fun addTeacher(name: String, email: String, context: Context) {
        if (name.isBlank() || email.isBlank()) return
        viewModelScope.launch {
            repository.insertTeacher(Teacher(name = name, email = email))
            Toast.makeText(context, "Teacher $name added successfully", Toast.LENGTH_SHORT).show()
        }
    }

    fun assignTeacher(teacherId: Int, className: String, subjectName: String, context: Context) {
        viewModelScope.launch {
            repository.insertTeacherAssignment(
                TeacherAssignment(teacherId = teacherId, className = className, subjectName = subjectName)
            )
            Toast.makeText(context, "Subject $subjectName assigned for $className", Toast.LENGTH_SHORT).show()
        }
    }

    fun removeAssignment(assignmentId: Int) {
        viewModelScope.launch {
            repository.deleteTeacherAssignment(assignmentId)
        }
    }

    // Student Management (Admin)
    fun addOrUpdateStudent(
        id: Int = 0,
        name: String,
        fatherName: String,
        className: String,
        section: String,
        rollNumber: String,
        feeCleared: Boolean,
        pendingAmount: Double,
        remarks: String,
        context: Context
    ) {
        if (name.isBlank() || fatherName.isBlank()) return
        viewModelScope.launch {
            val student = Student(
                id = id,
                name = name,
                fatherName = fatherName,
                className = className,
                section = section,
                rollNumber = rollNumber,
                feeCleared = feeCleared,
                feePendingAmount = pendingAmount,
                teacherRemarks = remarks
            )
            repository.insertStudent(student)
            Toast.makeText(context, "Student $name saved successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            repository.deleteStudent(student)
        }
    }

    // Admin Notice & Push Notifications
    fun addNotice(category: String, title: String, description: String, isEmergency: Boolean, context: Context) {
        if (title.isBlank() || description.isBlank()) return
        viewModelScope.launch {
            repository.insertNotice(
                SchoolNotice(
                    category = category,
                    title = title,
                    description = description,
                    date = "Today",
                    isEmergency = isEmergency
                )
            )
            Toast.makeText(context, "Notice published successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendPushNotification(title: String, message: String, context: Context) {
        if (title.isBlank() || message.isBlank()) return
        viewModelScope.launch {
            repository.sendPushNotification(title, message, "Announcement")
            Toast.makeText(context, "Push notification broadcasted to all parents!", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateBannerText(newText: String, context: Context) {
        val currentInfo = schoolInfo.value
        viewModelScope.launch {
            repository.updateSchoolInfo(currentInfo.copy(bannerText = newText))
            Toast.makeText(context, "School banner updated!", Toast.LENGTH_SHORT).show()
        }
    }

    // Direct Contact Actions (Intent Call & WhatsApp)
    fun callSchool(context: Context) {
        val phone = schoolInfo.value.phone
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Calling $phone", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(context: Context) {
        val rawNumber = schoolInfo.value.whatsapp.replace(" ", "").replace("+", "").replace("-", "")
        val formattedNumber = if (rawNumber.startsWith("0")) {
            "92" + rawNumber.substring(1)
        } else if (!rawNumber.startsWith("92") && rawNumber.length == 10) {
            "92$rawNumber"
        } else {
            rawNumber
        }
        val url = "https://wa.me/$formattedNumber?text=Assalamu%20Alaikum%20Sunrise%20School%20Lodhra"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp: ${schoolInfo.value.whatsapp}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openGoogleMaps(context: Context) {
        val url = schoolInfo.value.googleMapsUrl
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Location: ${schoolInfo.value.address}", Toast.LENGTH_SHORT).show()
        }
    }
}
