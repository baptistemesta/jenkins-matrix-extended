package org.jenkinsci.plugins.matrix_extended;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class ValuesParserTest {

    @Test
    public void check_parse_name() throws Exception {
        String name = ValuesParser.parseName("paramName[a=b@@@c=d]");

        assertEquals("paramName", name);
    }

    @Test
    public void check_parse_values() throws Exception {
        Map<String, String> values = ValuesParser.parseValues("paramName[a=b@@@c=d]");
        System.out.println(values);
        assertEquals(2, values.size());
        assertEquals("b", values.get("a"));
        assertEquals("d", values.get("c"));
    }

}
