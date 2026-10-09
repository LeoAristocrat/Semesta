package com.leoaristocrat.semesta.core.di

import android.content.Context
import com.leoaristocrat.semesta.core.datastore.UserPreferencesDataSource
import com.leoaristocrat.semesta.feature_expenses.data.local.ExpenseDao
import com.leoaristocrat.semesta.feature_grades.data.local.GradeDao
import com.leoaristocrat.semesta.feature_grades.data.local.SubjectDao
import com.leoaristocrat.semesta.feature_grades.data.local.SemestaDatabase
import com.leoaristocrat.semesta.feature_schedule.data.local.AgendaEventDao
import com.leoaristocrat.semesta.feature_terms.data.local.AcademicBreakDao
import com.leoaristocrat.semesta.feature_terms.data.local.AcademicTermDao
import com.leoaristocrat.semesta.feature_schedule.data.local.ClassOccurrenceDao
import com.leoaristocrat.semesta.feature_schedule.data.local.ClassSessionDao
import com.leoaristocrat.semesta.feature_notes.data.local.NoteAttachmentDao
import com.leoaristocrat.semesta.feature_notes.data.local.NoteDao
import com.leoaristocrat.semesta.feature_tasks.data.local.TaskAttachmentDao
import com.leoaristocrat.semesta.feature_tasks.data.local.TaskDao
import com.leoaristocrat.semesta.feature_templates.data.local.AcademicWorkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataSource(@ApplicationContext context: Context): UserPreferencesDataSource =
        UserPreferencesDataSource(context)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SemestaDatabase =
        SemestaDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideSubjectDao(db: SemestaDatabase): SubjectDao = db.subjectDao()

    @Provides
    @Singleton
    fun provideGradeDao(db: SemestaDatabase): GradeDao = db.gradeDao()

    @Provides
    @Singleton
    fun provideTaskDao(db: SemestaDatabase): TaskDao = db.taskDao()

    @Provides
    @Singleton
    fun provideTaskAttachmentDao(db: SemestaDatabase): TaskAttachmentDao = db.taskAttachmentDao()

    @Provides
    @Singleton
    fun provideExpenseDao(db: SemestaDatabase): ExpenseDao = db.expenseDao()

    @Provides
    @Singleton
    fun provideAcademicWorkDao(db: SemestaDatabase): AcademicWorkDao = db.academicWorkDao()

    @Provides
    fun provideWorkRoomDao(db: SemestaDatabase): com.leoaristocrat.semesta.feature_rooms.data.local.WorkRoomDao = db.workRoomDao()

    @Provides
    @Singleton
    fun provideClassSessionDao(db: SemestaDatabase): ClassSessionDao = db.classSessionDao()

    @Provides
    @Singleton
    fun provideClassOccurrenceDao(db: SemestaDatabase): ClassOccurrenceDao = db.classOccurrenceDao()

    @Provides
    @Singleton
    fun provideAgendaEventDao(db: SemestaDatabase): AgendaEventDao = db.agendaEventDao()

    @Provides
    @Singleton
    fun provideAcademicTermDao(db: SemestaDatabase): AcademicTermDao = db.academicTermDao()

    @Provides
    fun provideAcademicBreakDao(db: SemestaDatabase): AcademicBreakDao = db.academicBreakDao()

    @Provides
    @Singleton
    fun provideNoteDao(db: SemestaDatabase): NoteDao = db.noteDao()

    @Provides
    @Singleton
    fun provideNoteAttachmentDao(db: SemestaDatabase): NoteAttachmentDao = db.noteAttachmentDao()
}
