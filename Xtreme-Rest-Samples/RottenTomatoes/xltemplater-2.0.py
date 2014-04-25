#! /usr/bin/python
"""
Model Generator

Usage:
    templater.py <parameter_file> [(--silent | --verbose)]
    templater.py (-v | --version)
    templater.py (-V | --full-version)

    templater.py -h | --help

Options:
    --silent                        No output
    --verbose                       Extra output
    -h --help                       Show help screen.
    -v --version                    Show version.
    -V --full-version               Show full version.
    <parameter_file>                All the other parameters, in one JSON file

"""

import json
import module
import sys
import time
import imp
import os
import re
import subprocess
import inflect
from docopt import docopt
from mako.lookup import TemplateLookup, exceptions

PATH = sys.path[0]
GENERATOR_FAILURE = "Generation Failed".upper()
GENERATOR_WARNING= "Generation Warning".upper()
FULL_VERSION = "2.0.2"
VERSION = "2.0"

def main(arguments):

    if(arguments["--full-version"]):
        print(FULL_VERSION)
        sys.exit()
    if(arguments["--version"]):
        print(VERSION)  
        sys.exit()
    global VERBOSE
    global SILENT
    SILENT = arguments['--silent']
    VERBOSE = arguments['--verbose'] and not SILENT

    GENERATED = False

    try:
        with open(arguments["<parameter_file>"], 'r') as json_list:
            parameters = json.load(json_list)
    except IOError as error:
        sys.stderr.write('ERROR: Unable to access the file: {}'.format(filename))
        sys.exit(GENERATOR_FAILURE)

    for set_of_param in parameters:
        task = Parsed_attributes(set_of_param)
      
        for set_to_generate in set_of_param["tasks"]:
            if(task.templater_version != VERSION):
                continue
            else:
                GENERATED = True

            task.setup_templates(set_to_generate)
            task.setup_model(set_to_generate)

            for template_name in task.templates:
                if (task.config[template_name]["creation_type"] == "many_to_one" or task.config[template_name]["creation_type"] == "none_to_one"):
                    generate_all_models_in_one_template(task, template_name)
                elif (task.config[template_name]["creation_type"] == "one_to_one"):
                    generate_one_template_per_model(task, template_name)
                else:
                    if (not SILENT): sys.stderr.write("The template "+ template_name +" is not in the config. Assuming \"one_to_one\"")
                    generate_one_template_per_model(task, template_name)

    if (not GENERATED):
        sys.stderr.write("Warning: version of templater.py " + VERSION + " does not equal the version required by all the templates in the config file\n")
        sys.exit(GENERATOR_FAILURE)

class Parsed_attributes:
    def __init__(self, set_of_param):

        self.language = "objective_c" if (set_of_param["language"] == "objective_c") else "java"
        self.templater_version = set_of_param["templater_version"]
        self.base_package = set_of_param["base_package"] if set_of_param["base_package"] is not None else "com.xtreme.amg"
        self.config = self.get_config_file(set_of_param["templater_config"])
        self.replace_text = "_NAME_"
        self.dirname = os.path.dirname(set_of_param["templater_config"])

        path = os.path.abspath(self.dirname + "/common.py")
        self.common = imp.load_source('common', path) if os.path.isfile(path) else None

        self.model_def = self.load_models(set_of_param["model_schema"])

        if(VERBOSE): print(" Commandline arguments parsed.")

    def setup_model(self, task):
        self.selected_model = {}
        for model_name, model in self.model_def.items():
            if (model_name == task["model_name"]):
                self.selected_model[model_name] = model
                return

    def setup_templates(self, task):
        self.templates = self.get_templates(task["templates"])
        return self

    def parse_json(self, filename):
        try:
            with open(filename, 'r') as json_list:
                return json.load(json_list)
        except IOError as error:
            sys.stderr.write('ERROR: Unable to access the model: {}'.format(filename))
            sys.exit(GENERATOR_FAILURE)

    def get_config_file(self, filename):
        raw_config = self.parse_json(filename)

        out_config = {}
        for template in raw_config["templates"]:
            if("$base_package" in template['output_directory']):
                base_package_dir = self.base_package.replace(".","/")
                template['output_directory'] = template['output_directory'].replace("$base_package", base_package_dir)
            out_config[template["name"]] = template

        return out_config

    def load_models(self, filename=""):
        if not filename:
            sys.stderr.write("Error: missing model filename")
            sys.exit(GENERATOR_FAILURE)
        else:
            jsonModelsDef = self.parse_json(filename)
            jsonModels = jsonModelsDef["models"]
            self.project_name = jsonModelsDef["project"]
            models_map = {}
            for jsonModel in jsonModels:
                model = MetaModel(jsonModel, self.language)
                models_map[model.name] = model
        
            for n, model in models_map.items():
                model.add_inherited_properties(models_map)

            if len(models_map) > 0:
                if(VERBOSE): print(" Model generation list built.")
            else:
                if (not SILENT): sys.stderr.write("Warning: Model generation list is empty. If this is not a none_to_one template, this is an error.")

            return models_map

    def load_processor(self, filename):
        return imp.load_source('module.Processor', filename)

    def get_templates(self, template_files):
        templates = {}
        lookup = TemplateLookup(directories=[self.dirname])

        for template_name in template_files:
            template_path = self.dirname + "/" + template_name           
            template_uri = os.path.basename(template_path)
            lookup = TemplateLookup(directories=[self.dirname])

            try:
                templates[template_uri] = lookup.get_template(template_uri)
            except exceptions.TopLevelLookupException as e:
                sys.stderr.write(str(e))
                sys.exit(GENERATOR_FAILURE)

        return templates



class MetaModel:
    def __init__(self, json, language):
        sqlite_types = TypeMaps.get_sqlite_types(language)

        self.name = json["name"]

        self.properties = json["properties"]

        NameProcessor.name_processing(self.properties)
        TypeProcessor.type_processing(self.properties, TypeMaps.get_data_types(language))
        
        for prop in self.properties:
            if prop['type'] in sqlite_types:
                prop['sqlite_type'] = sqlite_types[prop['type']]

        self.parent = json["parent"] if "parent" in json else ""

    def add_inherited_properties(self, model_lookup):
        self.inherited_properties = []

        if self.parent in model_lookup:
            self.get_inherited_property_array(model_lookup, model_lookup[self.parent])

        elif self.parent != "":
            if (not SILENT): sys.stderr.write("The model " + self.name + " requires a model " + self.parent + " not selected.")

    def get_inherited_property_array(self, model_lookup, model):
        for prop in model.properties:
            key = prop["key"]

            if not in_array("key", self.inherited_properties, key):
                self.inherited_properties.append(prop)

        if model.parent in model_lookup:
            self.get_inherited_property_array(model_lookup, model_lookup[model.parent])

    def to_dictionary(self):
        model_map = {}
        model_map["name"] = self.name
        model_map["resource_name"] = NameProcessor.capitals_to_separators(self.name)
        model_map["camelcase_name"] = NameProcessor.separators_to_capitals(self.name)
        model_map["properties"] = self.properties
        model_map["parent"] = self.parent
        model_map["inherited_properties"] = self.inherited_properties
        model_map["all_properties"] = self.inherited_properties + self.properties
        return model_map

class NameProcessor:
    @staticmethod
    def name_processing(properties):        
            for property in properties:
                if 'name' not in property:
                    property['name'] = NameProcessor.separators_to_capitals(property['key'])
                    property['name'] = property['name'].replace( property['name'][0],property['name'][0].lower(),1)
                if 'resource_name' not in property:
                    property['resource_name'] = NameProcessor.capitals_to_separators(property['key'])

    @staticmethod
    def separators_to_capitals(name):
        separator_and_character = re.search('[-_].',name)
        while(separator_and_character):
            capitalized_match = separator_and_character.group(0).upper()
            capitalized_match = capitalized_match.replace('-','').replace('_','')
            name = name.replace(separator_and_character.group(0), capitalized_match)
            separator_and_character = re.search('([-_].)',name)
        return name

    @staticmethod
    def capitals_to_separators(name):
        temp = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
        return re.sub('([a-z0-9])([A-Z])', r'\1_\2', temp).lower()

class TypeProcessor:
    @staticmethod
    def type_processing(properties, types):
            for property in properties:
                if property['type'].endswith('[]'):
                    property['isSet'] = True
                    property['type'] = property['type'].strip('[]')
                else:
                    property['isSet'] = False
                if property['type'] in types:
                    property['object_type'] = types[property['type']]
                    property['custom_type'] = False
                else:
                    property['object_type'] = property['type']
                    property['custom_type'] = True

class TypeMaps:
    java_data_types = {
        "byte": "Byte",
        "short": "Short",
        "int": "Integer",
        "long": "Long",
        "bool": "Boolean",
        "boolean": "Boolean",
        "float": "Float",
        "double": "Double",
        "number": "Double",
        "char": "Character",
        "string": "String",
        "String": "String",
        "dict": "HashMap",
        "dictionary": "HashMap",
        "byte[]": "Byte[]",
        "short[]": "Short[]",
        "int[]": "Integer[]",
        "long[]": "Long[]",
        "boolean[]": "Boolean[]",
        "float[]": "Float[]",
        "double[]": "Double[]",
        "char[]": "Character[]",
        "string[]": "List<String>",
        "String[]": "List<String>"
    }
    java_sqlite_types = {
        "Byte": "INTEGER",
        "byte": "INTEGER",
        "Short": "INTEGER",
        "short": "INTEGER",
        "Integer": "INTEGER",
        "int": "INTEGER",
        "Long": "INTEGER",
        "long": "INTEGER",
        "bool": "BIT",
        "Boolean": "BIT",
        "boolean": "BIT",
        "Float": "REAL",
        "float": "REAL",
        "Double": "REAL",
        "double": "REAL",
        "Character": "TEXT",
        "char": "TEXT",
        "String": "TEXT",
        "string": "TEXT"
    }
    java_annotation_types = {
        "Byte": "byteValue",
        "byte": "byteValue",
        "Short": "shortValue",
        "short": "shortValue",
        "Integer": "intValue",
        "int": "intValue",
        "Long": "longValue",
        "long": "longValue",
        "Boolean": "boolValue",
        "boolean": "boolValue",
        "Float": "floatValue",
        "float": "floatValue",
        "Double": "doubleValue",
        "double": "doubleValue",
        "Character": "charValue",
        "char": "charValue",
        "String": "stringValue",
        "string": "stringValue",
        "Byte[]": "byteValues",
        "byte[]": "byteValues",
        "Short[]": "shortValues",
        "short[]": "shortValues",
        "Integer[]": "intValues",
        "int[]": "intValues",
        "Long[]": "longValues",
        "long[]": "longValues",
        "Boolean[]": "booleanValues",
        "boolean[]": "booleanValues",
        "Float[]": "floatValues",
        "float[]": "floatValues",
        "Double[]": "doubleValues",
        "double[]": "doubleValues",
        "Character[]": "charValues",
        "char[]": "charValues",
        "string[]": "stringValues",
        "String[]": "stringValues",
        "List<String>": "stringValues"
    }
    java_delimiter_types = {
        "Character": "'",
        "char": "'",
        "String": "\"",
        "string": "\""
    }
    ios_data_types = {
        "dict": "NSDictionary",
        "bool": "NSNumber",
        "dictionary": "NSDictionary",
        "float": "NSNumber",
        "int": "NSNumber",
        "long": "NSNumber",
        "number": "NSNumber",
        "string": "NSString",
        "String": "NSString",
        "float[]": "NSSet",
        "string[]": "NSSet",
        "int[]": "NSSet"
    }
    ios_sqlite_types = {
        "bool": "NSBooleanAttributeType",
        "float": "NSFloatAttributeType",
        "int": "NSInteger32AttributeType",
        "long": "NSInteger64AttributeType",
        "double": "NSDoubleAttributeType",
        "String": "NSStringAttributeType",
        "string": "NSStringAttributeType"
    }
    ios_annotation_types =  {
        "boolean": "boolValue",
        "float": "floatValue",
        "int": "intValue",
        "long": "longValue",
        "double": "doubleValue",
        "String": "stringValue",
        "string": "stringValue",
        "float[]": "floatsValues",
        "string[]": "stringValues",
        "int[]": "intValues"
    }
    ios_delimiter_types = {
        "Character": "'",
        "char": "'",
        "String": "\"",
        "string": "\""
    }
    @staticmethod
    def get_data_types(platform):
        if (platform == "objective_c"):
            return TypeMaps.ios_data_types
        return TypeMaps.java_data_types

    @staticmethod
    def get_sqlite_types(platform):
        if (platform == "objective_c"):
            return TypeMaps.ios_sqlite_types
        return TypeMaps.java_sqlite_types

    @staticmethod
    def get_annotation_types(platform):
        if (platform == "objective_c"):
            return TypeMaps.ios_annotation_types
        return TypeMaps.java_annotation_types

    @staticmethod
    def get_delimiter_types(platform):
        if (platform == "objective_c"):
            return TypeMaps.ios_delimiter_types
        return TypeMaps.java_delimiter_types

def generate_all_models_in_one_template(task, template_name):
    model_lookup = {}
    for model_name, model in task.selected_model.items():
        if(VERBOSE): print("\n Generating code for {}.".format(model_name))
        model_map = model.to_dictionary()
        model_map["types"] = TypeMaps.get_data_types(task.language)
        model_lookup[model_map["name"]] = model_map

    initializer_data = {}
    initializer_data["model_lookup"] = model_lookup
    initializer_data["base_package"] = task.base_package
    initializer_data["sqlite_types"] = TypeMaps.get_sqlite_types(task.language)
    initializer_data["delimiter_types"] = TypeMaps.get_delimiter_types(task.language)
    initializer_data["project_name"] = task.project_name
    initializer_data["common"] = task.common
    initializer_data["pluralizer"] = inflect.engine()
    created_files = ""
    template = task.templates[template_name]
    new_file = generate_template_for_model(task, template_name, template, task.project_name, initializer_data)
    if new_file is not None:
        created_files += new_file + "\n"
    if(not SILENT): print(created_files) 

def generate_one_template_per_model(task, template_name):
    model_lookup = task.selected_model

    created_files = ""
    for model_name, model in model_lookup.items():
        if(VERBOSE): print("\n Generating code for {}.".format(model_name))
        
        model_map = model.to_dictionary()
        model_map["types"] = TypeMaps.get_data_types(task.language)
        model_map["base_package"] = task.base_package
        model_map["annotation_types"] = TypeMaps.get_annotation_types(task.language)
        model_map["sqlite_types"] = TypeMaps.get_sqlite_types(task.language)
        model_map["delimiter_types"] = TypeMaps.get_delimiter_types(task.language)
        model_map["project_name"] = task.project_name
        model_map["common"] = task.common
        model_map["pluralizer"] = inflect.engine()
        template = task.templates[template_name]
        new_file = generate_template_for_model(task, template_name, template, model.name, model_map)
        if new_file is not None:
            created_files += new_file + "\n"
    if(VERBOSE): print('\nSuccessfully generated all models at: {}'.format(time.asctime()))
    if(not SILENT): print(created_files)

def generate_template_for_model(task, template_name, template, replace_string, templating_data):
    overwrite_existing = task.config[template_name]["overwrite_old"]
    file_name = generate_file_name(template_name, replace_string, task.replace_text)
    output_directory = os.path.join(os.getcwd(), task.config[template_name]["output_directory"])
    if( not os.path.exists(output_directory)):
        os.makedirs(output_directory)
    file_path = os.path.join(output_directory,file_name)
    file_exists = os.path.exists(file_path) 
    if (overwrite_existing or not file_exists):
        generate_model(template, templating_data, file_path)
        if(VERBOSE): print("  {} successfully generated.".format(file_path))
        return file_path
    else:
        if(VERBOSE): print("  {} exists. Skipped.".format(file_name))
        return None

def make_directory(path):
    try:
        os.makedirs(path)
    except OSError:
        try:
            os.path.exists(path)
            if(VERBOSE): print(" Output directory created: " + path)
        except IOError as error:
            sys.stderr.write('ERROR: Unable to access the destination: {}'.format(path))
            sys.exit(GENERATOR_FAILURE)

def generate_model(template, model, file_path):
    output = template.render(**model)
    
    with open(file_path,'w') as output_file:
        output_file.write(output)


def generate_file_name(template_name, model_name, replace_flag):
    template_name = template_name.replace(replace_flag, NameProcessor.separators_to_capitals(model_name))
    template_name = template_name.replace(replace_flag.lower(), NameProcessor.capitals_to_separators(model_name))
    return template_name

def in_array(api_key, prop_array, key):
    for prop in prop_array:
        if prop[api_key] == key:
            return True
    return False

## Merge Code and associated functions

def merge_code(task):
    model_lookup = {}
    for model_name, model in task.selected_model.items():
        if(VERBOSE): print("\n Generating code for {}.".format(model_name))
        model_map = model.to_dictionary()
        model_map["types"] = TypeMaps.get_data_types(task.language)
        model_lookup[model_map["name"]] = model_map

    initializer_data = {}
    initializer_data["model_lookup"] = model_lookup
    initializer_data["base_package"] = task.base_package
    initializer_data["sqlite_types"] = TypeMaps.get_sqlite_types(task.language)
    initializer_data["delimiter_types"] = TypeMaps.get_delimiter_types(task.language)
    initializer_data["project_name"] = task.project_name
    initializer_data["common"] =  task.common
    initializer_data["pluralizer"] = inflect.engine()
    created_files = ""
    for key, value in task.templates.items():
        # Look for existing
        file_name = generate_file_name(key, task.project_name, task.replace_text)
        output_path = os.path.join(os.getcwd(), task.config[key]["output_directory"])
        file_path = os.path.join(output_path,file_name)
        if( not os.path.exists(output_path)):
            os.makedirs(output_path)
        file_exists = os.path.exists(file_path) 
        if file_exists:
            if not "Application" in key:
                # If exists, back up, generate new, diff
                subprocess.call(["mv",file_path,file_path+".bk"])
                generate_model(value, initializer_data, file_path)
                created_files += os.path.join(output_directory,file_name)+"\n"
                diff_file(file_path)
        else:
            # If not exists, generate new (done below)
            generate_model(value, initializer_data, file_path)
            created_files += os.path.join(output_directory,file_name)+"\n"
        if(VERBOSE): print("  {} successfully generated.".format(file_path))

    if(VERBOSE): print('\nSuccessfully generated all models at: {}'.format(time.asctime()))
    if(not SILENT): print(created_files)

def backup_file(filename):
    subprocess.call(["mv",filename,filename+".bk"])

def clean_up_files(filename):
    f = open(filename, 'wb')
    cat_proc = subprocess.Popen(["cat",filename+".new"],stdout=subprocess.PIPE)
    tr_proc = subprocess.Popen(["tr","-d","\r"],stdin=cat_proc.stdout,stdout=f)
    cat_proc.stdout.close();
    f.close()
    subprocess.call(["rm",filename+".new"])

def diff_file(filename):
    # Open the file for writing
    f = open(filename+".new", 'wb')

    # Set prefix (and suffix) for comments dependant on file type
    if "xml" in filename:
        commentPrefix = "<!-- "
        commentSuffix = " -->"
    else:
        commentPrefix = "// "
        commentSuffix = ""

    # Get the differences between the old file and the new
    # stored in a list of lines
    diff_proc = subprocess.Popen(["diff","-w",filename,filename+".bk"],stdout = subprocess.PIPE)
    grep_proc = subprocess.Popen(["grep", "-B1","^<"],stdin = diff_proc.stdout, stdout = subprocess.PIPE,stderr = subprocess.PIPE)
    diff_proc.stdout.close()
    (diff_str,err) = grep_proc.communicate()
    grep_proc.stderr.close()
    diff = diff_str.split("\n")

    first_sql_statement = True

    # Get a list of lines from the original file
    orig_str = subprocess.check_output(["cat",filename+".bk"])
    orig = orig_str.split("\n")
    orig_index = 0

    # Merge the differences into the original
    # using the line counts from diff
    for diffline in diff:
        if len(diffline) == 0:
            # do nothing
            continue
        elif diffline[0].isdigit():
            # found a diff line indicator
            # get the last value
            read_to_nums = re.split("[a-z,]",diffline)
            read_to = int(read_to_nums[-1])
            # read and output orig file until last value
            for i in range(orig_index,read_to):
                f.write( orig[i]+"\n")
                orig_index = i+1
        # output lines untill line == -- (drop the "<" at start)
        # reset first_sql_statement to fix edge case
        elif diffline.startswith("--"):
            first_sql_statement = True
        else:
            # special case - SQL definitions need extra output line
            if "CREATE TABLE" in diffline and first_sql_statement:
                #f.write("        + \");\"; // TODO: set unique fields here"+"\n")
                first_sql_statement = False
            # special case - Android Manifest - only add activities
            if "AndroidManifest.xml" in filename and not "Activity" in diffline:
                # do nothing
                continue
            else:
                f.write(diffline[2:]+"\n")

    # if there is anything left to write for the original file, write it now
    for i in range(orig_index,len(orig)):
        f.write(orig[i]+"\n")

    f.close()
    clean_up_files(filename)
    subprocess.call(["rm",filename+".bk"])

## main method

if __name__ == "__main__":
    arguments = docopt(__doc__, version=VERSION)
    main(arguments)

