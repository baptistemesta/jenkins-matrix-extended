package org.jenkinsci.plugins.matrix_extended;

import hudson.Extension;
import hudson.Util;
import hudson.matrix.Axis;
import hudson.matrix.AxisDescriptor;
import hudson.matrix.TextAxis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
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
                    map.put(entry.getKey(), entry.getValue());
                }
                return;
            }
        }
        throw new IllegalStateException("entry not found for <" + value + "> in values <" + values + ">");
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

        // /**
        // * Ensures the value is a valid environment variable. Since some
        // * variables are not available until build time a warning is generated
        // * if the name is valid but cannot be found in the current environment.
        // * @param value
        // * @return
        // */
        // public FormValidation doCheckValueString( @QueryParameter
        // String value )
        // {
        // // must have a value
        // if( value == null || value.length() == 0 )
        // {
        // return FormValidation.error( Messages.configNameRequired() );
        // }
        //
        // // check for non-portable characters
        // Pattern pattern = Pattern.compile( "[^\\p{Alnum}_]+" );
        // if( pattern.matcher( value ).find() )
        // {
        // return FormValidation.warning( Messages.configPortableName() );
        // }
        //
        // // see if it exists in the system; if not we cannot tell if it is valid or not
        // String content = System.getenv( value );
        // if( content == null )
        // {
        // return FormValidation.warning( Messages.configBuildVariable() );
        // }
        //
        // // should be ok - display current value so user can verify contents are okay to use as axis values
        // return FormValidation.ok( Messages.configCurrentValue( content ) );
        // }
    }
}
