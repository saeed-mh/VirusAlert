import psycopg2
from psycopg2.extensions import ISOLATION_LEVEL_AUTOCOMMIT

from config import Config


if __name__ == '__main__':
    connection = psycopg2.connect(host=Config.host, port=Config.port, user=Config.user, password=Config.password)
    connection.set_isolation_level(ISOLATION_LEVEL_AUTOCOMMIT)
    cursor = connection.cursor()
    cursor.execute('DROP DATABASE IF EXISTS virus_alert')
    cursor.execute('CREATE DATABASE virus_alert')
    connection.close()
    connection = psycopg2.connect(host=Config.host, port=Config.port, user=Config.user, password=Config.password, dbname=Config.dbname)
    cursor = connection.cursor()
    with open('virus_alert.sql') as file:
        virus_alert_sql = file.read()
    cursor.execute(virus_alert_sql)
    connection.commit()
    connection.close()
