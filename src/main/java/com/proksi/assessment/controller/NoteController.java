package com.proksi.assessment.controller;

import com.proksi.assessment.component.ApiUserDetails;
import com.proksi.assessment.dto.requestdto.CreateNoteRequestDto;
import com.proksi.assessment.dto.requestdto.ListNotesRequestDto;
import com.proksi.assessment.dto.responseDto.CreateNoteResponseDto;
import com.proksi.assessment.dto.responseDto.GetNoteResponseDto;
import com.proksi.assessment.dto.responseDto.ListNotesResponseDto;
import com.proksi.assessment.enums.ResponseStatus;
import com.proksi.assessment.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }
    @Operation(
            summary = "Creates New Note",
            description = "Creates a new note to be summarized by the AI.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @PostMapping("/create")
    public ResponseEntity<?> tryNote(@AuthenticationPrincipal ApiUserDetails currentUser, @RequestBody CreateNoteRequestDto requestDto) {
        CreateNoteResponseDto responseDto = noteService.createNewNote(requestDto, currentUser.getApiUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(
            summary = "List Notes",
            description = "Fetches the list of notes that belongs to the provided user ID. Admin can query notes for all users.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @PostMapping("/list")
    public ResponseEntity<?> listNotesByUserId(@AuthenticationPrincipal ApiUserDetails currentUser, @RequestBody ListNotesRequestDto requestDto) {
        ListNotesResponseDto responseDto = noteService.fetchNotes(requestDto, currentUser.getApiUser());
        if (responseDto.getResult().getStatus() == ResponseStatus.SUCCESS) {
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
    }

    @Operation(
            summary = "Fetch A Single Note",
            description = "Fetches the information of a single note. Admin can query notes of all users.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @GetMapping("/{noteId}")
    public ResponseEntity<?> getNote(@AuthenticationPrincipal ApiUserDetails currentUser, @PathVariable Long noteId) {
        GetNoteResponseDto responseDto = noteService.fetchNoteById(noteId, currentUser.getApiUser());
        if (responseDto.getResult().getStatus() == ResponseStatus.SUCCESS) {
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
        }
    }
}
