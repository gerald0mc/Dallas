package me.gerald.dallas.utils;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.behavior.IPathingBehavior;
import baritone.api.pathing.goals.GoalInverted;
import baritone.api.pathing.goals.GoalXZ;
import baritone.api.process.ICustomGoalProcess;

public class BaritoneHelper {
    public static IBaritone getBaritone() {
        return BaritoneAPI.getProvider().getPrimaryBaritone();
    }

    public static ICustomGoalProcess getGoalProcess() {
        return getBaritone().getCustomGoalProcess();
    }

    public static IPathingBehavior getPathingBehavior() {
        return getBaritone().getPathingBehavior();
    }

    public static void gotoPos(int x, int z) {
        getGoalProcess().setGoalAndPath(new GoalXZ(x, z));
    }

    public static void gotoPosInverted(int x, int z) {
        getGoalProcess().setGoalAndPath(new GoalInverted(new GoalXZ(x, z)));
    }

    public static void stop() {
        getPathingBehavior().cancelEverything();
    }

    public static boolean isActive() {
        return getGoalProcess().isActive();
    }
}
