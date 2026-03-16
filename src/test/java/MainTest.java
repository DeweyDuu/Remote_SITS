import org.junit.jupiter.api.Test;

public class MainTest {
	@Test
    public void testMainConstructor() {
        new Main();
    }
    @Test
    public void testMainAppRuns() {
        Main.main(new String[]{});
    }

}