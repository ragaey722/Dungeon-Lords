package datatest;

import de.unisaarland.cs.se.selab.datapackage.Dungeon;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import de.unisaarland.cs.se.selab.datapackage.Room;
import de.unisaarland.cs.se.selab.datapackage.RoomRestriction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class DungeonTest {


    @Test
    void testSetBattleDigTunnelBuildRoom() {

        final Dungeon d = new Dungeon(6);
        Assertions.assertTrue(d.checkSetBattleGround(0, 0));

        d.conquerCurrentTile();
        Assertions.assertFalse(d.checkSetBattleGround(0, 0));

        Assertions.assertTrue(d.digTunnel(0, 1));
        Assertions.assertTrue(d.digTunnel(1, 0));
        Assertions.assertTrue(d.checkSetBattleGround(0, 1));
        d.conquerCurrentTile();
        Assertions.assertTrue(d.checkSetBattleGround(1, 0));
        d.conquerCurrentTile();

        Assertions.assertFalse(d.checkSetBattleGround(0, 1));
        Assertions.assertFalse(d.checkSetBattleGround(1, 0));
        Assertions.assertTrue(d.digTunnel(0, 2));
        Assertions.assertTrue(d.digTunnel(0, 3));
        Assertions.assertFalse(d.checkSetBattleGround(0, 3));
        Assertions.assertTrue(d.checkSetBattleGround(0, 2));
        d.conquerCurrentTile();
        Assertions.assertTrue(d.checkSetBattleGround(0, 3));
        d.conquerCurrentTile();
        Assertions.assertFalse(d.checkSetBattleGround(5, 5));
        Assertions.assertFalse(d.digTunnel(5, 5));
        Assertions.assertFalse(d.digTunnel(1, 1));
        Assertions.assertTrue(d.digTunnel(1, 2));
        Assertions.assertFalse(d.digTunnel(-1, 4));
        final Resources res = new Resources(0, 0, 0, 0);
        Assertions.assertTrue(d.digTunnel(0, 4));
        Assertions.assertTrue(d.digTunnel(1, 4));
        Assertions.assertTrue(d.digTunnel(2, 2));
        Assertions.assertTrue(d.digTunnel(3, 2));
        Assertions.assertTrue(d.digTunnel(2, 1));
        Assertions.assertTrue(d.digTunnel(2, 3));
        final Room upperRoom = new Room(1, RoomRestriction.UPPER_HALF, 0, res);
        Assertions.assertFalse(d.buildRoom(1, 4, upperRoom)
                || d.buildRoom(0, 2, upperRoom));
        final Room outerRoom = new Room(2, RoomRestriction.OUTER, 0, res);
        Assertions.assertFalse(d.buildRoom(1, 4, outerRoom)
                || d.buildRoom(0, 3, outerRoom));
        final Room lowerRoom = new Room(3, RoomRestriction.LOWER_HALF, 0, res);
        Assertions.assertFalse(d.buildRoom(1, 2, lowerRoom)
                || d.buildRoom(0, 3, lowerRoom));
        final Room innerRoom = new Room(0, RoomRestriction.INNER, 0, res);
        Assertions.assertFalse(d.buildRoom(0, 4, innerRoom)
                || d.buildRoom(0, 1, innerRoom));
        Assertions.assertTrue(d.buildRoom(2, 2, innerRoom));
        //check adjacent rooms
        Assertions.assertFalse(d.buildRoom(2, 2, upperRoom));
        Assertions.assertFalse(d.buildRoom(1, 2, upperRoom));
        Assertions.assertFalse(d.buildRoom(3, 2, upperRoom));
        Assertions.assertFalse(d.buildRoom(2, 1, upperRoom));
        Assertions.assertFalse(d.buildRoom(2, 3, lowerRoom));
        Assertions.assertTrue(d.checkSetBattleGround(1, 2));
        d.conquerCurrentTile();
        Assertions.assertTrue(d.checkSetBattleGround(2, 2));
        Assertions.assertTrue(d.isBattleGroundRoom());
    }
}
