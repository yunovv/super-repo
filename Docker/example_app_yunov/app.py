import yaml
from flask import Flask, render_template, abort


app = Flask(__name__)

def read_parameters(params_filename):
    with open(params_filename, "r") as input_parameters_file:
        my_params = yaml.safe_load(input_parameters_file)
    return my_params

@app.route('/', methods=['GET'])
def index():
    context = my_params
    return render_template('index.html', **context)

@app.route('/api', methods=['GET', 'POST'])
def api_help():
    return {'Usage':'/api/animals/<animal>'}


@app.route('/api/health', methods=['GET', 'POST'])
def api_health():
    return {'status':'OK'}


@app.route('/api/animals/<string:animal>', methods=['GET', 'POST'])
def api_animals(animal):
    if animal not in my_params['animals']:
        abort(404) 
    return {animal:my_params['animals'][animal]}


@app.errorhandler(404)
def page_not_found(e):
    return "404 - Not Found", 404


if __name__ == '__main__':
    my_params = read_parameters('parameters.yml')
    app.run()