package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.plcoding.cleanarchitecturenoteapp.core.util.TestTags
import com.plcoding.cleanarchitecturenoteapp.di.AppModule
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.MainActivity
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.Screen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {


    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalAnimationApi::class)
    @Before
    fun setUp(){
        hiltRule.inject()
        composeRule.setContent {

            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.NotesScreen.route
            ) {
                composable(route = Screen.NotesScreen.route) {
                    NotesScreen(navController = navController)
                }
                composable(
                    route = Screen.AddEditNoteScreen.route +
                            "?noteId={noteId}&noteColor={noteColor}",
                    arguments = listOf(
                        navArgument(
                            name = "noteId"
                        ) {
                            type = NavType.IntType
                            defaultValue = -1
                        },
                        navArgument(
                            name = "noteColor"
                        ) {
                            type = NavType.IntType
                            defaultValue = -1
                        },
                    )
                ) {
                    val color = it.arguments?.getInt("noteColor") ?: -1
                    AddEditNoteScreen(
                        navController = navController,
                        noteColor = color
                    )
                }
            }
        }
    }

    @Test
    fun saveNewNoteEditAfterwards(){

        composeRule.onNodeWithContentDescription("Add").performClick()

        composeRule.
            onNodeWithContentDescription(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("test-title")

        composeRule.
        onNodeWithContentDescription(TestTags.CONTENT_TEXT_FIELD)
            .performTextInput("test-content")


        composeRule.onNodeWithContentDescription("Save").performClick()

        composeRule.onNodeWithText("test-title").assertIsDisplayed()

        composeRule.onNodeWithText("test-title").performClick()

        composeRule.
        onNodeWithContentDescription(TestTags.TITLE_TEXT_FIELD)
            .assertTextContains("test-title")


        composeRule.
        onNodeWithContentDescription(TestTags.CONTENT_TEXT_FIELD)
            .assertTextContains("test-content")


        composeRule
            .onNodeWithContentDescription(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("2")


        composeRule.onNodeWithContentDescription("Save").performClick()

        composeRule.onNodeWithText("test-title2").assertIsDisplayed()
    }
}