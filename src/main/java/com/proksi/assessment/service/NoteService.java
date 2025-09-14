package com.proksi.assessment.service;

import com.proksi.assessment.constant.MessageConstants;
import com.proksi.assessment.dto.requestdto.CreateNoteRequestDto;
import com.proksi.assessment.dto.requestdto.ListNotesRequestDto;
import com.proksi.assessment.dto.responseDto.CreateNoteResponseDto;
import com.proksi.assessment.dto.responseDto.GetNoteResponseDto;
import com.proksi.assessment.dto.responseDto.ListNotesResponseDto;
import com.proksi.assessment.dto.responseDto.SummaryResponse;
import com.proksi.assessment.entity.ApiUser;
import com.proksi.assessment.entity.Note;
import com.proksi.assessment.enums.NoteStatus;
import com.proksi.assessment.enums.ResponseStatus;
import com.proksi.assessment.enums.Role;
import com.proksi.assessment.exception.NoResponseFromModelException;
import com.proksi.assessment.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final RestTemplate restTemplate;

    @Value("${summarizer-model-path}")
    private String MODEL_PATH;

    @Value("${summarizer-secret-key}")
    private String MODEL_API_KEY;

    @Autowired
    public NoteService(NoteRepository noteRepository, RestTemplate restTemplate) {
        this.noteRepository = noteRepository;
        this.restTemplate = restTemplate;
    }

    public CreateNoteResponseDto createNewNote(CreateNoteRequestDto noteDto, ApiUser apiUser) {
        Note note = new Note();
        note.setRawText(noteDto.getText());
        note.setStatus(NoteStatus.QUEUED);
        note.setApiUser(apiUser);
        noteRepository.save(note);

        CreateNoteResponseDto responseDto = new CreateNoteResponseDto(ResponseStatus.SUCCESS, MessageConstants.NOTE_CREATED_SUCCESSFULLY);
        responseDto.setNoteId(note.getId());

        return responseDto;
    }

    public ListNotesResponseDto fetchNotes(ListNotesRequestDto requestDto, ApiUser apiUser) {
        if (apiUser.getRole() != Role.ADMIN && !Objects.equals(apiUser.getId(), requestDto.getUserId())) {
            throw new AccessDeniedException("Access denied");
        }
        Pageable pageable = PageRequest.of(requestDto.getPageNum(), requestDto.getSize());
        Page<Note> page = noteRepository.findAllByApiUser_Id(requestDto.getUserId(), pageable);
        List<Note> notes = page.getContent();
        ListNotesResponseDto responseDto = new ListNotesResponseDto(ResponseStatus.SUCCESS, MessageConstants.NOTES_FETCHED_SUCCESSFULLY);
        responseDto.setNotes(notes);
        return responseDto;
    }

    public GetNoteResponseDto fetchNoteById(Long noteId, ApiUser apiUser) {
        Optional<Note> noteOpt = noteRepository.findById(noteId);
        if (noteOpt.isPresent()) {
            Note note = noteOpt.get();
            if (note.getApiUser().getId().equals(apiUser.getId())) {
                GetNoteResponseDto getNoteResponseDto = new GetNoteResponseDto(ResponseStatus.SUCCESS, MessageConstants.NOTE_IS_FOUND);
                getNoteResponseDto.setNote(note);
                return getNoteResponseDto;
            } else {
                throw new AccessDeniedException("Access denied");
            }
        } else {
            return new GetNoteResponseDto(ResponseStatus.FAILURE, MessageConstants.NOTE_NOT_FOUND);
        }
    }

    private Note dequeueNextNote() {
        Optional<Note> noteToSummarize = noteRepository.findFirstByStatusOrderByCreatedAtAsc(NoteStatus.QUEUED);
        if (noteToSummarize.isPresent()) {
            Note targetNote = noteToSummarize.get();
            targetNote.setStatus(NoteStatus.PROCESSING);
            targetNote.setStartedAt(LocalDateTime.now());
            noteRepository.save(targetNote);
            return targetNote;
        }
        return null;
    }


    public void summarizeNote() {
        Note targetNote = dequeueNextNote();
        if (targetNote == null) {
            return;
        }
        try {
            String summary = getSummaryFromModel(targetNote);
            if (summary != null) {
                targetNote.setSummary(summary);
                targetNote.setStatus(NoteStatus.DONE);
                targetNote.setFinishedAt(LocalDateTime.now());
            } else {
                targetNote.setStatus(NoteStatus.FAILED);
            }
        } catch (Exception e) {
            targetNote.setStatus(NoteStatus.FAILED);
            targetNote.setFinishedAt(LocalDateTime.now());
            e.printStackTrace();
        } finally {
            noteRepository.save(targetNote);
        }
    }


    private String getSummaryFromModel(Note note) {
        Map<String, Object> requestBody = Map.of(
                "text", note.getRawText(),
                "max_length", 60,
                "min_length", 10
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", MODEL_API_KEY);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {

            ResponseEntity<SummaryResponse> response = restTemplate.postForEntity(
                    MODEL_PATH + "/summarize", request, SummaryResponse.class);

            if (response.getBody() == null || response.getBody().getSummary() == null || !response.getStatusCode().is2xxSuccessful()) {
                throw new NoResponseFromModelException(MessageConstants.NO_RESPONSE_FROM_MODEL);
            }
            return response.getBody().getSummary();
        } catch (Exception e) {
            throw new NoResponseFromModelException(MessageConstants.NO_RESPONSE_FROM_MODEL);
        }
    }
}
