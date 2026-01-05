package com.planora.backend.service;

import com.planora.backend.dto.AnalyticsSummary;
import com.planora.backend.dto.WeeklyProgress;
import com.planora.backend.model.Task;
import com.planora.backend.model.User;
import com.planora.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final TaskRepository taskRepo;

    public AnalyticsService(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    // 1️⃣ Overall analytics
    public AnalyticsSummary getSummary(User user) {

        List<Task> tasks = taskRepo.findByUser(user);

        int total = tasks.size();
        int completed = (int) tasks.stream().filter(Task::isCompleted).count();
        int pending = total - completed;

        AnalyticsSummary summary = new AnalyticsSummary();
        summary.totalTasks = total;
        summary.completedTasks = completed;
        summary.pendingTasks = pending;
        summary.completionPercentage =
                total == 0 ? 0 : (completed * 100.0) / total;

        // Focus score logic
        summary.focusScore = calculateFocusScore(tasks);

        return summary;
    }

    // 2️⃣ Weekly progress
    public List<WeeklyProgress> getWeeklyProgress(User user) {

        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);

        List<Task> tasks = taskRepo.findByUser(user);

        Map<LocalDate, Long> completedPerDay =
                tasks.stream()
                        .filter(Task::isCompleted)
                        .filter(t -> t.getUpdatedAt() != null)
                        .filter(t -> !t.getUpdatedAt().toLocalDate().isBefore(start))
                        .collect(Collectors.groupingBy(
                                t -> t.getUpdatedAt().toLocalDate(),
                                Collectors.counting()
                        ));

        List<WeeklyProgress> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = start.plusDays(i);
            WeeklyProgress wp = new WeeklyProgress();
            wp.date = date;
            wp.completedCount =
                    completedPerDay.getOrDefault(date, 0L).intValue();
            result.add(wp);
        }

        return result;
    }

    // 3️⃣ Focus score (simple & explainable)
    private int calculateFocusScore(List<Task> tasks) {

        if (tasks.isEmpty()) return 0;

        long highPriority = tasks.stream()
                .filter(t -> "HIGH".equalsIgnoreCase(t.getPriority()))
                .count();

        long completedHigh = tasks.stream()
                .filter(Task::isCompleted)
                .filter(t -> "HIGH".equalsIgnoreCase(t.getPriority()))
                .count();

        if (highPriority == 0) return 80;

        return (int) ((completedHigh * 100.0) / highPriority);
    }
}
