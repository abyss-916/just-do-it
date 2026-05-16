package com.escodro.task.presentation.instrumented

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.permission.api.PermissionController
import com.escodro.resources.Res
import com.escodro.resources.task_detail_alarm_no_alarm
import com.escodro.task.presentation.detail.alarm.AlarmSelectionContent
import com.escodro.task.presentation.detail.alarm.AlarmSelectionState
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmScheduler
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmSchedulerImpl
import com.escodro.task.presentation.fake.PermissionControllerFake
import com.escodro.test.AlkaaTest
import org.jetbrains.compose.resources.getString
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
internal class AlarmPermissionFlowTest : AlkaaTest() {

    private val permissionsController = PermissionControllerFake()

    private val testModule = module {
        single<PermissionController> { permissionsController }
        single<OpenAlarmScheduler> { OpenAlarmSchedulerImpl() }
    }

    private lateinit var state: AlarmSelectionState

    @AfterTest
    fun tearDown() {
        permissionsController.clean()
    }

    private fun createState() = AlarmSelectionState(
        calendar = null,
        permissionsController = permissionsController,
    ).also { state = it }

    @Test
    fun test_notificationPermissionGrantedDoesNotShowDialog() = runComposeUiTest {
        permissionsController.isPermissionGrantedValue = true
        createState()

        loadAlarmSelectionContent(
            state = state,
            hasAlarmPermission = true,
        )

        val noAlarmString = getString(Res.string.task_detail_alarm_no_alarm)
        onNodeWithText(noAlarmString).performClick()

        assertFalse(state.isNotificationDialogOpen)
        assertFalse(state.isRationaleDialogOpen)
        assertFalse(state.isExactAlarmDialogOpen)
    }

    @Test
    fun test_noNotificationPermissionShowsPermissionDialog() = runComposeUiTest {
        permissionsController.isPermissionGrantedValue = false
        createState()

        loadAlarmSelectionContent(
            state = state,
            hasAlarmPermission = true,
        )

        val noAlarmString = getString(Res.string.task_detail_alarm_no_alarm)
        onNodeWithText(noAlarmString).performClick()

        assertTrue(state.isNotificationDialogOpen)
        assertFalse(state.isRationaleDialogOpen)
        assertFalse(state.isExactAlarmDialogOpen)
    }

    @Test
    fun test_noExactAlarmPermissionDialog() = runComposeUiTest {
        createState()

        loadAlarmSelectionContent(
            state = state,
            hasAlarmPermission = false,
        )

        val noAlarmString = getString(Res.string.task_detail_alarm_no_alarm)
        onNodeWithText(noAlarmString).performClick()

        assertFalse(state.isNotificationDialogOpen)
        assertFalse(state.isRationaleDialogOpen)
        assertTrue(state.isExactAlarmDialogOpen)
    }

    @Test
    fun test_noPermissionAtAllDialog() = runComposeUiTest {
        permissionsController.isPermissionGrantedValue = false
        createState()

        loadAlarmSelectionContent(
            state = state,
            hasAlarmPermission = false,
        )

        val noAlarmString = getString(Res.string.task_detail_alarm_no_alarm)
        onNodeWithText(noAlarmString).performClick()

        assertTrue(state.isNotificationDialogOpen)
        assertFalse(state.isRationaleDialogOpen)
        assertTrue(state.isExactAlarmDialogOpen)
    }

    private fun ComposeUiTest.loadAlarmSelectionContent(
        state: AlarmSelectionState,
        hasAlarmPermission: Boolean,
    ) {
        setContent {
            KoinApplication(configuration = koinConfiguration { modules(testModule) }) {
                AlkaaThemePreview {
                    AlarmSelectionContent(
                        alarmSelectionState = state,
                        hasExactAlarmPermission = { hasAlarmPermission },
                        onAlarmUpdate = {},
                    )
                }
            }
        }
    }
}
