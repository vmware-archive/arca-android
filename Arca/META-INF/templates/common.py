## Python functions useful for templates
import inflect;
import re;

def propType(prop):
	if 'isSet' in prop and prop['isSet']:
		return "List<" + prop['object_type'] + ">"
	else:
		return prop['object_type']

def hasListType(properties):
	for prop in properties:
		if 'List<' in propType(prop):
			return True
	return False
	
def hasIdProp(properties):
	for prop in properties:
		if 'id' in prop['key']:
			return True
	return False

def idProp(properties):
	for prop in properties:
		if 'id' in prop['key']:
			return underscoreUppercase(prop['key'])
	if len(properties) > 0:
		return underscoreUppercase(properties[0]["key"])
	else:
		return "UNKNOWN_PROPERTY"

def underscore(text):
	temp = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', text)
	return re.sub('([a-z0-9])([A-Z])', r'\1_\2', temp).lower()
	
def underscoreUppercase(text):
	return underscore(text).upper()
	
def underscoreCombine(name, text):
	return underscore(name + '_' + text)

def capitalize(text):
	return str(text[0]).upper() + str(text[1:])

def isSqliteType(prop, sqlite_types):
	return (prop['type'] in sqlite_types) and not prop['isSet']

def lowerStart(text):
	return str(text[0]).lower() + str(text[1:])

def upperStart(text):
	return str(text[0]).upper() + str(text[1:])

def pluralCapitalized(text):
	engine = inflect.engine()
	return upperStart(engine.plural(lowerStart(text)))

def pluralLowercase(text):
	engine = inflect.engine()
	return engine.plural(lowerStart(text)).lower()

def pluralUppercase(text):
	engine = inflect.engine()
	return engine.plural(lowerStart(text)).upper()

def pluralUnderscoreUppercase(text):
	engine = inflect.engine()
	return engine.plural(underscore(text)).upper()
	
def pluralUnderscoreLowercase(text):
	engine = inflect.engine()
	return engine.plural(underscore(text)).lower()