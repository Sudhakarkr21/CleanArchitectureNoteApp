package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddNoteTest{
    private lateinit var repository: NoteRepository
    private lateinit var addNote: AddNote

    @Before
    fun setUp(){
        repository = FakeNoteRepository()
        addNote = AddNote(repository)
    }

    @Test
    fun `validate title of Added Note`() = runBlocking{

        val note = Note("sss","Sub",2.22.toLong(),1)
        addNote(note)

        assertThat(repository.getNotes().first()).contains(note)
    }

}