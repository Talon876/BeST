package org.nolat.best;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.Test;

public class IconManagerTest {

    private static final Logger log = Logger.getLogger(IconManagerTest.class);

    @Test
    public void testIconNameExtraction() {
        log.info("Handling main use cases");

        String path = "/icons/cog.png";
        String expectedResult = "cog";
        String actualResult = IconManager.stripExtensionAndPath(path);
        log.info("Expecting " + expectedResult + "; got " + actualResult);
        assertEquals(expectedResult, actualResult);

        path = "icons/cog.png";
        expectedResult = "cog";
        actualResult = IconManager.stripExtensionAndPath(path);
        log.info("Expecting " + expectedResult + "; got " + actualResult);
        assertEquals(expectedResult, actualResult);

        path = "/cog.png";
        expectedResult = "cog";
        actualResult = IconManager.stripExtensionAndPath(path);
        log.info("Expecting " + expectedResult + "; got " + actualResult);
        assertEquals(expectedResult, actualResult);

        path = "cog.png";
        expectedResult = "cog";
        actualResult = IconManager.stripExtensionAndPath(path);
        log.info("Expecting " + expectedResult + "; got " + actualResult);
        assertEquals(expectedResult, actualResult);

        path = "cog";
        expectedResult = "cog";
        actualResult = IconManager.stripExtensionAndPath(path);
        log.info("Expecting " + expectedResult + "; got " + actualResult);
        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void testIconNameExtractionEdgeCases() {
        log.info("Handling edge cases");
        String path = "/this.is/a.weird.path.txt";
        String expectedResult = "a.weird.path";
        String actualResult = IconManager.stripExtensionAndPath(path);
        log.info("Expecting " + expectedResult + "; got " + actualResult);
        assertEquals(expectedResult, actualResult);

        path = "c.og.";
        expectedResult = "c.og";
        actualResult = IconManager.stripExtensionAndPath(path);
        log.info("Expecting " + expectedResult + "; got " + actualResult);
        assertEquals(expectedResult, actualResult);

        //        This isn't likely to occur as I am mostly in control of what paths will be used and nobody would use relative
        //        paths like this anyway

        //        path = "/../test";
        //        expectedResult = "test";
        //        actualResult = IconManager.stripExtensionAndPath(path);
        //        log.info("Expecting " + expectedResult + "; got " + actualResult);
        //        assertEquals(expectedResult, actualResult);
    }
}
