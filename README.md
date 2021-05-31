# camundastudy

This study demonstrates:
- The SAGA pattern
- How to create an orchestrator with Camunda

Content
- Cloning the repository
- Building the project
- Order process
- Binding Java classes to service tasks
- Using expression in a gateway
- The Send email task
- The Camunda database
- Storing history
- Idempotent transactions

## Cloning the repository

```
git clone https://github.com/nagypet/camundastudy.git
```

## Building the project

Change dir to the orchestrator folder and run:

```
c:\np\github\camundastudy\orchestrator>gradlew clean dist
Included projects:
Dependency check took 7 ms

BUILD SUCCESSFUL in 4s
8 actionable tasks: 8 executed
```

After successful compilation start the `run.bat`. 

Check if server is up and running. 

https://localhost:8400/admin-gui

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

## Storing history

Since the community edition does not support storing execution history, we have to make it on our own.

I have created a TaskHistoryService, which is able to store the execution history in an own database.

```
@Slf4j
public class ChargeCreditcardDelegate implements JavaDelegate
{
    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        // Store history
        SpringContext.getBean(TaskHistoryService.class).storeHistory(execution);
        ...
    }
}
```

![](https://github.com/nagypet/camundastudy/blob/main/doc/pics/task-history.jpg)

## Idempotent transactions

The SAGA pattern can only work reliable, if all transactions are idempotent. This means you must be able to call each transaction any number of times, the repeated execution may not change the state of the given service. Let's imagine the `Charge creditcard` task has succesfully finished its work, and right after it the orchestrator crashes, so it cannot persist the state change. The next time the orchestrator restarts, it will repeat the last action, so it will call the `Charge creditcard` action once more. Of course we must not charge the buyer's credit card twice. 

How to achieve this behaviour?

As an example the `Charge creditcard` action may keep a record of the transactions performed, for example, based on the camunda process ID. If the service notices that it have already completed the transaction with that process ID, it will not execute it again. Assuming that charging the credit card and updating the records are executed in one database transaction, this may work well.

This is also called the [idempotent consumer](https://microservices.io/patterns/communication-style/idempotent-consumer.html) pattern.


