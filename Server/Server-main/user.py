class User:
    def __init__(self, username=None, password=None, latitude=None, longitude=None, virus=None):
        self.username = username
        self.password = password
        self.latitude = latitude
        self.longitude = longitude
        self.virus = virus
    
    def insert(self, connection):
        cursor = connection.cursor()
        cursor.execute('INSERT INTO users (username, password, latitude, longitude, virus) VALUES (%s, %s, %s, %s, %s)',
            (self.username, self.password, self.latitude, self.longitude, self.virus))
        connection.commit()

    def update(self, connection):
        cursor = connection.cursor()
        cursor.execute('UPDATE users SET latitude = %s, longitude = %s, virus = %s WHERE username = %s',
            (self.latitude, self.longitude, self.virus, self.username))
        connection.commit()

    @staticmethod
    def all(connection):
        users = []
        cursor = connection.cursor()
        cursor.execute('SELECT username, password, latitude, longitude, virus FROM users')
        for user in cursor.fetchall():
            users.append(User(*user))
        return users

    @staticmethod
    def delete(connection, username):
        cursor = connection.cursor()
        cursor.execute('DELETE FROM users WHERE username = %s', (username,))
        connection.commit()

    @staticmethod
    def count_corona_virus(connection):
        cursor = connection.cursor()
        cursor.execute('select count (*) from users where virus = 1')
        return cursor.fetchone()[0]

    @staticmethod
    def count_measles_virus(connection):
        cursor = connection.cursor()
        cursor.execute('select count (*) from users where virus = 2')
        return cursor.fetchone()[0]

    @staticmethod
    def count_flu_virus(connection):
        cursor = connection.cursor()
        cursor.execute('select count (*) from users where virus = 3')
        return cursor.fetchone()[0]

    @staticmethod
    def count_other_virus(connection):
        cursor = connection.cursor()
        cursor.execute('select count (*) from users where virus = 4')
        return cursor.fetchone()[0]

    @staticmethod
    def getVirusByUsername(connection, username):
        cursor = connection.cursor()
        cursor.execute('select (virus) from users where username = %s', (username,))
        return cursor.fetchone()[0]