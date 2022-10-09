package datatest;

import de.unisaarland.cs.se.selab.datapackage.Adventurer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AdventurerTest {


    @Test
    void testAdventurerSetGet() {
        final Adventurer a = new Adventurer(1, 2, 3, 1, 2, true);

        Assertions.assertEquals(1, a.getId());
        Assertions.assertEquals(2, a.getDifficulty());
        Assertions.assertEquals(3, a.getHP());
        Assertions.assertEquals(1, a.getHealValue());
        Assertions.assertEquals(2, a.getDefuseValue());
        Assertions.assertTrue(a.getCharge());
    }

    @Test
    void testAdventurerTakeDamage() {
        final Adventurer a = new Adventurer(1, 2, 3, 1, 2, true);

        final int d1 = a.takeDamage(2);
        Assertions.assertEquals(2, d1);
        Assertions.assertEquals(1, a.getHP());

        final int d2 = a.takeDamage(3);
        Assertions.assertEquals(1, d2);
        Assertions.assertEquals(0, a.getHP());

        final int d3 = a.takeDamage(2);
        Assertions.assertEquals(0, d3);
        Assertions.assertEquals(0, a.getHP());
    }

    @Test
    void testAdventurerTakeHeal() {
        final Adventurer a = new Adventurer(1, 2, 3, 1, 2, true);

        a.takeDamage(2);
        final int h1 = a.takeHeal(1);
        Assertions.assertEquals(1, h1);
        Assertions.assertEquals(2, a.getHP());
        final int h2 = a.takeHeal(3);
        Assertions.assertEquals(1, h2);
        Assertions.assertEquals(3, a.getHP());
        final int h3 = a.takeHeal(3);
        Assertions.assertEquals(0, h3);
        Assertions.assertEquals(3, a.getHP());

        a.takeDamage(3);
        final int h4 = a.takeHeal(3);
        Assertions.assertEquals(0, h4);
        Assertions.assertEquals(0, a.getHP());
    }

}
