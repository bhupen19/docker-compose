package com.bezkoder.spring.datajpa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.datajpa.model.Subject;
import com.bezkoder.spring.datajpa.repository.SubjectRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")

/*        public static Number createNumber(long value, Class clazz) throws IdentifierGenerationException {
     if (clazz == Long.class) {
       return new Long(value);
     }
     if (clazz == Integer.class) {
       return new Integer((int)value);
     }
     if (clazz == Short.class) {
      return new Short((short)(int)value);
 */     
public class SubjectController {

	@Autowired
	SubjectRepository SubjectRepository;
// Making call on basis of subjects 
	@GetMapping("/subjects")
	public ResponseEntity<List<Subject>> getAllsubjects(@RequestParam(required = false) String title) {
		try {
			List<Subject> subjects = new ArrayList<Subject>();

			if (title == null)
				SubjectRepository.findAll().forEach(subjects::add);
			else
				SubjectRepository.findByTitleContaining(title).forEach(subjects::add);

			if (subjects.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(subjects, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
// Making call on basis of subjects id
	@GetMapping("/subjects/{id}")
	public ResponseEntity<Subject> getSubjectById(@PathVariable("id") long id) {
		Optional<Subject> SubjectData = SubjectRepository.findById(id);

		if (SubjectData.isPresent()) {
			return new ResponseEntity<>(SubjectData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/subjects")
	public ResponseEntity<Subject> createSubject(@RequestBody Subject Subject) {
		try {
			Subject _Subject = SubjectRepository
					.save(new Subject(Subject.getTitle(), Subject.getDescription(), false));
			return new ResponseEntity<>(_Subject, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/subjects/{id}")
	public ResponseEntity<Subject> updateSubject(@PathVariable("id") long id, @RequestBody Subject Subject) {
		Optional<Subject> SubjectData = SubjectRepository.findById(id);

		if (SubjectData.isPresent()) {
			Subject _Subject = SubjectData.get();
			_Subject.setTitle(Subject.getTitle());
			_Subject.setDescription(Subject.getDescription());
			_Subject.setPublished(Subject.isPublished());
			return new ResponseEntity<>(SubjectRepository.save(_Subject), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/subjects/{id}")
	public ResponseEntity<HttpStatus> deleteSubject(@PathVariable("id") long id) {
		try {
			SubjectRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/subjects")
	public ResponseEntity<HttpStatus> deleteAllsubjects() {
		try {
			SubjectRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/subjects/published")
	public ResponseEntity<List<Subject>> findByPublished() {
		try {
			List<Subject> subjects = SubjectRepository.findByPublished(true);

			if (subjects.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(subjects, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
