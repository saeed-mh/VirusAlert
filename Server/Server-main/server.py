import json

from flask import Flask, request, jsonify
from singleton import core_singleton

core_singleton.start()
application = Flask('server')


@application.route('/', methods=['GET'])
def root():
    return jsonify({'result': True})


@application.route('/create_user', methods=['GET'])
def create_user():
    username = request.args.get('username')
    password = request.args.get('password')
    result = core_singleton.create_user(username, password)
    return jsonify({'result': result})


@application.route('/delete_user', methods=['GET'])
def delete_user():
    username = request.args.get('username')
    result = core_singleton.delete_user(username)
    return jsonify({'result': result})


@application.route('/update_latitude_longitude', methods=['GET'])
def update_latitude_longitude():
    username = request.args.get('username')
    latitude = request.args.get('latitude')
    longitude = request.args.get('longitude')
    result = core_singleton.update_latitude_longitude(username, latitude, longitude)
    return jsonify({'result': result})

@application.route('/update_virus', methods=['GET'])
def update_virus():
    username = request.args.get('username')
    virus = request.args.get('virus')
    result = core_singleton.update_virus(username, virus)
    return jsonify({'result': result})

@application.route('/login_user', methods=['GET'])
def login_user():
    username = request.args.get('username')
    password = request.args.get('password')
    result = core_singleton.login_user(username, password)
    return jsonify({'result': result})


@application.route('/select_virus_information', methods=['GET'])
def select_virus_information():
    username = request.args.get('username')
    latitudes, longitudes, viruses = core_singleton.select_virus_information(username)
    return jsonify({'latitudes': latitudes, 'longitudes': longitudes, 'viruses': viruses})

@application.route('/count_corona', methods=['GET'])
def corona():
    res = core_singleton.count_corona()
    return jsonify({'result': res})

@application.route('/count_measles', methods=['GET'])
def measles():
    res = core_singleton.count_measles()
    return jsonify({'result': res})

@application.route('/count_flu', methods=['GET'])
def flu():
    res = core_singleton.count_flu()
    return jsonify({'result': res})

@application.route('/count_other_viruses', methods=['GET'])
def other():
    res = core_singleton.count_other()
    return jsonify({'result': res})

@application.route('/get_virus_by_name', methods=['GET'])
def get_virus_by_name():
    username = request.args.get('username')
    res = core_singleton.get_virus_by_name(username)
    return jsonify({'result': res})

application.run(debug=True, host="192.168.43.121", port=5000)
