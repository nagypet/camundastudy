# camundastudy

Order process:

![](https://github.com/nagypet/camundastudy/blob/main/doc/pics/order-process.jpg)

This sample illustrates the basics of the SAGA pattern with one compensating transaction. The Camunda community edition is used. 

## The Camunda database

https://localhost:8400/h2

- JDBC URL: jdbc:h2:mem:camunda
- User: sa
- Password: sa

![](https://github.com/nagypet/camundastudy/blob/main/doc/pics/h2-access.jpg)

The table `ACT_HI_PROCINST` contains the process instances:

![](https://github.com/nagypet/camundastudy/blob/main/doc/pics/h2-db.jpg)
