package com.example.demo.Repository;

import com.example.demo.Entities.Priority;
import com.example.demo.Entities.Status;
import com.example.demo.Entities.Task;
import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByUserAndStatusAndPriority(User user, Status status, Priority priority);
    List<Task> findByUser(User user);
    List<Task> findByStatus(Status status);

}
