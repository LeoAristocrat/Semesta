package com.leoaristocrat.semesta.feature_notes.data.local

import com.leoaristocrat.semesta.feature_notes.domain.AttachmentKind
import com.leoaristocrat.semesta.feature_notes.domain.NoteAttachment

fun NoteAttachmentEntity.toDomain(): NoteAttachment = NoteAttachment(
    id = id,
    noteId = noteId,
    kind = runCatching { AttachmentKind.valueOf(kind) }.getOrDefault(AttachmentKind.FILE),
    displayName = displayName,
    storedName = storedName,
    mimeType = mimeType,
    sizeBytes = sizeBytes,
    durationMillis = durationMillis,
    createdAt = createdAt
)

fun NoteAttachment.toEntity(userId: String): NoteAttachmentEntity = NoteAttachmentEntity(
    id = id,
    userId = userId,
    noteId = noteId,
    kind = kind.name,
    displayName = displayName,
    storedName = storedName,
    mimeType = mimeType,
    sizeBytes = sizeBytes,
    durationMillis = durationMillis,
    createdAt = createdAt
)
