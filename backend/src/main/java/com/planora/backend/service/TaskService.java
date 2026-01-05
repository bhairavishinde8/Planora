package com.planora.backend.service;

import com.planora.backend.dto.ProductivityStats;
import com.planora.backend.dto.TaskRequest;
import com.planora.backend.model.Task;
import com.planora.backend.model.User;
import com.planora.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepo;
    private final TaskRuleEngine ruleEngine;


    public TaskService(TaskRepository taskRepo, TaskRuleEngine ruleEngine) {
        this.taskRepo = taskRepo;
        this.ruleEngine = ruleEngine;
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
        List<Task> tasks = taskRepo.findByUser(user);

        ruleEngine.applyRules(tasks);
        tasks.sort((t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()));
        return tasks;
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

    public ProductivityStats getProductivity(User user) {

        var tasks = taskRepo.findByUser(user);

        ProductivityStats stats = new ProductivityStats();
        stats.totalTasks = tasks.size();
        stats.completedTasks =
                (int) tasks.stream().filter(Task::isCompleted).count();

        if (stats.totalTasks == 0) {
            stats.completionRate = 0;
        } else {
            stats.completionRate =
                    (stats.completedTasks * 100.0) / stats.totalTasks;
        }

        return stats;
    }


}
