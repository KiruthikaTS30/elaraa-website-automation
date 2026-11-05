 Elaraa Studio – Website Automation Testing

Project Overview

This project focuses on automating the functional testing of the Elaraa Studio e-commerce website using Selenium WebDriver with Java.

The main objective is to verify important website functionalities, identify issues and improve application quality through automated testing.

 Application Under Test

- Website: https://elaraastudio.com/home
- Application Type: E-commerce Website
- Testing Type: Functional Testing and UI Testing

Technologies Used

- Java 17
- Selenium WebDriver
- TestNG
- Maven
- Chrome Browser
- GitHub Actions

Project Features Tested

The project includes automated test scenarios for:

1. Homepage verification
2. Homepage content verification
3. Login button functionality
4. Campaign page navigation
5. Empty mobile number validation
6. Invalid mobile number input
7. Search field visibility
8. Product search functionality
9. Empty search behaviour
10. Wishlist button functionality
11. Shopping bag button functionality
12. Profile menu verification
13. Footer content verification
14. Kurtis category navigation
15. Homepage image verification
16. Bottom Wear category navigation
17. Chudidar Sets category navigation
18. Tops category navigation
19. Product details page verification
20. Product size selection

Project Structure


elaraa-website-automation
│
├── src
│   └── test
│       └── java
│           └── com
│               └── elaraa
│                   └── tests
│                       └── HomePageTest.java
│
├── pom.xml
├── README.md
└── .github
    └── workflows
        └── test.yml
```

How to Run the Project

Prerequisites

- Java JDK 17
- Maven
- Google Chrome
- Git

### Execution Command

Run the following command in the project directory:

```bash
mvn -B clean test
```

Test Execution Results

Latest successful execution:

- Total Tests: 20
- Passed: 20
- Failed: 0
- Skipped: 0
- Build Status: SUCCESS

CI/CD Integration

GitHub Actions is used to execute the automated test suite.

The workflow runs Maven tests and displays the execution results.

## Manual Testing Observations

During exploratory testing, the following potential issues were identified:

1. Product images partially displayed.
2. Duplicate products displayed.
3. Excessive gap between product images and details.
4. Share button not responding.
5. Some categories displaying "Oops, Nothing Here".
6. Gift campaign opening the Kurti collection.

These observations require further verification and are not yet confirmed as fixed or resolved.

Project Outcome

This project helped develop practical knowledge of:

- Selenium WebDriver automation
- Java programming
- TestNG test execution
- Maven project management
- Functional testing
- UI testing
- GitHub Actions CI execution
- Identifying and documenting website defects

**Project Status:** 20 automated test cases successfully executed.
