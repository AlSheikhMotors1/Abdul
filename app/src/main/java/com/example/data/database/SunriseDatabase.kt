package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.SchoolDao
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Student::class,
        Teacher::class,
        TeacherAssignment::class,
        HomeworkEntry::class,
        AttendanceRecord::class,
        ResultRecord::class,
        SchoolNotice::class,
        SchoolNotification::class,
        SchoolGalleryItem::class,
        SchoolInfo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SunriseDatabase : RoomDatabase() {

    abstract fun schoolDao(): SchoolDao

    companion object {
        @Volatile
        private var INSTANCE: SunriseDatabase? = null

        fun getDatabase(context: Context): SunriseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SunriseDatabase::class.java,
                    "sunrise_school_db"
                )
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.schoolDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: SchoolDao) {
                // School Info
                dao.updateSchoolInfo(
                    SchoolInfo(
                        id = 1,
                        schoolName = "Sunrise School Lodhra",
                        tagline = "Excellence in Education - Lodhra, Pakistan",
                        phone = "03013662014",
                        whatsapp = "03013662014",
                        email = "info@sunriseschool.edu.pk",
                        address = "Main College Road, Near Station Chowk, Lodhra, Punjab, Pakistan",
                        googleMapsUrl = "https://maps.google.com/?q=Lodhra+Pakistan",
                        schoolTiming = "Mon - Sat: 07:45 AM - 01:30 PM (Friday: 07:45 AM - 12:00 PM)",
                        bannerText = "🌟 Welcome to Sunrise School Lodhra - Admissions Open for Session 2026-27! Quality Education with Discipline & Character.",
                        principalName = "Muhammad Aftab Ali",
                        principalMessage = "Welcome parents and students! At Sunrise School Lodhra, we focus on modern academic excellence alongside Islamic and moral values to prepare future leaders."
                    )
                )

                // Teachers
                val t1Id = dao.insertTeacher(
                    Teacher(name = "Saniya", email = "saniya@sunriseschool.edu.pk", passcode = "1234")
                ).toInt()
                val t2Id = dao.insertTeacher(
                    Teacher(name = "Ahmed Raza", email = "ahmed@sunriseschool.edu.pk", passcode = "1234")
                ).toInt()
                val t3Id = dao.insertTeacher(
                    Teacher(name = "Fatima Noor", email = "fatima@sunriseschool.edu.pk", passcode = "1234")
                ).toInt()

                // Teacher Assignments
                // Saniya: Class 1 -> Science, Class 2 -> Mathematics, Class 4 -> Urdu
                dao.insertTeacherAssignment(TeacherAssignment(teacherId = t1Id, className = "Class 1", subjectName = "Science"))
                dao.insertTeacherAssignment(TeacherAssignment(teacherId = t1Id, className = "Class 2", subjectName = "Mathematics"))
                dao.insertTeacherAssignment(TeacherAssignment(teacherId = t1Id, className = "Class 4", subjectName = "Urdu"))

                // Ahmed: Class 1 -> English, Class 3 -> Mathematics, Class 5 -> Computer
                dao.insertTeacherAssignment(TeacherAssignment(teacherId = t2Id, className = "Class 1", subjectName = "English"))
                dao.insertTeacherAssignment(TeacherAssignment(teacherId = t2Id, className = "Class 3", subjectName = "Mathematics"))
                dao.insertTeacherAssignment(TeacherAssignment(teacherId = t2Id, className = "Class 5", subjectName = "Computer"))

                // Fatima: Class 2 -> Science, Class 4 -> English, Class 6 -> Islamiat
                dao.insertTeacherAssignment(TeacherAssignment(teacherId = t3Id, className = "Class 2", subjectName = "Science"))
                dao.insertTeacherAssignment(TeacherAssignment(teacherId = t3Id, className = "Class 4", subjectName = "English"))
                dao.insertTeacherAssignment(TeacherAssignment(teacherId = t3Id, className = "Class 6", subjectName = "Islamiat"))

                // Students
                val s1Id = dao.insertStudent(
                    Student(
                        name = "Ali Raza",
                        fatherName = "Tariq Mahmood",
                        className = "Class 1",
                        section = "A",
                        rollNumber = "SSL-101",
                        feeCleared = true,
                        feePendingAmount = 0.0,
                        feeDueDate = "10th August 2026",
                        teacherRemarks = "Excellent in Science and English. Very attentive in class."
                    )
                ).toInt()

                val s2Id = dao.insertStudent(
                    Student(
                        name = "Ayesha Khan",
                        fatherName = "Zahid Khan",
                        className = "Class 1",
                        section = "A",
                        rollNumber = "SSL-102",
                        feeCleared = false,
                        feePendingAmount = 4500.0,
                        feeDueDate = "10th August 2026",
                        teacherRemarks = "Shows good enthusiasm in drawing and oral science questions."
                    )
                ).toInt()

                val s3Id = dao.insertStudent(
                    Student(
                        name = "Zainab Fatima",
                        fatherName = "Muhammad Imran",
                        className = "Class 2",
                        section = "B",
                        rollNumber = "SSL-201",
                        feeCleared = true,
                        feePendingAmount = 0.0,
                        feeDueDate = "10th August 2026",
                        teacherRemarks = "Top scorer in Mathematics. Great neatness in notebook work."
                    )
                ).toInt()

                val s4Id = dao.insertStudent(
                    Student(
                        name = "Usman Ghani",
                        fatherName = "Ghani Akram",
                        className = "Class 4",
                        section = "A",
                        rollNumber = "SSL-401",
                        feeCleared = true,
                        feePendingAmount = 0.0,
                        feeDueDate = "10th August 2026",
                        teacherRemarks = "Punctual, polite, and strong command over Urdu literature."
                    )
                ).toInt()

                // Homework Entries
                dao.insertHomework(
                    HomeworkEntry(
                        className = "Class 1",
                        subjectName = "Science",
                        contentType = "HOMEWORK",
                        chapter = "Chapter 4: Living Things around Us",
                        content = "Read pages 45–48 carefully. Solve Exercise 4 (Q1 to Q5) in your homework notebook.",
                        authorTeacherName = "Saniya",
                        dateFormatted = "Today"
                    )
                )

                dao.insertHomework(
                    HomeworkEntry(
                        className = "Class 1",
                        subjectName = "Science",
                        contentType = "DAILY_DIARY",
                        chapter = "Chapter 4: Living Things around Us",
                        content = "Class Test for Chapter 3 will be held tomorrow. Bring colored pencils for diagram drawing.",
                        authorTeacherName = "Saniya",
                        dateFormatted = "Today"
                    )
                )

                dao.insertHomework(
                    HomeworkEntry(
                        className = "Class 2",
                        subjectName = "Mathematics",
                        contentType = "HOMEWORK",
                        chapter = "Chapter 3: Multiplication & Division",
                        content = "Learn Multiplication Tables of 6 and 7. Complete Page 34 Exercise 3.2 Q1 to Q8.",
                        authorTeacherName = "Saniya",
                        dateFormatted = "Today"
                    )
                )

                dao.insertHomework(
                    HomeworkEntry(
                        className = "Class 4",
                        subjectName = "Urdu",
                        contentType = "HOMEWORK",
                        chapter = "Sabaq 5: Hamara Pyara Pakistan",
                        content = "Sabaq 5 ke alfaz maani aur khulasa copy mein tahreer karein aur yaad karein.",
                        authorTeacherName = "Saniya",
                        dateFormatted = "Today"
                    )
                )

                // Attendance Records for Ali Raza (Class 1)
                val dates = listOf("2026-07-21", "2026-07-20", "2026-07-19", "2026-07-18", "2026-07-17", "2026-07-16")
                val statuses = listOf("PRESENT", "PRESENT", "PRESENT", "LEAVE", "PRESENT", "PRESENT")
                for (i in dates.indices) {
                    dao.insertAttendance(
                        AttendanceRecord(
                            studentId = s1Id,
                            date = dates[i],
                            month = "July 2026",
                            status = statuses[i]
                        )
                    )
                    dao.insertAttendance(
                        AttendanceRecord(
                            studentId = s2Id,
                            date = dates[i],
                            month = "July 2026",
                            status = if (i == 3) "ABSENT" else "PRESENT"
                        )
                    )
                }

                // Academic Results
                val subjects = listOf("English", "Urdu", "Mathematics", "Science", "Islamiat", "Computer")
                val marks1 = listOf(92, 88, 95, 96, 98, 90)
                val marks2 = listOf(82, 75, 80, 85, 90, 84)

                for (i in subjects.indices) {
                    dao.insertResult(
                        ResultRecord(
                            studentId = s1Id,
                            term = "MONTHLY_TEST",
                            subjectName = subjects[i],
                            totalMarks = 100,
                            obtainedMarks = marks1[i],
                            teacherRemarks = "Outstanding grade!"
                        )
                    )
                    dao.insertResult(
                        ResultRecord(
                            studentId = s1Id,
                            term = "MID_TERM",
                            subjectName = subjects[i],
                            totalMarks = 100,
                            obtainedMarks = marks1[i] - 2,
                            teacherRemarks = "Consistently excellent!"
                        )
                    )
                    dao.insertResult(
                        ResultRecord(
                            studentId = s2Id,
                            term = "MONTHLY_TEST",
                            subjectName = subjects[i],
                            totalMarks = 100,
                            obtainedMarks = marks2[i],
                            teacherRemarks = "Good effort, keep practicing math."
                        )
                    )
                }

                // Notices
                dao.insertNotice(
                    SchoolNotice(
                        category = "EMERGENCY_NOTICES",
                        title = "Heavy Rain Alert & Weather Update",
                        description = "Due to heavy monsoon rainfall prediction in Lodhra district, school will remain closed on Thursday, 23rd July 2026. Online homework has been posted.",
                        date = "22 July 2026",
                        isEmergency = true
                    )
                )

                dao.insertNotice(
                    SchoolNotice(
                        category = "SCHOOL_TIMING",
                        title = "Summer School Timings",
                        description = "Morning Assembly: 07:45 AM. Departure: 01:30 PM. All students must strictly arrive in full summer uniform.",
                        date = "15 July 2026"
                    )
                )

                dao.insertNotice(
                    SchoolNotice(
                        category = "EXAM_SCHEDULE",
                        title = "Mid-Term Examination Datesheet",
                        description = "Mid-Term exams for Play Group to Class 8 will commence from 15th August 2026. Detailed datesheet is available in downloads.",
                        date = "18 July 2026"
                    )
                )

                dao.insertNotice(
                    SchoolNotice(
                        category = "HOLIDAYS",
                        title = "Independence Day Celebration & Holiday",
                        description = "14th August 2026: Independence Day celebration at 08:00 AM. Dress code: Green & White clothing.",
                        date = "20 July 2026"
                    )
                )

                dao.insertNotice(
                    SchoolNotice(
                        category = "PARENT_MEETINGS",
                        title = "Monthly Parent-Teacher Meeting (PTM)",
                        description = "PTM for discussing Monthly Test results is scheduled for Saturday, 1st August 2026 from 09:00 AM to 12:30 PM.",
                        date = "19 July 2026"
                    )
                )

                // Notifications
                dao.insertNotification(
                    SchoolNotification(
                        title = "New Homework Uploaded",
                        message = "Teacher Saniya added new Science homework for Class 1.",
                        category = "Homework"
                    )
                )

                dao.insertNotification(
                    SchoolNotification(
                        title = "Important Notice",
                        message = "Emergency Weather Notification issued by School Administration.",
                        category = "Notice"
                    )
                )

                // Gallery Items
                dao.insertGalleryItem(
                    SchoolGalleryItem(
                        title = "Annual Science Exhibition 2026",
                        category = "Events",
                        imageUrl = "https://images.unsplash.com/photo-1532094349884-543bc11b234d",
                        date = "March 2026"
                    )
                )
                dao.insertGalleryItem(
                    SchoolGalleryItem(
                        title = "Annual Sports Day & Tug of War",
                        category = "Sports",
                        imageUrl = "https://images.unsplash.com/photo-1461896836934-ffe607ba8211",
                        date = "February 2026"
                    )
                )
                dao.insertGalleryItem(
                    SchoolGalleryItem(
                        title = "Sunrise School Lodhra Campus Entrance",
                        category = "Campus",
                        imageUrl = "https://images.unsplash.com/photo-1541829070764-84a7d30dd3f3",
                        date = "2026"
                    )
                )
            }
        }
    }
}
