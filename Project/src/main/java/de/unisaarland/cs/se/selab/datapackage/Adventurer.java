package de.unisaarland.cs.se.selab.datapackage;

public class Adventurer {

    private final int id;
    private final int difficulty;
    private final int maxHP;
    private int currentHP;
    private final int healValue;
    private final int defuseValue;
    private final boolean charge;

    public Adventurer(final int id, final int diff, final int hp, final int healVal,
                      final int defuseVal, final boolean charge) {
        this.id = id;
        this.difficulty = diff;
        this.healValue = healVal;
        this.maxHP = hp;
        this.currentHP = hp;
        this.defuseValue = defuseVal;
        this.charge = charge;
    }

    public int getId() {
        return id;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getHealValue() {
        return healValue;
    }

    public int getDefuseValue() {
        return defuseValue;
    }

    public boolean getCharge() {
        return charge;
    }

    public int getHP() {
        return currentHP;
    }

    /**
     * applies damage to the adventurer
     *
     * @param damage amount of damage dealt to this adventure
     * @return the damage amount if the damage is less than the current hp
     * otherwise the current HP
     */
    public int takeDamage(final int damage) {
        if (damage < this.currentHP) {
            this.currentHP = currentHP - damage;
            return damage;
        } else {
            final int nowHP = this.currentHP;
            this.currentHP = 0;
            return nowHP;
        }
    }

    /**
     * applies healing to the adventurer
     *
     * @param healValue the amount of heal given to this adventurer
     * @return the healValue if it is less than the missing HP
     * Otherwise return the amount of missing hp
     */
    public int takeHeal(final int healValue) {
        if (this.currentHP <= 0) {
            return 0;
        } else {
            final int couldHeal = this.maxHP - this.currentHP;
            if (healValue > couldHeal) {
                this.currentHP = this.maxHP;
                return couldHeal;
            } else {
                this.currentHP = currentHP + healValue;
                return healValue;
            }
        }
    }


}
