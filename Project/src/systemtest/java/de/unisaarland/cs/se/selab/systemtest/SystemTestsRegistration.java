package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.systemtest.api.SystemTestManager;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.ActNowBroadcastAfterBiddingStarted;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.ActionFailedForLockedBids;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.AdditionalBuildRoomAndActivateRoom;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.AllPlayerHaveBidInAllSlotsOfCoinAndTrapOptions;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.AllPlayerHaveBidInAllSlotsOfDefaultOptions;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.AllPlayerLeftInMonsterOptEval;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.BuildRoomAndActivateRoom;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.ChooseWrongMonsterActionFailed;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.EdgeCasesForDiggingTunnelAndMiningGold;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.FirstPlayerBiddingActionFailed;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.FirstPlayerHaveFinishedBidding;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.InsufficientEvilnessForMonster;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.InsufficientForNicenessAndRoom;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.InsufficientResourcesForOptionsInEvaluationPhase;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.MinningInARoom;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.PlacingBidTest;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.SecondPlayerLeftWhenFirstPlayerHaveFinishedBidding;
import de.unisaarland.cs.se.selab.systemtest.biddingphase.TooManyPlayerBidsOnMonster;
import de.unisaarland.cs.se.selab.systemtest.combatphase.BattleGroundSettingTest;
import de.unisaarland.cs.se.selab.systemtest.combatphase.ConqueredRoomOrTunnel;
import de.unisaarland.cs.se.selab.systemtest.combatphase.ConqueredRoomOrTunnelAdaptationWithRoomActivation;
import de.unisaarland.cs.se.selab.systemtest.combatphase.FullBattle;
import de.unisaarland.cs.se.selab.systemtest.combatphase.FullBattleAdaptationForOneBattlePhase;
import de.unisaarland.cs.se.selab.systemtest.combatphase.FullBattleAdaptationToCatchMonsterAbuser;
import de.unisaarland.cs.se.selab.systemtest.combatphase.GoToNextYear;
import de.unisaarland.cs.se.selab.systemtest.combatphase.MultiTrapsAndMonsters;
import de.unisaarland.cs.se.selab.systemtest.combatphase.SetBattleGroundOnRoom;
import de.unisaarland.cs.se.selab.systemtest.combatphase.UseTrapAndMonster;
import de.unisaarland.cs.se.selab.systemtest.fullgame.MaxPlayersTwoYearsStandard;
import de.unisaarland.cs.se.selab.systemtest.fullgame.OnePlayerTwoYears;
import de.unisaarland.cs.se.selab.systemtest.fullgame.OnePlayerTwoYearsCycleBeater;
import de.unisaarland.cs.se.selab.systemtest.registration.RegisteredPlayerSendActivateRoomActionInRegistration;
import de.unisaarland.cs.se.selab.systemtest.registration.RegisteredPlayerSendHireMonsterActionInRegistration;
import de.unisaarland.cs.se.selab.systemtest.registration.RegisteredPlayerSendPlaceBidInRegistration;
import de.unisaarland.cs.se.selab.systemtest.registration.RegisteredPlayerSendRegistrationActionInRegistration;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationCombinedTests;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationLastPlayerIDTest;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationMaxPlayerTest;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationStartGameTest;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationTest;
import de.unisaarland.cs.se.selab.systemtest.registration.UnregisteredPlayerSendStartGameInRegistration;


final class SystemTestsRegistration {

    private SystemTestsRegistration() {
        // empty
    }

    static void registerSystemTests(final SystemTestManager manager) {
        //REGISTRATION PHASE
        manager.registerTest(new RegistrationTest());
        manager.registerTest(new RegistrationMaxPlayerTest());
        manager.registerTest(new RegistrationStartGameTest());
        manager.registerTest(new RegistrationLastPlayerIDTest());
        manager.registerTest(new UnregisteredPlayerSendStartGameInRegistration());
        manager.registerTest(new RegisteredPlayerSendRegistrationActionInRegistration());
        manager.registerTest(new RegisteredPlayerSendHireMonsterActionInRegistration());
        manager.registerTest(new RegisteredPlayerSendActivateRoomActionInRegistration());
        manager.registerTest(new RegistrationCombinedTests());
        manager.registerTest(new RegisteredPlayerSendPlaceBidInRegistration());
        //BIDDING PHASE
        manager.registerTest(new ActNowBroadcastAfterBiddingStarted());
        manager.registerTest(new PlacingBidTest());
        manager.registerTest(new FirstPlayerHaveFinishedBidding());
        manager.registerTest(new FirstPlayerBiddingActionFailed());
        manager.registerTest(new AllPlayerHaveBidInAllSlotsOfDefaultOptions());
        manager.registerTest(new SecondPlayerLeftWhenFirstPlayerHaveFinishedBidding());
        manager.registerTest(new AllPlayerHaveBidInAllSlotsOfCoinAndTrapOptions());
        manager.registerTest(new ActionFailedForLockedBids());
        manager.registerTest(new InsufficientResourcesForOptionsInEvaluationPhase());
        manager.registerTest(new ChooseWrongMonsterActionFailed());
        manager.registerTest(new EdgeCasesForDiggingTunnelAndMiningGold());
        manager.registerTest(new BuildRoomAndActivateRoom());
        manager.registerTest(new TooManyPlayerBidsOnMonster());
        manager.registerTest(new AdditionalBuildRoomAndActivateRoom());
        manager.registerTest(new AllPlayerLeftInMonsterOptEval());
        manager.registerTest(new InsufficientEvilnessForMonster());
        manager.registerTest(new InsufficientForNicenessAndRoom());
        manager.registerTest(new MinningInARoom());
        //COMBAT PHASE
        manager.registerTest(new MultiTrapsAndMonsters());
        manager.registerTest(new UseTrapAndMonster());
        manager.registerTest(new BattleGroundSettingTest());
        manager.registerTest(new FullBattleAdaptationForOneBattlePhase());
        manager.registerTest(new FullBattleAdaptationToCatchMonsterAbuser());
        manager.registerTest(new FullBattle());
        // UNCOMPLETED TESTS THAT STILL WORK ALREADY
        manager.registerTest(new MaxPlayersTwoYearsStandard());
        // next year
        manager.registerTest(new GoToNextYear());
        manager.registerTest(new SetBattleGroundOnRoom());
        // NEW TESTS
        manager.registerTest(new OnePlayerTwoYears());
        manager.registerTest(new OnePlayerTwoYearsCycleBeater());
        manager.registerTest(new ConqueredRoomOrTunnel());
        manager.registerTest(new ConqueredRoomOrTunnelAdaptationWithRoomActivation());
    }
}