package com.example.demo.Services;

import com.example.demo.DTOs.TaskRequest;
import com.example.demo.DTOs.TaskResponse;
import com.example.demo.Entities.Priority;
import com.example.demo.Entities.Status;
import com.example.demo.Entities.Task;
import com.example.demo.Entities.User;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(TaskRequest request, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setPriority(request.getPriority());
        task.setStatus(Status.PENDING);
        task.setUser(user);
        return taskRepository.save(task);
    }

    public List<TaskResponse> getTasks(String email, Status status, Priority priority) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with Email:" +email));

        List<Task> tasks = (status != null && priority != null)
                ? taskRepository.findByUserAndStatusAndPriority(user, status, priority)
                : taskRepository.findByUser(user);

        return tasks.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public TaskResponse updateTask(Long id, TaskRequest request, String useremail) {
        Task task = getUserTask(id, useremail);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setPriority(request.getPriority());
        return mapToResponse(taskRepository.save(task));
    }

    public void deleteTask(Long id, String username) {
        Task task = getUserTask(id, username);
        taskRepository.delete(task);
    }

    public TaskResponse markAsDone(Long id, String username) {
        Task task = getUserTask(id, username);
        task.setStatus(Status.COMPLETED);
        return mapToResponse(taskRepository.save(task));
    }
    public TaskResponse markAsPIP(Long id, String username) {
        Task task = getUserTask(id, username);
        task.setStatus(Status.IN_PROGRESS);
        return mapToResponse(taskRepository.save(task));
    }
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(Status.valueOf(status));
    }

    private Task getUserTask(Long id, String useremail) {
        User user = userRepository.findByEmail(useremail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }
        return task;
    }

    public TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setDueDate(task.getDueDate());
        response.setPriority(task.getPriority());
        response.setStatus(task.getStatus());
        return response;
    }
}
