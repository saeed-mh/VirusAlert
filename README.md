# Virus alert application for final university project ( Associate degree)

## what does it do?

Using the app, users can see who is infected with the virus ( COVID-19, FLU, MEASLES and otherViruses ) in their location.
everyone who are infected, it shows them in a map ( I've used Google Map API ) for other people as a circle with specific color.
for example one has a measles:


![image](https://user-images.githubusercontent.com/75900669/179374374-1609ff27-1f1e-4bf8-b6de-0ab3c0452368.png)


also, users can see how many people are infected with virus:


![image](https://user-images.githubusercontent.com/75900669/179374435-aa2d2863-c8d9-447b-97db-0331b4e7ffcb.png)


## Install Libraries - Python3
```
pip install Flask
pip install psycopg2-binary
```
## Run Server Application

for first time, in order to creates database and user table, should run **install.py** that is located on server folder, then for running server application just run **server.py**
```
py install.py
py server.py
```
you should see the below lines:

![image](https://user-images.githubusercontent.com/75900669/179373810-9352f4e1-83dd-448b-a2ce-29dddff382a6.png)

now this is ready and you can run clinet application.
