Holds all database related logic and classes that needs
to be shared across different modules. This is mostly
used by index-data and index-view. 

The database logic uses the ORM tool Hibernate to
(https://hibernate.org/orm/)
  - create schemas
  - insert data
  - in memory test db management


For debugging this SO post explains how to view the 
actual SQL statements Hibernate generates
https://stackoverflow.com/questions/30118683/how-to-log-sql-statements-in-spring-boot

