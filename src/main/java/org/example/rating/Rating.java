package org.example.rating;

import org.example.model.UserAction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Rating {
    static final int MAX_RATING_COUNT = 10;
    private List<Statistics> statisticsList = new ArrayList<>();
    private Statistics currentStats = new Statistics();

    public List<Statistics> getStatisticsList() {
        return statisticsList;
    }

    public void setStatisticsList(List<Statistics> statisticsList) {
        this.statisticsList = statisticsList;
    }

    public Statistics getCurrentStats() {
        statisticsList.sort(Comparator.comparing(Statistics::getTreasureCount).reversed());
        return currentStats;
    }

    public void setCurrentStats(Statistics currentStats) {
        this.currentStats = currentStats;
    }

    public boolean isExitRating(UserAction action) {
        return action == UserAction.EXIT;
    }

    public void addStatics() {
        statisticsList.add(currentStats);
        statisticsList.sort(Comparator.comparing(Statistics::getTreasureCount).reversed());

        if (statisticsList.size() > MAX_RATING_COUNT) {
            statisticsList.remove(statisticsList.size() - 1);
        }
    }
}
