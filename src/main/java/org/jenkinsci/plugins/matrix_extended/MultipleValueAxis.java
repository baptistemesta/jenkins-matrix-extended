package org.jenkinsci.plugins.matrix_extended;

import hudson.Extension;
import hudson.Util;
import hudson.matrix.Axis;
import hudson.matrix.AxisDescriptor;
import hudson.matrix.TextAxis;
import hudson.util.FormValidation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

public class MultipleValueAxis extends TextAxis {

    public MultipleValueAxis(final String name, final List<String> values) {
        super(name, values);
    }

    public MultipleValueAxis(final String name, final String... values) {
        super(name, values);
    }

    @DataBoundConstructor
    public MultipleValueAxis(final String name, final String valueString) {
        super(name, new ArrayList<String>(Arrays.asList(Util.tokenize(valueString, "\n\r\f"))));
    }

    @Override
    public void addBuildVariable(final String value, final Map<String, String> map) {
        // value is the multi valuated value name
        map.put(name, value);

        // get the good complete value
        for (String completeValue : values) {
            if (ValuesParser.parseName(completeValue).equals(value)) {
                Map<String, String> parseValues = ValuesParser.parseValues(completeValue);
                for (Entry<String, String> entry : parseValues.entrySet()) {
                    String replacedValue = replaceEnvVars(entry.getValue(), map);
                    map.put(entry.getKey(), replacedValue);
                }
                return;
            }
        }
        throw new IllegalStateException("entry not found for <" + value + "> in values <" + values + ">");
    }

    private String replaceEnvVars(final String value, final Map<String, String> map) {
        String replacedValue = value;
        while (replacedValue.indexOf("${") >= 0) {
            int start = replacedValue.indexOf("${");
            int end = replacedValue.indexOf("}", start);
            String envVar = replacedValue.substring(start + 2, end);
            String replacement = map.get(envVar) == null ? envVar : map.get(envVar);
            replacedValue = new StringBuilder(replacedValue).replace(start, end + 1, replacement).toString();
        }
        return replacedValue;
    }

    @Override
    public List<String> getValues() {
        ArrayList<String> arrayList = new ArrayList<String>(values.size());
        for (String string : values) {
            arrayList.add(ValuesParser.parseName(string));
        }
        return arrayList;
    }

    /**
     * Descriptor for this plugin.
     */
    @Extension
    public static class DescriptorImpl extends AxisDescriptor
    {

        /**
         * Overridden to create a new instance of our Axis extension from UI
         * values.
         * 
         * @see hudson.model.Descriptor#newInstance(org.kohsuke.stapler.StaplerRequest, net.sf.json.JSONObject)
         */
        @Override
        public Axis newInstance(final StaplerRequest req, final JSONObject formData) throws FormException
        {
            return new MultipleValueAxis(formData.getString("name"), formData.getString("valueString"));
        }

        /**
         * Overridden to provide our own display name.
         * 
         * @see hudson.model.Descriptor#getDisplayName()
         */
        @Override
        public String getDisplayName()
        {
            return "Multiple value axis";
        }

        /**
         * Ensures the value is a valid environment variable. Since some
         * variables are not available until build time a warning is generated
         * if the name is valid but cannot be found in the current environment.
         * 
         * @param value
         * @return
         */
        public FormValidation doCheckValueString(@QueryParameter final String value)
        {
            String[] tokenize = Util.tokenize(value, "\n\r\f");
            for (String multiValue : tokenize) {
                try {
                    String parseName = ValuesParser.parseName(multiValue);
                    Map<String, String> parseValues = ValuesParser.parseValues(multiValue);
                } catch (Exception e) {
                    return FormValidation.error(e, "Wrong value for the axis value <" + multiValue + ">, see exceptions");
                }
            }
            System.out.println("check " + value);
            return FormValidation.ok();
        }
    }
}
