from database import Database
from user import User
from virus import Virus


class Core:
    def __init__(self):
        self.database = None
        self.users = {}
    
    def start(self):
        self.database = Database(connect=True)
        for user in User.all(self.database.connection):
            self.users[user.username] = user

    def create_user(self, username, password):
        user = User(username=username, password=password, latitude=None, longitude=None, virus=None)
        user.insert(self.database.connection)
        self.users[user.username] = user
        return True

    def delete_user(self, username):
        User.delete(self.database.connection, username)
        del self.users[username]
        return True

    def login_user(self, username, password):
        return username in self.users and self.users[username].password == password

    def update_latitude_longitude(self, username, latitude, longitude):
        user = self.users[username]
        user.latitude = latitude
        user.longitude = longitude
        user.update(self.database.connection)
        return True
    
    def update_virus(self, username, virus):
        user = self.users[username]
        user.virus = virus
        user.update(self.database.connection)
        return True

    def select_virus_information(self, username):
        latitudes = []
        longitudes = []
        viruses = []
        for username_, user in self.users.items():
            if username_ != username and user.virus != Virus.Nothing:
                latitudes.append(user.latitude)
                longitudes.append(user.longitude)
                viruses.append(user.virus)
        return latitudes, longitudes, viruses

    def count_corona(self):
        count = User.count_corona_virus(self.database.connection)
        return count
    
    def count_measles(self):
        count = User.count_measles_virus(self.database.connection)
        return count

    def count_flu(self):
        count = User.count_flu_virus(self.database.connection)
        return count

    def count_other(self):
        count = User.count_other_virus(self.database.connection)
        return count
    
    def get_virus_by_name(self, username):
        virusType = User.getVirusByUsername(self.database.connection, username)
        return virusType
    