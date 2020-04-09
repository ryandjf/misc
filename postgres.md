## Run into postgres
```
docker exec -it grafana-postgres psql -U grafana -W grafana grafana
```

## Listing Databases
A single Postgres server process can manage multiple databases at the same time. Each database is stored as a separate set of files in its own directory within the server’s data directory. To view all of the defined databases on the server you can use the \list meta-command or its shortcut \l.
```
postgres=# \l
```
List of databases
```
   Name    |  Owner   | Encoding |   Collate   |    Ctype    |   Access privileges
-----------+----------+----------+-------------+-------------+-----------------------
 postgres  | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 |
 sales     | ubuntu   | UTF8     | en_US.UTF-8 | en_US.UTF-8 |
 template0 | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/postgres          +
           |          |          |             |             | postgres=CTc/postgres
 template1 | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/postgres          +
           |          |          |             |             | postgres=CTc/postgres
(4 rows)
```

## Switching Databases
Most Postgres servers have three databases defined by default: template0, template1 and postgres. template0 and template1 are skeleton databases that are or can be used by the CREATE DATABASE command. postgres is the default database you will connect to before you have created any other databases. Once you have created another database you will want to switch to it in order to create tables and insert data. Often, when working with servers that manage multiple databases, you’ll find the need to jump between databases frequently. This can be done with the \connect meta-command or its shortcut \c.
```
postgres=# \c sales
```
```
You are now connected to database "sales" as user "ubuntu".
sales=#
```
## Listing Tables
Once you’ve connected to a database, you will want to inspect which tables have been created there. This can be done with the \dt meta-command. However, if there are no tables you will get no output.
```
sales=# \dt
```
```
No relations found.
sales=#
```
After creating a table, it will be returned in a tabular list of created tables.
```
sales=# CREATE TABLE leads (id INTEGER PRIMARY KEY, name VARCHAR);
CREATE TABLE
```
```
sales=# \dt
```
```
List of relations
 Schema | Name  | Type  | Owner
--------+-------+-------+--------
 public | leads | table | ubuntu
(1 row)
sales=#
```