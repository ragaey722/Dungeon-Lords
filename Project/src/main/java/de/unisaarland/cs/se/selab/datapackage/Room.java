package de.unisaarland.cs.se.selab.datapackage;

public class Room extends Tunnel {
    private final int id;
    private final RoomRestriction restriction;
    private final int impsCost;
    private final Resources rewards;
    private boolean activated;

    public Room(final int id, final RoomRestriction restriction,
                final int impsCost, final Resources rewards) {
        this.id = id;
        this.restriction = restriction;
        this.impsCost = impsCost;
        this.rewards = rewards;
        activated = false;
    }

    public int getId() {
        return id;
    }

    public RoomRestriction getRestriction() {
        return restriction;
    }

    public int getImpsCost() {
        return impsCost;
    }

    public Resources getRewards() {
        return rewards;
    }

    public boolean isActive() {
        return activated;
    }

    public void activate() {
        activated = true;
    }

    public void deActivate() {
        activated = false;
    }

    @Override
    public boolean isRoom() {
        return true;
    }
}