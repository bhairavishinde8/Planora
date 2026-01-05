package com.planora.backend.service;

import com.planora.backend.dto.TaskRequest;
import com.planora.backend.model.Task;
import com.planora.backend.model.User;
import com.planora.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepo;

    public TaskService(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    public Task createTask(TaskRequest req, User user) {
        Task task = new Task();
        task.setTitle(req.title);
        task.setDescription(req.description);
        task.setPriority(req.priority);
        task.setDueDate(req.dueDate);
        task.setUser(user);
        return taskRepo.save(task);
    }

    public List<Task> getUserTasks(User user) {
        return taskRepo.findByUser(user);
    }

    public Task updateTask(Long taskId, TaskRequest req, User user) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        task.setTitle(req.title);
        task.setDescription(req.description);
        task.setPriority(req.priority);
        task.setDueDate(req.dueDate);

        return taskRepo.save(task);
    }

    public void deleteTask(Long taskId, User user) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        taskRepo.delete(task);
    }

    public Task updateStatus(Long taskId, boolean completed, User user) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        task.setCompleted(completed);
        return taskRepo.save(task);
    }


}
