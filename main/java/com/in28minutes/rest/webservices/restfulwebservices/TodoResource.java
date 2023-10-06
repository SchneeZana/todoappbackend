package com.in28minutes.rest.webservices.restfulwebservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.System.*;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class TodoResource {
    @Autowired
    private TodoHardcodedService todoService;

    @GetMapping("/users/{username}/todos")
    public List<Todo> getAllTodos(@PathVariable String username) {
        return todoService.findAll();

    }

    @GetMapping("/users/{username}/todos/{id}")
    public List<Todo> getTodo(@PathVariable String username, @PathVariable long id) {
        return Collections.singletonList(todoService.findById(id));

    }


    @DeleteMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String username,
                                           @PathVariable long id) {
        Todo todo = todoService.deleteById(id);
        if (todo != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{username}/todos/{id}")
    public ResponseEntity<?> updateTodo(
            @PathVariable String username,
            @PathVariable long id,
            @RequestBody Todo todo) {

        // Basic validation
        if (todo == null || id <= 0) {
            return new ResponseEntity<String>("Invalid input", HttpStatus.BAD_REQUEST);
        }

        try {
            todo.setId(id);
            Todo updatedTodo = todoService.save(todo);
            return new ResponseEntity<Todo>(updatedTodo, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return new ResponseEntity<String>("Error updating Todo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/users/{username}/todos")
    public ResponseEntity<Void> updateTodo(@PathVariable String username,

                                           @RequestBody Todo todo) {
        Todo createdTodo = todoService.save(todo);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdTodo.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }
}
