package com.proksi.assessment.component;

import com.proksi.assessment.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SummarizeModelScheduler {
    private final NoteService noteService;

    @Autowired
    public SummarizeModelScheduler(NoteService noteService) {
        this.noteService = noteService;
    }

    @Scheduled(fixedRate = 30000)
    public void processNextNote() {
        noteService.summarizeNote();
    }
}
