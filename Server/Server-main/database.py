import psycopg2

from config import Config


class Database:
    def __init__(self, connect=False):
        if connect:
            self.connect()
        else:
            self.connection = None

    def connect(self):
        self.connection = psycopg2.connect(host=Config.host, port=Config.port, user=Config.user, password=Config.password, dbname=Config.dbname)

    def close(self):
         self.connection.close()
