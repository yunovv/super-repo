import sys
import yaml
import jinja2

if len(sys.argv) < 2:
    print('No template specified')
    sys.exit(0)
elif len(sys.argv) < 3:
    print('No input parameters file used')
    template_filename = sys.argv[1]
else:
    template_filename = sys.argv[1]
    input_parameters_filename = sys.argv[2]
    with open(input_parameters_filename, "r") as input_parameters_file:
        input_parameters = yaml.safe_load(input_parameters_file)


templateLoader = jinja2.FileSystemLoader(searchpath="./")
templateEnv = jinja2.Environment(loader=templateLoader)
template_file = templateEnv.get_template(template_filename)


rendered_template = template_file.render(params=input_parameters)

print(rendered_template)
print()
print(' - - - - -')
print()

with open('processed_templates/{}'.format(template_filename.replace('.j2','')),'w') as rendered_file:
    rendered_file.write(rendered_template)