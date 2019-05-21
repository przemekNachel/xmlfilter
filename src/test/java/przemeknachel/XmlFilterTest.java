package przemeknachel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XmlFilterTest {

    private XmlFilter xmlFilter;

    @Before
    public void setUp() throws Exception {
        xmlFilter = new XmlFilter("filter.xml");
    }

    @Test
    public void test_true() {
        Assert.assertTrue(xmlFilter.check("8=FIX.4.4|9=348|115=ABCD|"));
    }

    @Test
    public void test_false() {
        Assert.assertFalse(xmlFilter.check("8=FIX.4,5|9=null|115=ABCE|"));
    }
}
