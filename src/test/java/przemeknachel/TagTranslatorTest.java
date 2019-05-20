package przemeknachel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TagTranslatorTest {

    private TagTranslator tagTranslator;

    @Before
    public void setUp() {
        tagTranslator = new TagTranslator();
    }


    @Test
    public void test_tag8() {
        Assert.assertEquals("beginstring", tagTranslator.getName(8));
    }

    @Test
    public void test_tag9() {
        Assert.assertEquals("bodylength", tagTranslator.getName(9));
    }

    @Test
    public void test_bodylength() {
        Assert.assertEquals((long) 9 , (long) tagTranslator.getTag("bodylength"));
    }

    @Test
    public void test_beginstring() {
        Assert.assertEquals((long) 8, (long) tagTranslator.getTag("beginstring"));
    }

}
