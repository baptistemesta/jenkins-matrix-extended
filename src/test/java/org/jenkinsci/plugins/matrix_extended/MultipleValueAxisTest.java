package org.jenkinsci.plugins.matrix_extended;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

public class MultipleValueAxisTest {

    private MultipleValueAxis axis;

    @Test
    public void check_add_build_variable_add_correct_values() throws Exception {
        axis = new MultipleValueAxis("axeName",
                "axeValue1[firstkey=firstvalue@@@secondkey=secondvalue]\naxeValue2[firstkey=firstvalue2@@@secondkey=secondvalue2]");

        HashMap<String, String> map = new HashMap<String, String>();
        axis.addBuildVariable("axeValue2", map);

        assertEquals(3, map.size());
        assertEquals("axeValue2", map.get("axeName"));
        assertEquals("firstvalue2", map.get("firstkey"));
        assertEquals("secondvalue2", map.get("secondkey"));

    }

}
