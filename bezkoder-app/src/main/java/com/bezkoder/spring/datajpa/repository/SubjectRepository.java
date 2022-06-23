package com.bezkoder.spring.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.datajpa.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
	List<Subject> findByPublished(boolean published);
	List<Subject> findByTitleContaining(String title);
}
