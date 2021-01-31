# Java Coding Challenge

## Introduction
Given is a Spring Boot application that implements a REST service for manipulating ads and customers. This application is already runnable, but does not do anything useful, yet. Your task is to make it more useful. The scenario of this application is a very simplified version of mobile.de's domain - we have customers, and these customers want to list ads on our platform.

It is very likely that you will not be able to finish all subtasks in the given time frame. You are basically free to choose which subtasks you work on, but keep in mind that some of the subtasks depend on others. They are listed here in no particular order. Some of the subtasks will have a bigger impact on the overall assessment than others.

You have one week to finish this challenge.

## Tasks
1. The classes in this artifact are all in one package called `de.mobile`. Create a proper package structure and move the classes to where they belong.
2. Make the domain objects `MobileAd` and `MobileCustomer` persist, either on a local `mysqld` or a local `mongodb`.
3. Currently the application only handles read use cases for ads. Extend the application so that ads can also be created.
4. There is a `MobileCustomer` class in the artifact. Extend the application so that it is also possible to create and delete customers.
5. Make sure that the ad related usecases become customer-aware (meaning that an ad needs a customer in every case).
6. Add validation to the calls that create new entities. The following rules should apply:
   - An ad needs a customer id, a make name, a model name and a category.
   - A customer needs a formally valid email address, a firstname and a lastname composed of alphanumeric characters.
7. Configure logging properly so that log messages are logged to a file.
8. Implement a proper error handling for 404 and 500, with different error messages. Please describe how to reproduce both error statuses.
9. Change the project so that it builds an executable jar.
10. Create a simple HTML/Javascript application that talks to the above REST service. It should be able to list all ads. Making it look pretty would be a plus.
11. If the ad data and the customer data were not accessible from the database but from other RESTful services, what approach would you use to integrate these services? 
12. If you decide not to write tests for your work for reasons of timeboxing, please spend a few minutes to describe what parts of the application you would write tests for, and what aspects these tests would cover, and what role they play in the development process.

Good Luck!
