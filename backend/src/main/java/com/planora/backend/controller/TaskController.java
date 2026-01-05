package com.planora.backend.controller;

import com.planora.backend.dto.TaskRequest;
import com.planora.backend.model.Task;
import com.planora.backend.model.User;
import com.planora.backend.repository.UserRepository;
import com.planora.backend.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepo;

    public TaskController(TaskService taskService, UserRepository userRepo) {
        this.taskService = taskService;
        this.userRepo = userRepo;
    }

    @PostMapping
    public Object createTask(@RequestBody TaskRequest req, Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();
        return taskService.createTask(req, user);
    }

    @GetMapping
    public Object getTasks(Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();
        return taskService.getUserTasks(user);
    }
    @PutMapping("/{id}")
    public Object updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest req,
            Principal principal
    ) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();
        return taskService.updateTask(id, req, user);
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id, Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();
        taskService.deleteTask(id, user);
        return "Task deleted";
    }

    @PatchMapping("/{id}/status")
    public Object updateStatus(
            @PathVariable Long id,
            @RequestParam boolean completed,
            Principal principal
    ) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();
        return taskService.updateStatus(id, completed, user);
    }

    @GetMapping("/today")
    public List<Task> getTodayTasks(Principal principal) {

        User user = userRepo.findByEmail(principal.getName()).orElseThrow();

        return taskService.getUserTasks(user).stream()
                .filter(task -> task.getDueDate().isEqual(java.time.LocalDate.now()))
                .toList();
    }

    @GetMapping("/stats")
    public Object getStats(Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();
        return taskService.getProductivity(user);
    }

    @GetMapping("/filter")
    public List<Task> filterTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String priority,
            Principal principal
    ) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();

        return taskService.getUserTasks(user).stream()
                .filter(task -> completed == null || task.isCompleted() == completed)
                .filter(task -> priority == null || task.getPriority().equalsIgnoreCase(priority))
                .toList();
    }


}
