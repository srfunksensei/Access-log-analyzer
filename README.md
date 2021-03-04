# Access log analyzer

Simple Java parser that parses web server access log file, loads the log to MySQL and checks if a given IP makes more than a certain number of requests for the given duration. 

- Log file format: Date, IP, Request, Status, User Agent
- The delimiter of the log file is pipe (|)
- The tool takes `startDate`, `duration` and `threshold` as command line arguments. 
    * `startDate` is of `yyyy-MM-dd.HH:mm:ss` format
    * `duration` can take only `hourly` and `daily` as inputs
    * `threshold` is integer type
- The tool assumes **200** as hourly limit and **500** as daily limit

Additionally, there is a [SQL script](initial.sql) which 
- creates db schema
- exposes procedure to find IPs that mode more than a certain number of requests for a given time period
- exposes procedure to find requests made by a given IP
 	
## Prerequisites

1. Java 8
2. Maven 3.3 (or higher)

## How to run application

To be able to see the application in action you must follow these steps:

1. run `mvn clean install package`. This command will generate two jars, one with dependencies and the other one without. Use the one with dependencies to run app quicker
2. run `java -cp "parser.jar" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100`

You can use [access.log](access.log) file provided, or some other file which matches the format defined above.

## What to expect as a result

This is how the tool works:

    java -cp "parser.jar" com.ef.Parser --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100
	
The tool will find any IPs that made more than **100** requests starting from **2017-01-01.13:00:00** to **2017-01-01.14:00:00** (one hour) and print them to console AND also load them to another MySQL table with comments on why it's blocked.

	java -cp "parser.jar" com.ef.Parser --startDate=2017-01-01.13:00:00 --duration=daily --threshold=250

The tool will find any IPs that made more than **250** requests starting from **2017-01-01.13:00:00** to **2017-01-02.13:00:00** (24 hours) and print them to console AND also load them to another MySQL table with comments on why it's blocked.

All found IPs will be printed on the standard output as well.

## License

This product is available under the MIT license. See the [LICENSE](LICENSE) file for more info. 