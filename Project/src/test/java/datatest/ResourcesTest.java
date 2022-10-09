package datatest;

import de.unisaarland.cs.se.selab.datapackage.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourcesTest {
    @Test
    void checkCanSubtractCoinTest() {
        final Resources reDungeonLord = new Resources(0, 0, 0, 0);
        final Resources reSubtract = new Resources(1, 0, 0, 0);
        Assertions.assertFalse(reDungeonLord.canResourceBeSubtracted(reSubtract));
    }

    @Test
    void checkCanSubtractEvilnessTest() {
        final Resources reDungeonLord = new Resources(0, 15, 0, 0);
        final Resources reSubtract = new Resources(0, 1, 0, 0);
        Assertions.assertFalse(reDungeonLord.canResourceBeSubtracted(reSubtract));
    }

    @Test
    void checkCanSubtractImpsTest() {
        final Resources reDungeonLord = new Resources(0, 0, 4, 0);
        final Resources reSubtract = new Resources(0, 0, 4, 0);
        Assertions.assertTrue(reDungeonLord.canResourceBeSubtracted(reSubtract));
    }

    @Test
    void checkSubtractTest4() {
        final Resources reDungeonLord = new Resources(0, 0, 3, 0);
        final Resources reSubtract = new Resources(0, 0, 3, 0);
        Assertions.assertTrue(reDungeonLord.canResourceBeSubtracted(reSubtract));
    }

    @Test
    void checkSubtractTest5() {
        final Resources reDungeonLord = new Resources(0, 15, 0, 0);
        final Resources reSubtract = new Resources(0, 1, 0, 0);
        Assertions.assertFalse(reDungeonLord.canResourceBeSubtracted(reSubtract));
    }

    @Test
    void downEvilnessTest() {
        final Resources reDungeonLord = new Resources(0, 0, 0, 0);
        Assertions.assertFalse(reDungeonLord.downEvilness(1));
    }

}
