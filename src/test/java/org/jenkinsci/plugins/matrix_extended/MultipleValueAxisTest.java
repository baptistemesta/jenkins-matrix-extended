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

    @Test
    public void check_add_build_variable_add_correct_values_with_env_var() throws Exception {
        axis = new MultipleValueAxis("axeName",
                "axeValue1[firstkey=firstvalue@@@secondkey=secondvalue]\naxeValue2[firstkey=firstvalue2@@@secondkey=${env.var}second${env.var}value${unknown}2${env.var}]");

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("env.var", "%");
        axis.addBuildVariable("axeValue2", map);

        assertEquals("%second%valueunknown2%", map.get("secondkey"));

    }

    @Test
    public void check_add_build_variable_add_correct_values_with_spaces() throws Exception {
        axis = new MultipleValueAxis("axeName",
                "axeValue1[firstkey=firstvalue@@@secondkey=secondvalue]\naxeValue2[firstkey=first value2@@@secondkey=secondvalue2]");

        HashMap<String, String> map = new HashMap<String, String>();
        axis.addBuildVariable("axeValue2", map);
        System.out.println(map);
        assertEquals(3, map.size());
        assertEquals("axeValue2", map.get("axeName"));
        assertEquals("first value2", map.get("firstkey"));
        assertEquals("secondvalue2", map.get("secondkey"));

    }

}
