package com.devtrack.service;

import com.devtrack.dto.AnalyticsDTO;
import com.devtrack.model.CodingSession;
import com.devtrack.model.User;
import com.devtrack.repository.SessionRepository;
import com.devtrack.repository.TaskRepository;
import com.devtrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get complete analytics for user
     */
    public AnalyticsDTO getAnalytics(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AnalyticsDTO analytics = new AnalyticsDTO();

        // Calculate success rate
        long totalTasks = taskRepository.countByUser(user);
        long completedTasks = taskRepository.countByUserAndStatus(user, "DONE");
        if (totalTasks > 0) {
            analytics.setSuccessRate((completedTasks * 100.0) / totalTasks);
        } else {
            analytics.setSuccessRate(0.0);
        }

        // Calculate streaks
        calculateStreaks(user, analytics);

        // Calculate time distributions
        analytics.setTimeByCategory(getTimeByCategory(user));
        analytics.setTimeByProject(getTimeByProject(user));
        analytics.setTimeByWorkType(getTimeByWorkType(user));

        // Calculate focus metrics
        Map<String, Integer> timeByProject = getTimeByProject(user);
        if (!timeByProject.isEmpty()) {
            analytics.setTopProject(
                timeByProject.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null)
            );
        }

        Map<String, Integer> timeByCategory = getTimeByCategory(user);
        if (!timeByCategory.isEmpty()) {
            analytics.setTopCategory(
                timeByCategory.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null)
            );
        }

        // Calculate estimation accuracy
        Double avgError = taskRepository.getAverageEstimationError(user);
        if (avgError != null) {
            analytics.setAverageEstimationError(avgError.intValue());
            // Calculate accuracy as percentage (inverse of error)
            // If tasks are estimated perfectly, error = 0, accuracy = 100%
            // This is a simplified metric
            analytics.setEstimationAccuracy(Math.max(0, 100.0 - (avgError / 10.0)));
        } else {
            analytics.setEstimationAccuracy(null);
            analytics.setAverageEstimationError(null);
        }

        return analytics;
    }

    /**
     * Calculate current and longest streaks
     */
    private void calculateStreaks(User user, AnalyticsDTO analytics) {
        LocalDate startDate = LocalDate.now().minusDays(90); // Check last 90 days
        List<CodingSession> sessions = sessionRepository.findSessionsAfterDate(user, startDate);

        if (sessions.isEmpty()) {
            analytics.setCurrentStreak(0);
            analytics.setLongestStreak(0);
            return;
        }

        // Group sessions by date
        Map<LocalDate, Boolean> sessionDates = new HashMap<>();
        for (CodingSession session : sessions) {
            sessionDates.put(session.getSessionDate(), true);
        }

        // Calculate current streak (from today backwards)
        int currentStreak = 0;
        LocalDate checkDate = LocalDate.now();
        while (sessionDates.containsKey(checkDate)) {
            currentStreak++;
            checkDate = checkDate.minusDays(1);
        }

        // Calculate longest streak
        int longestStreak = 0;
        int tempStreak = 0;
        LocalDate date = startDate;
        while (!date.isAfter(LocalDate.now())) {
            if (sessionDates.containsKey(date)) {
                tempStreak++;
                longestStreak = Math.max(longestStreak, tempStreak);
            } else {
                tempStreak = 0;
            }
            date = date.plusDays(1);
        }

        analytics.setCurrentStreak(currentStreak);
        analytics.setLongestStreak(longestStreak);
    }

    /**
     * Get time distribution by category from tasks
     */
    private Map<String, Integer> getTimeByCategory(User user) {
        List<Object[]> results = taskRepository.countTasksByCategory(user);
        Map<String, Integer> distribution = new HashMap<>();
        
        for (Object[] result : results) {
            String category = (String) result[0];
            Long count = (Long) result[1];
            if (category != null) {
                distribution.put(category, count.intValue());
            }
        }
        
        return distribution;
    }

    /**
     * Get time distribution by project from sessions
     */
    private Map<String, Integer> getTimeByProject(User user) {
        List<Object[]> results = sessionRepository.getTimeByProject(user);
        Map<String, Integer> distribution = new HashMap<>();
        
        for (Object[] result : results) {
            String project = (String) result[0];
            Long minutes = (Long) result[1];
            distribution.put(project, minutes.intValue());
        }
        
        return distribution;
    }

    /**
     * Get time distribution by work type from sessions
     */
    private Map<String, Integer> getTimeByWorkType(User user) {
        List<Object[]> results = sessionRepository.getTimeByWorkType(user);
        Map<String, Integer> distribution = new HashMap<>();
        
        for (Object[] result : results) {
            String workType = (String) result[0];
            Long minutes = (Long) result[1];
            distribution.put(workType, minutes.intValue());
        }
        
        return distribution;
    }
}
