package hr.algebra.airhockey.models;

public class Move {
    private final int gameSecond;
    private final GoalType goalType;


    public Move(final int gameSecond, final GoalType goalType) {
        this.gameSecond = gameSecond;
        this.goalType = goalType;
    }

    public int getGameSecond() {
        return gameSecond;
    }

    public GoalType getGoalType() {
        return goalType;
    }
}
