package com.planora.backend.scheduler;

import com.planora.backend.model.Task;
import com.planora.backend.model.User;
import com.planora.backend.repository.TaskRepository;
import com.planora.backend.repository.UserRepository;
import com.planora.backend.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DailySummaryScheduler {

    private final UserRepository userRepo;
    private final TaskRepository taskRepo;
    private final EmailService emailService;

    public DailySummaryScheduler(UserRepository userRepo,
                                 TaskRepository taskRepo,
                                 EmailService emailService) {
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.emailService = emailService;
    }

    // Daily summary at 8 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailySummary() {

        List<User> users = userRepo.findAll();

        for (User user : users) {

            List<Task> tasks = taskRepo.findByUser(user);

            long todayTasks = tasks.stream()
                    .filter(t -> t.getDueDate().isEqual(LocalDate.now()))
                    .count();

            long overdueTasks = tasks.stream()
                    .filter(t -> !t.isCompleted()
                            && t.getDueDate().isBefore(LocalDate.now()))
                    .count();

            String body = """
                    Hello %s,

                    Here is your daily task summary:
                    - Tasks due today: %d
                    - Overdue tasks: %d

                    Stay productive!
                    """.formatted(user.getName(), todayTasks, overdueTasks);

            emailService.sendEmail(
                    user.getEmail(),
                    "Your Daily Task Summary",
                    body
            );
        }
    }
    // Alerts every hour
    @Scheduled(cron = "0 0 * * * ?")
    public void sendDueAlerts() {

        List<User> users = userRepo.findAll();

        for (User user : users) {

            List<Task> tasks = taskRepo.findByUser(user);

            for (Task task : tasks) {

                if (!task.isCompleted()
                        && task.getDueDate().isEqual(LocalDate.now())) {

                    emailService.sendEmail(
                            user.getEmail(),
                            "Task Due Today",
                            "Reminder: Your task '" + task.getTitle() + "' is due today."
                    );
                }

                if (!task.isCompleted()
                        && task.getDueDate().isBefore(LocalDate.now())) {

                    emailService.sendEmail(
                            user.getEmail(),
                            "Task Overdue",
                            "Warning: Your task '" + task.getTitle() + "' is overdue."
                    );
                }
            }
        }
    }

}

