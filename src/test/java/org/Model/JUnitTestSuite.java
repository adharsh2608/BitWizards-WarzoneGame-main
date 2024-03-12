package org.Model;
import org.Model.Phases.*;
import org.Model.PlayerStrategy.AggressivePlayerTest;
import org.Model.PlayerStrategy.BenevolentStrategyTest;
import org.Model.PlayerStrategy.CheaterStrategyTest;
import org.Model.PlayerStrategy.RandomStrategyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)

@Suite.SuiteClasses({
        MapTest.class,
        StartUpPhaseTest.class,
        IssueOrderPhaseTest.class,
        OrderExecutionPhaseTest.class,
        AggressivePlayerTest.class,
        BenevolentStrategyTest.class,
        CheaterStrategyTest.class,
        RandomStrategyTest.class
})
public class JUnitTestSuite {
}



