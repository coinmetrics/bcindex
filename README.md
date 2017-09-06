# bcindex
block chain index

To run in dev mode do the following:
  1. run: mvn clean install (in root dir)
  2. run: index-data/runDev.sh
  3. run: index-view/runDev.sh

To see the in memory database go to localhost:9122
Make sure saved seetings = 'Generic H2 (Server) 
and JDBC URL = jdbc:h2:tcp://localhost:9123/~/testdb
user name = sa
leave password blank

Note localhost:8090 is the home page. Dummy data is 
pre populated on startup


# frontend
All the configurable properties are now located in js/config.js
There you can update any of the settings, i will move stuff there such as color and stuff.

## To update the config.js file
- change what every settings you want
- from the root directory (bcindex) run the following command:
```npm --save-dev install ```
- then run this command:
`gulp`
