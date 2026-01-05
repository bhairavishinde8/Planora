package com.planora.backend.service;

import com.planora.backend.model.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskRuleEngine {

    public void applyRules(List<Task> tasks) {

        LocalDate today = LocalDate.now();

        for (Task task : tasks) {

            // Rule 1: Overdue task
            if (!task.isCompleted() && task.getDueDate().isBefore(today)) {
                task.setPriority("HIGH");
            }

            // Rule 2: Due today
            else if (!task.isCompleted() && task.getDueDate().isEqual(today)) {
                task.setPriority("HIGH");
            }

            // Rule 3: Future task
            else if (!task.isCompleted() && task.getDueDate().isAfter(today)) {
                if (task.getPriority() == null) {
                    task.setPriority("MEDIUM");
                }
            }
            //auto move overdue tasks
            if (!task.isCompleted() && task.getDueDate().isBefore(today)) {
                task.setPriority("HIGH");
                task.setDueDate(today.plusDays(1));  // 🔥 auto-move
            }

        }
    }
}
