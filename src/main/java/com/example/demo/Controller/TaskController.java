package com.example.demo.Controller;

import com.example.demo.DTOs.TaskRequest;
import com.example.demo.DTOs.TaskResponse;
import com.example.demo.Entities.Priority;
import com.example.demo.Entities.Task;
import com.example.demo.Services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Entities.Status;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse create(@RequestBody TaskRequest request, Authentication auth) {
        return taskService.mapToResponse(taskService.createTask(request, auth.getName()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        List<Task> tasks = taskService.getTasksByStatus(status.toUpperCase());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping
    public List<TaskResponse> getAll(@RequestParam(required = false) Status status,
                                     @RequestParam(required = false) Priority priority,
                                     Authentication auth) {
        return taskService.getTasks(auth.getName(), status, priority);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @RequestBody TaskRequest request, Authentication auth) {
        return taskService.updateTask(id, request, auth.getName());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        taskService.deleteTask(id, auth.getName());
    }

    @PatchMapping("/{id}/done")
    public TaskResponse markDone(@PathVariable Long id, Authentication auth) {
        return taskService.markAsDone(id, auth.getName());
    }
}
