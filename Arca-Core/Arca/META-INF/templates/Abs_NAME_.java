package ${base_package}.models;

%if common.hasListType(properties):
import java.util.List;
%endif\
import com.google.gson.annotations.SerializedName;

public abstract class Abs${name} ${'extends ' + parent if parent else ''} {

	protected static class Fields {
		%for prop in properties:
		public static final String ${common.underscoreUppercase(prop['key'])} = "${prop['key']}";
		%endfor
	}

    %for prop in properties:
    @SerializedName(Fields.${common.underscoreUppercase(prop['key'])}) private ${common.propType(prop)} m${common.capitalize(prop['name'])};
    %endfor

    %for prop in properties:
    public ${common.propType(prop)} get${common.capitalize(prop['name'])}() {
        return m${common.capitalize(prop['name'])};
    }
    
    public void set${common.capitalize(prop['name'])}(final ${common.propType(prop)} ${prop['name']}) {
        m${common.capitalize(prop['name'])} = ${prop['name']};
    }
    
    %endfor
}
