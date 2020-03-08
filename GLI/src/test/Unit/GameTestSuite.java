package test.Unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Global test suite of unit tests.
 * Includes 6 tests cases :
 * <ul>
 * 		<li>{@link TestActionValidator}</li>
 * 		<li>{@link TestInitGame}</li>
 * 		<li></li>
 * 		<li></li>
 * 		<li></li>
 * 		<li></li>
 * </ul>
 * @author Aldric Vitali Silvestre
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestInitGame.class, TestActionValidator.class, TestCreateBuilding.class, TestRessource.class})
public class GameTestSuite {

}
