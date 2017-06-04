# bcindex
block chain index

To run in dev mode do the following:
  1. cd index-data/
  2. mvn spring-boot:run
  3. cd ../index-web/
  4. mvn spring-boot:run

To see the in memory database go to localhost:9122
Make sure saved seetings = 'Generic H2 (Server) 
and JDBC URL = jdbc:h2:tcp://localhost:9123/~/testdb
user name = sa
leave password blank

To seed the database with data go to localhost:8080/seed
to see example REST data go to localhost:8090/api/index

Note localhost:8090 is the home page, but currently does
not load any data


# frontend
All the configurable properties are now located in js/config.js
There you can update any of the settings, i will move stuff there such as color and stuff.

## To update the config.js file
- change what every settings you want
- from the root directory (bcindex) run the following command:
```npm --save-dev install ```
- then run this command:
`gulp`
