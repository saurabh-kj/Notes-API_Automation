# Notes-API_Automation

This project is an API automation framework built using Java, RestAssured, Extent Report, TestNG Framework and Maven. It aims to provide a robust and scalable solution for automating API testing and generating detailed test reports.

## Prerequisites
Make sure you have the following software installed on your machine:

* Java Development Kit (JDK) - version 11.0 higher
* RestAssured - Latest Stable version
* TestNG - Latest Stable version
* Maven - Latest Stable version
* Git - version 2.34 or higher

## Getting Started
1. Follow these steps to get started with the API automation project:

`git clone https://github.com/saurabh-kj/Notes-API_Automation.git`

2. Install project dependencies using Maven:
`mvn clean install`

3. Open the project in your preferred IDE (e.g., IntelliJ, Eclipse).


## Configuration
Configure the API endpoints, authentication details, and other settings in the config package to suit your specific API testing requirements.

## Writing Tests
Write your API test cases using RestAssured in the tests package. You can create new test classes or modify existing ones to suit your test scenarios.

## Running Tests
To run the API tests, execute the following command from the project directory:
`mvn test`

The test results will be displayed in the console, and an Extent Report will be generated in the reports directory.

## Test Reports
After running the tests, an Extent Report is generated in the reports directory. You can open the HTML report in any web browser to view the detailed test results, including test case status, response details, and logs.

## Contributing
Contributions are always welcome! If you find any issues or have suggestions for improvement, please open an issue or submit a pull request.
