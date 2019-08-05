Index-view is responsible for pulling data from 
the relation data base and displaying the indexes
in a webview. 
It also provides some API end points. All endpoints
are defined in the controller package

Dependencies:
  index-view depends on core-db to connect to the db,
  however it uses the jdbcTemplate object to run 
  custom SQL queries (vs index-data that uses an ORM)

  index-vew also depends on core for shared objects

