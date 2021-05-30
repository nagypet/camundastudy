# camundastudy

## Cloning the repository

This project requires the `Spvitamin` library. Please clone it into your project's root folder.

git clone https://github.com/nagypet/spvitamin.git

So that you have this structure:
```
c:\np\github\camundastudy>dir
 Volume in drive C has no label.
 Volume Serial Number is BEA2-3C2D

 Directory of c:\np\github\camundastudy

2021.05.29  16:28    <DIR>          .
2021.05.29  16:28    <DIR>          ..
2021.05.29  10:09               405 .gitignore
2021.05.29  16:28    <DIR>          .gradle
2021.05.29  13:52    <DIR>          doc
2021.05.24  14:08    <DIR>          first
2021.05.29  09:33    <DIR>          gradle
2021.05.14  12:50             6 105 gradlew
2021.05.14  12:50             2 942 gradlew.bat
2021.05.23  14:37            11 558 LICENSE
2021.05.29  09:40    <DIR>          orchestrator
2021.05.29  16:20             3 317 README.md
2021.05.29  16:27                87 settings.gradle
2021.05.29  09:34    <DIR>          spvitamin
               6 File(s)         24 414 bytes
               8 Dir(s)  269 315 895 296 bytes free
```

## Building the project

Change dir to the orchestrator folder and run:

```
c:\np\github\camundastudy\orchestrator>gradlew dist
Included projects:
  :spvitamin-core => C:\np\github\camundastudy\spvitamin\spvitamin-core
  :spvitamin-spring-server => C:\np\github\camundastudy\spvitamin\spvitamin-spring-server
  :spvitamin-spring-general => C:\np\github\camundastudy\spvitamin\spvitamin-spring-general
  :spvitamin-spring-admin => C:\np\github\camundastudy\spvitamin\spvitamin-spring-admin
  :spvitamin-spring-security => C:\np\github\camundastudy\spvitamin\spvitamin-spring-security
  :spvitamin-spring-logging => C:\np\github\camundastudy\spvitamin\spvitamin-spring-logging
  :spvitamin-spring-data => C:\np\github\camundastudy\spvitamin\spvitamin-spring-data
Dependency check took 16 ms

> Task :spvitamin-spring-general:compileTestJava
Note: C:\np\github\camundastudy\spvitamin\spvitamin-spring-general\src\test\java\hu\perit\spvitamin\spring\connectablecontext\ThreadSpecificContextHolder.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.

> Task :spvitamin-spring-security:compileTestJava
Note: C:\np\github\camundastudy\spvitamin\spvitamin-spring-security\src\test\java\hu\perit\spvitamin\spring\security\auth\jwt\JwtTokenProviderTest.java uses or overrides a deprecated API.
Note: Recompile with -Xlint:deprecation for details.

BUILD SUCCESSFUL in 29s
37 actionable tasks: 32 executed, 5 up-to-date
```

After successful compilation start the `run.bat`. 

https://localhost:8400/swagger-ui.html

## Order process

![](https://github.com/nagypet/camundastudy/blob/main/doc/pics/order-process.jpg)

This sample illustrates the basics of the SAGA pattern with one compensating transaction. The Camunda community edition is used. 

## Binding Java classes to service tasks

![](https://github.com/nagypet/camundastudy/blob/main/doc/pics/reserve-stock-camunda.jpg)

```
@Slf4j
public class ReserveStockDelegate implements JavaDelegate
{
    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        boolean available = new Random().nextBoolean();
        log.debug(String.format("Reserving stock... Available: %b", available));
        execution.setVariable(Constants.PROCESS_VARIABLE_STOCK_AVAILABLE, available);
    }
}
```

## Using expression in a gateway

![](https://github.com/nagypet/camundastudy/blob/main/doc/pics/is-stock-available-yes.jpg)

## The Send email task
```
@Slf4j
public class SendEmailDelegate implements JavaDelegate
{
    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        Boolean stockAvailable = (Boolean) execution.getVariable(Constants.PROCESS_VARIABLE_STOCK_AVAILABLE);
        Boolean paymentSuccessfull = (Boolean) execution.getVariable(Constants.PROCESS_VARIABLE_PAYMENT_SUCCESSFULL);

        if (BooleanUtils.isNotTrue(stockAvailable))
        {
            log.debug("Sending email: sorry, stock is not available!");
        }
        else if (BooleanUtils.isNotTrue(paymentSuccessfull))
        {
            log.debug("Sending email: sorry, your payment was not successfull!");
        }
        else
        {
            log.debug("Sending email: your order has been successfully placed!");
        }
    }
}
```

## The Camunda database

Camunda will connect automatically to the primary data source, I could not find any solution to set the datasource otherwise. But it is possible to create a secondary or even tertiary data source as proposed in this study. Check out the db package.

https://localhost:8400/h2

- JDBC URL: jdbc:h2:mem:camunda
- User: sa
- Password: sa

![](https://github.com/nagypet/camundastudy/blob/main/doc/pics/h2-access.jpg)

The table `ACT_HI_PROCINST` contains the process instances:

![](https://github.com/nagypet/camundastudy/blob/main/doc/pics/h2-db.jpg)

## Idempotent transactions

The SAGA pattern can only work reliable, if all transactions are idempotent. This means you must be able to call each transaction any number of times, the repeated execution may not change the state of the given service. Let's imagine the `Charge creditcard` task has succesfully finished its work, and right after it the orchestrator crashes, so it cannot persist the state change. The next time the orchestrator restarts, it will repeat the last action, so it will call the `Charge creditcard` action once more. Of course we must not charge the buyer's credit card twice. 

How to achieve this behaviour?

As an example the `Charge creditcard` action may keep a record of the transactions performed, for example, based on the camunda process ID. If the service notices that it have already completed the transaction with that process ID, it will not execute it again. Assuming that charging the credit card and updating the records are executed in one database transaction, this may work well.

This is also called the [idempotent consumer](https://microservices.io/patterns/communication-style/idempotent-consumer.html) pattern.


