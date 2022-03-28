import yaml
from flask import Flask, render_template, abort


app = Flask(__name__)
app.config

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
    return {'Usage':'/api/animals /api/animals/<animal>'}


@app.route('/api/health', methods=['GET', 'POST'])
def api_health():
    return {'status':'OK'}


@app.route('/api/animals', methods=['GET', 'POST'])
def api_animals():
    animals = []
    for animal in my_params['animals']:
        animals.append(animal)
    #animals = my_params['animals'].keys()
    return {'animals':animals}


@app.route('/api/animals/<string:animal>', methods=['GET', 'POST'])
def api_animals_animal(animal):
    if animal not in my_params['animals']:
        abort(404)
    return {animal:my_params['animals'][animal]}


@app.route('/api/animals/<string:animal>/<string:param>', methods=['GET', 'POST'])
def api_animals_animal_param(animal,param):
    if animal not in my_params['animals'] or param not in my_params['animals'][animal]:
        abort(404)
    return {param:my_params['animals'][animal][param]}


@app.errorhandler(404)
def page_not_found(e):
    return "404 - Not Found", 404


if __name__ == '__main__':
    my_params = read_parameters('parameters.yml')
    app.run(host='0.0.0.0', port=5000)
