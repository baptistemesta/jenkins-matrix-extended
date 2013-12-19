Matrix extended plugin
=======================
Description
------------
this plugin will add a new axis to multi configuration project.
This axis will have for each value more than one system property.

It is usefull for e.g. runnning a job on multiple database, for each database type you will be able to set all database connection properties.

Usage
------

* In axis add a "Multiple value axis"
* add a line for each axis value. the line format is:
     axis_name[sysprop1=value1@@@sysprop2=value2@@@sysprop3=value3]

Example
-------

a multi database configuration would be like this:

    mysql[db.url=jdbc:mysql://localhost:3306/mydb@@@db.user=user@@@db.password=pass@@@db.driverClass=com.mysql.jdbc.Driver]
	postgres[db.url=jdbc:postgresql://localhost:5432/mydb@@@db.user=user@@@db.password=pass@@@db.driverClass=org.postgresql.Driver]
	sqlserver[db.url=jdbc:sqlserver://localhost:1433;database=mydb@@@db.user=user@@@db.password=pass@@@db.driverClass=com.microsoft.sqlserver.jdbc.SQLServerDriver]
