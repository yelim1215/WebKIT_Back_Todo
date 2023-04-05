package com.example.todo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.TodoDTO;
import com.example.todo.model.TodoEntity;
import com.example.todo.service.TodoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService service;
	
	@PostMapping
	public ResponseEntity<?>createTodo(@RequestBody TodoDTO dto) {
		try {
			log.info("Log : createTodo entrance");
			
			// dto 를 이용해 태이블에 저장하기 위한 entity를 생성하나다.
			TodoEntity entity = TodoDTO.toEntity(dto);
			log.info("Log : dto => entity ok!");
			
			// entity userId를 임시로 저장한다.
			entity.setUserId("temporary-user");
			
			// service.create 를 통해 repository에 entity를 저장한다.
			// 이때 넘어노는 값이 없을 수도 있으므로 List가 아닌 Optional 로 한다.
			Optional<TodoEntity> entities = service.create(entity);
			log.info("Log : service.create ok!");
			
			// entities 를 dtos 로 스트림 변환한다,
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			log.info("Log : entities => dtos ok!");
			
			// Response DTO를 생성한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			log.info("Log : responsed to ok!");
			
			// HTTP Status 200 상태로 response를 전송한다.
			return ResponseEntity.ok().body(response);
			}
		catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping
	public ResponseEntity<?>retrieveTodoList() {
		String temporaryUserId = "temporary-user";
		List<TodoEntity> entities = service.retrieve(temporaryUserId);
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// HTTP Status 200 상태로 response를 전송한다.
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/update")
	public ResponseEntity<?>update(@RequestBody TodoDTO dto) {
		try {
			// dto 를 이용해 테이블에 저장하기 위한 entity를 생성한다.
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// entity userId를 임시로 지정한다.
			entity.setUserId("temporary-user");
			
			// service.create 를 통해 repository 에 entity를 저장한다.
			// 이때 넘어노는 값이 없을 수도 있으므로 List가 아닌 Optional로 한다.
			Optional<TodoEntity> entities = service.update(entity);
			
			// entites 를 dtos로 스트림 변환한다.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// Response DTO 를 생성한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// HTTP Status 200 상태로 response를 전송한다.
			return ResponseEntity.ok().body(response);
		}
		catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PutMapping
	public ResponseEntity<?>updateTodo(@RequestBody TodoDTO dto) {
		try {
			// dto 를 이용해 테이블에 저장하기 위한 entity를 생성한다.
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// entity userId를 임시로 지정한다.
			entity.setUserId("temporary-user");
			
			// service.create 를 통해 repository에 entity를 저장한다.
			// 이때 넘어오는 값이 없을 수도 있으므로 List가 아닌 Optional로 한다.
			Optional<TodoEntity> entities = service.updateTodo(entity);
			
			// entities 를 dto 로 스트림 변환한다.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// Response DTO를 생성한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// HTTP Status 200 상태로 response를 전송한다.
			return ResponseEntity.ok().body(response);
		}
		catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@DeleteMapping
	public ResponseEntity<?> delete(@RequestBody TodoDTO dto) {
		try {
			List<String> message = new ArrayList<>();
			String msg = service.delete(dto.getId());
			message.add(msg);
			// Response DTO 를 생성한다.
			ResponseDTO<String> response = ResponseDTO.<String>builder().data(message).build();
			return ResponseEntity.ok().body(response);
		}
		catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
}
