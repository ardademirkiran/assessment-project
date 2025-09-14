package com.proksi.assessment.repository;

import com.proksi.assessment.entity.ApiUser;
import com.proksi.assessment.entity.Note;
import com.proksi.assessment.enums.NoteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findFirstByStatusOrderByCreatedAtAsc(NoteStatus status);

    Page<Note> findAllByApiUser(ApiUser apiUser, Pageable pageable);

    Page<Note> findAllByApiUser_Id(Long userId, Pageable pageable);
}
