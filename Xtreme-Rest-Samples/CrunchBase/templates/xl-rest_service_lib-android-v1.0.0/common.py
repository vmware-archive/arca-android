## Python functions useful for templates
import inflect;
import re;

def underscore(name):
	temp = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
	return re.sub('([a-z0-9])([A-Z])', r'\1_\2', temp).lower()
	
def pluralUnderscoreUppercase(text):
	engine = inflect.engine()
	return engine.plural(underscore(text)).upper()