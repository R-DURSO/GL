package test.Unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Global test suite of unit tests.
 * Includes 6 tests cases :
 * <ul>
 * 		<li>{@link TestInitGame}</li>
 * 		<li>{@link TestActionValidator}</li>
 * 		<li>{@link TestCreateBuilding}</li>
 * 		<li>{@link TestRessource}</li>
 * 		<li>{@link TestUnits}</li>
 * 		<li>{@link TestBoat}</li>
 * </ul>
 * @author Aldric Vitali Silvestre
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestInitGame.class,
	TestActionValidator.class,
	TestCreateBuilding.class,
	TestRessource.class,
	TestUnits.class,
	TestBoat.class})
public class GameTestSuite {

}
