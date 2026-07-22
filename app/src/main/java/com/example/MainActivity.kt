package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.model.UserRole
import com.example.ui.components.SchoolHeaderBanner
import com.example.ui.screens.*
import com.example.ui.screens.subscreens.*
import com.example.ui.theme.SunriseSchoolTheme
import com.example.ui.viewmodel.SchoolViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SchoolViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SunriseSchoolTheme {
                val navController = rememberNavController()

                // ViewModel State Observations
                val schoolInfo by viewModel.schoolInfo.collectAsStateWithLifecycle()
                val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
                val verifiedStudent by viewModel.verifiedStudent.collectAsStateWithLifecycle()
                val activeTeacher by viewModel.activeTeacher.collectAsStateWithLifecycle()
                val notices by viewModel.allNotices.collectAsStateWithLifecycle()
                val notifications by viewModel.allNotifications.collectAsStateWithLifecycle()
                val galleryItems by viewModel.allGalleryItems.collectAsStateWithLifecycle()
                val allStudents by viewModel.allStudents.collectAsStateWithLifecycle()
                val allTeachers by viewModel.allTeachers.collectAsStateWithLifecycle()
                val assignments by viewModel.allTeacherAssignments.collectAsStateWithLifecycle()
                val homeworkEntries by viewModel.allHomeworkEntries.collectAsStateWithLifecycle()
                val allAttendanceRecords by viewModel.allAttendanceRecords.collectAsStateWithLifecycle()

                val verifiedAttendance by viewModel.getAttendanceForVerifiedStudent().collectAsStateWithLifecycle(initialValue = emptyList())
                val verifiedResults by viewModel.getResultsForVerifiedStudent().collectAsStateWithLifecycle(initialValue = emptyList())

                val parentNameInput by viewModel.parentInputStudentName.collectAsStateWithLifecycle()
                val parentFatherInput by viewModel.parentInputFatherName.collectAsStateWithLifecycle()
                val parentClassInput by viewModel.parentInputClass.collectAsStateWithLifecycle()
                val verificationError by viewModel.verificationError.collectAsStateWithLifecycle()

                val context = LocalContext.current

                Scaffold(
                    topBar = {
                        SchoolHeaderBanner(
                            schoolInfo = schoolInfo,
                            currentRole = currentRole,
                            onRoleSelect = { newRole ->
                                viewModel.setRole(newRole)
                                if (newRole == UserRole.ADMIN) {
                                    navController.navigate("ADMIN_PANEL")
                                } else if (newRole == UserRole.TEACHER) {
                                    navController.navigate("TEACHER_PORTAL")
                                }
                            },
                            onNotificationsClick = {
                                navController.navigate("NOTIFICATIONS")
                            },
                            unreadNotificationCount = notifications.count { !it.isRead }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "HOME"
                        ) {
                            // Home Screen
                            composable("HOME") {
                                HomeScreen(
                                    schoolInfo = schoolInfo,
                                    onNavigateToUpdates = { navController.navigate("UPDATES") },
                                    onNavigateToDiary = { navController.navigate("DIARY") },
                                    onNavigateToDashboard = { navController.navigate("DASHBOARD") },
                                    onQuickAccessClick = { itemId ->
                                        when (itemId) {
                                            "call" -> viewModel.callSchool(context)
                                            "whatsapp" -> viewModel.openWhatsApp(context)
                                            "location" -> viewModel.openGoogleMaps(context)
                                            else -> navController.navigate("QUICK_ACCESS/$itemId")
                                        }
                                    },
                                    onCallClick = { viewModel.callSchool(context) },
                                    onWhatsAppClick = { viewModel.openWhatsApp(context) },
                                    onMapsClick = { viewModel.openGoogleMaps(context) },
                                    onAdminClick = { navController.navigate("ADMIN_PANEL") },
                                    onTeacherPortalClick = { navController.navigate("TEACHER_PORTAL") },
                                    currentRole = currentRole
                                )
                            }

                            // 1. School Updates Screen
                            composable("UPDATES") {
                                SchoolUpdatesScreen(
                                    notices = notices,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            // 2. Classes & Daily Diary Screen
                            composable("DIARY") {
                                ClassesAndDiaryScreen(
                                    homeworkEntries = homeworkEntries,
                                    currentRole = currentRole,
                                    activeTeacher = activeTeacher,
                                    onUploadHomeworkClick = { cls, subj, type ->
                                        if (currentRole == UserRole.TEACHER) {
                                            navController.navigate("TEACHER_PORTAL")
                                        } else {
                                            navController.navigate("ADMIN_PANEL")
                                        }
                                    },
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            // 3. Student Dashboard Screen
                            composable("DASHBOARD") {
                                StudentDashboardScreen(
                                    verifiedStudent = verifiedStudent,
                                    allStudents = allStudents,
                                    parentInputName = parentNameInput,
                                    parentInputFather = parentFatherInput,
                                    parentInputClass = parentClassInput,
                                    verificationError = verificationError,
                                    onNameChange = { viewModel.parentInputStudentName.value = it },
                                    onFatherChange = { viewModel.parentInputFatherName.value = it },
                                    onClassChange = { viewModel.parentInputClass.value = it },
                                    onVerifyClick = { viewModel.verifyStudentForm(context) },
                                    onSelectStudent = { student -> viewModel.selectStudentForParent(student) },
                                    onOpenOptionPage = { optionKey ->
                                        when (optionKey) {
                                            "INFO" -> navController.navigate("DASHBOARD_INFO")
                                            "ACADEMIC" -> navController.navigate("DASHBOARD_ACADEMIC")
                                            "ATTENDANCE" -> navController.navigate("DASHBOARD_ATTENDANCE")
                                            "FEE" -> navController.navigate("DASHBOARD_FEE")
                                            "REMARKS" -> navController.navigate("DASHBOARD_REMARKS")
                                        }
                                    },
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            // Dedicated Dashboard Sub-Pages (Strict Separate Page Requirement)
                            composable("DASHBOARD_INFO") {
                                verifiedStudent?.let { student ->
                                    StudentInfoScreen(
                                        student = student,
                                        onBackClick = { navController.popBackStack() }
                                    )
                                }
                            }

                            composable("DASHBOARD_ACADEMIC") {
                                verifiedStudent?.let { student ->
                                    AcademicPerformanceScreen(
                                        student = student,
                                        results = verifiedResults,
                                        onBackClick = { navController.popBackStack() }
                                    )
                                }
                            }

                            composable("DASHBOARD_ATTENDANCE") {
                                AttendanceDashboardScreen(
                                    currentRole = currentRole,
                                    activeTeacher = activeTeacher,
                                    allStudents = allStudents,
                                    allAttendanceRecords = allAttendanceRecords,
                                    verifiedStudent = verifiedStudent,
                                    onSaveAttendance = { recs, absents, cls, dt ->
                                        viewModel.saveDailyAttendance(recs, absents, cls, dt, context)
                                    },
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            composable("ATTENDANCE_DASHBOARD") {
                                AttendanceDashboardScreen(
                                    currentRole = currentRole,
                                    activeTeacher = activeTeacher,
                                    allStudents = allStudents,
                                    allAttendanceRecords = allAttendanceRecords,
                                    verifiedStudent = verifiedStudent,
                                    onSaveAttendance = { recs, absents, cls, dt ->
                                        viewModel.saveDailyAttendance(recs, absents, cls, dt, context)
                                    },
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            composable("DASHBOARD_FEE") {
                                verifiedStudent?.let { student ->
                                    FeeStatusScreen(
                                        student = student,
                                        onBackClick = { navController.popBackStack() }
                                    )
                                }
                            }

                            composable("DASHBOARD_REMARKS") {
                                verifiedStudent?.let { student ->
                                    TeacherRemarksScreen(
                                        student = student,
                                        onBackClick = { navController.popBackStack() }
                                    )
                                }
                            }

                            // Teacher Portal Screen
                            composable("TEACHER_PORTAL") {
                                TeacherPortalScreen(
                                    activeTeacher = activeTeacher,
                                    allTeachers = allTeachers,
                                    assignments = assignments,
                                    onSelectTeacher = { teacher -> viewModel.loginTeacher(teacher) },
                                    onUploadContent = { className, subjectName, contentType, chapter, content ->
                                        viewModel.uploadHomework(
                                            className = className,
                                            subjectName = subjectName,
                                            contentType = contentType,
                                            chapter = chapter,
                                            content = content,
                                            authorTeacherName = activeTeacher?.name ?: "Teacher",
                                            context = context
                                        )
                                    },
                                    onNavigateToAttendance = {
                                        navController.navigate("ATTENDANCE_DASHBOARD")
                                    },
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            // Admin Panel Screen
                            composable("ADMIN_PANEL") {
                                AdminPanelScreen(
                                    schoolInfo = schoolInfo,
                                    students = allStudents,
                                    teachers = allTeachers,
                                    assignments = assignments,
                                    notices = notices,
                                    onAddTeacher = { name, email -> viewModel.addTeacher(name, email, context) },
                                    onAssignTeacher = { teacherId, className, subjectName -> viewModel.assignTeacher(teacherId, className, subjectName, context) },
                                    onAddStudent = { id, name, father, className, section, roll, feeCleared, pending, remarks ->
                                        viewModel.addOrUpdateStudent(id, name, father, className, section, roll, feeCleared, pending, remarks, context)
                                    },
                                    onDeleteStudent = { student -> viewModel.deleteStudent(student) },
                                    onAddNotice = { category, title, description, isEmergency ->
                                        viewModel.addNotice(category, title, description, isEmergency, context)
                                    },
                                    onSendNotification = { title, message ->
                                        viewModel.sendPushNotification(title, message, context)
                                    },
                                    onUpdateBanner = { text -> viewModel.updateBannerText(text, context) },
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            // Notification Center Screen
                            composable("NOTIFICATIONS") {
                                NotificationScreen(
                                    notifications = notifications,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            // Quick Access Detail Screen
                            composable("QUICK_ACCESS/{itemId}") { backStackEntry ->
                                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                                QuickAccessDetailScreen(
                                    itemId = itemId,
                                    schoolInfo = schoolInfo,
                                    galleryItems = galleryItems,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
