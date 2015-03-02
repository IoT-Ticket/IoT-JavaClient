import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        ValidationTest.class,
        DeviceTest.class,
        QuotaTest.class,
        DataNodeWriteReadTest.class
})
public class IOTAPITests extends TestCase {


}



