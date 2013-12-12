package org.jenkinsci.plugins.matrix_extended;

import hudson.matrix.TextAxis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kohsuke.stapler.DataBoundConstructor;

public class MultipleValueAxis extends TextAxis {

    public MultipleValueAxis(final String name, final List<String> values) {
        super(name, values);
    }

    public MultipleValueAxis(final String name, final String... values) {
        super(name, values);
    }

    @DataBoundConstructor
    public MultipleValueAxis(final String name, final String valueString) {
        super(name, valueString);
    }

    @Override
    public void addBuildVariable(final String value, final Map<String, String> map) {
        map.put(name, ValuesParser.parseName(value));
        Map<String, String> parseValues = ValuesParser.parseValues(value);
        for (Entry<String, String> entry : parseValues.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public List<String> getValues() {
        ArrayList<String> arrayList = new ArrayList<String>(values.size());
        for (String string : values) {
            arrayList.add(ValuesParser.parseName(string));
        }
        return arrayList;
    }
}
